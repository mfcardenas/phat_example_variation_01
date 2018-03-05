/*
 * Copyright (C) 2014 Pablo Campillo-Sanchez <pabcampi@ucm.es>
 *
 * This software has been developed as part of the
 * SociAAL project directed by Jorge J. Gomez Sanz
 * (http://grasia.fdi.ucm.es/sociaal)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package phat.clients;

import com.espertech.esper.client.*;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.connectors.elasticsearch2.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch2.RequestIndexer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import phat.sensors.accelerometer.AccelerationData;
import phat.sensors.accelerometer.XYShiftingAccelerationsChart;
import phat.server.PHATServerManager;
import phat.beans.SimSensorEvent;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author pablo
 */
public class RemoteClientTest {

    private static final Logger logger = Logger.getLogger(RemoteClientTest.class.getName());

    private static EPServiceProvider cep;
    private static EPRuntime cepRT;

    /**
     * PSMJ.
     * @param args args.
     */
    public static void main(String[] args) {
        launchRemoteXYChart("remote", "127.0.0.1", 60000);
    }

    /**
     * Launch Remote Chart.
     * @param title title chart.
     * @param host host connect.
     * @param port port connect.
     */
    private static void launchRemoteXYChart(final String title, final String host, final int port) {
        new Thread() {
            public void run() {
                Socket s;
                try {
                    final XYShiftingAccelerationsChart chart = new XYShiftingAccelerationsChart(title, "Chest accelerations", "m/s2", "x,y,z");
                    chart.showWindow();
                    s = new Socket(host, port);
                    if (s==null) throw new RuntimeException("Could not connect to host "+ PHATServerManager.getAddress()+" at port "+ PHATServerManager.getPort());
                    BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String objRead = null;
                    Long lastRead = new Date().getTime();

                    do {
                        objRead = is.readLine();
                        final long interval = new Date().getTime() - lastRead;
                        lastRead = new Date().getTime();

                        if (objRead != null && !objRead.isEmpty()) {
                            final SimSensorEvent sse = SimSensorEvent.fromString(objRead);
                            if (sse != null){
                                Configuration config = new Configuration();
                                config.addEventType("SimSensorEvent", SimSensorEvent.class.getName());

                                cep = EPServiceProviderManager.getProvider("simulation_data", config);
                                EPAdministrator queryApply = cep.getEPAdministrator();
                                cepRT = cep.getEPRuntime();

                                String epl = "select type, step, accuracy, ax, ay, az from SimSensorEvent ";
                                EPStatement statement = queryApply.createEPL(epl);
                                statement.addListener( (newData, oldData) -> {
                                    int type = (int) newData[0].get("type");
                                    long step = (long) newData[0].get("step");
                                    int accuracy = (int) newData[0].get("accuracy");
                                    float x = (float) newData[0].get("ax");
                                    float y = (float) newData[0].get("ay");
                                    float z = (float) newData[0].get("az");
                                    //float timestamp = (float) newData[0].get("timestamp");
                                    System.out.println(String.format(" -->CEP --> {type: %s, step: %s, accuracy: %s, x: %f, y: %f, z: %f, timestamp: %d }",
                                            type, step, accuracy, x, y, z, new Date().getTime()));
                                });

                                cepRT.sendEvent(sse);

                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        AccelerationData ad = new AccelerationData(
                                                interval, sse.getAx(), sse.getAy(), sse.getAz());
                                        chart.update(null, ad);
                                        chart.repaint();
                                    }
                                });
                            }
                        }
                    } while (objRead != null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Crear los indices.
     */
    public static class SimulationGenerate
            implements ElasticsearchSinkFunction<Tuple5<String, Integer, Double, Double, Integer>> {

        /**
         *
         * @param record a
         * @param ctx b
         * @param indexer c
         */
        @Override
        public void process(Tuple5<String, Integer, Double, Double, Integer> record, RuntimeContext ctx,
                            RequestIndexer indexer) {
            // construct JSON document to index
            Map<String, String> json = new HashMap<>();
            json.put("human", record.f0);                                               // hashtag
            json.put("agent_count", record.f1.toString());                              // followers count
            json.put("location", record.f2 + "," + record.f3);                          // lat,lon pair
            json.put("count", record.f4.toString());                                    //count of the hashtag
            IndexRequest rqst = Requests.indexRequest().index("agent-locations")        // index name
                    .type("agent-location")                                             // mapping name
                    .source(json);

            System.out.println(json.toString());
            indexer.add(rqst);
        }
    }
}
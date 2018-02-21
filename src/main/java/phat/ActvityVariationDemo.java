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
package phat;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import phat.agents.actors.parkinson.HandTremblingControl;
import phat.app.PHATApplication;
import phat.app.PHATInitAppListener;
import phat.beans.Body;
import phat.beans.PhatSimulationBean;
import phat.body.BodiesAppState;
import phat.body.commands.AlignWithCommand;
import phat.body.commands.CloseObjectCommand;
import phat.body.commands.GoCloseToObjectCommand;
import phat.body.commands.OpenObjectCommand;
import phat.commands.MovArmCommand;
import phat.commands.PHATCommand;
import phat.commands.PHATCommandListener;
import phat.devices.DevicesAppState;
import phat.devices.commands.CreateSmartphoneCommand;
import phat.devices.commands.SetDeviceOnFurnitureCommand;
import phat.devices.commands.SetDeviceOnPartOfBodyCommand;
import phat.environment.SpatialEnvironmentAPI;
import phat.mobile.servicemanager.server.ServiceManagerServer;
import phat.mobile.servicemanager.services.Service;
import phat.sensors.accelerometer.AccelerationData;
import phat.sensors.accelerometer.AccelerometerControl;
import phat.sensors.accelerometer.XYAccelerationsChart;
import phat.sensors.accelerometer.XYShiftingAccelerationsChart;
import phat.server.PHATServerManager;
import phat.server.ServerAppState;
import phat.structures.houses.commands.CreateHouseCommand;
import phat.utils.ReadJSON;
import phat.utils.Utils;
import phat.world.WorldAppState;
import sim.android.hardware.service.SimSensorEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Class example Test rum simulatios.
 * @author UCM
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ActvityVariationDemo implements PHATInitAppListener{

    private static final Logger logger = Logger.getLogger(ActvityVariationDemo.class.getName());
    
    /**
     * Phat Simulation Object.
     */
    PhatSimulationBean phatSimulation;
    
    /**
     * Bodies App State.
     */
    BodiesAppState bodiesAppState;
    DevicesAppState devicesAppState;
    WorldAppState worldAppState;
    ServerAppState serverAppState;
    
    public void init(SimpleApplication app) {
    	
    	String fileConfig = "config_simulation_v3.json";
    	
    	ScriptEngineManager manager = new ScriptEngineManager();
    	ScriptEngine engine = manager.getEngineByName("Renjin");

    	AppStateManager stateManager = app.getStateManager();
        app.getFlyByCamera().setMoveSpeed(10f);

        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        SpatialEnvironmentAPI seAPI = SpatialEnvironmentAPI.createSpatialEnvironmentAPI(app);
        seAPI.getWorldAppState().setCalendar(2016, 2 ,18, 12, 30, 0);
                
        setPhatSimulation(ReadJSON.initPhatSimConfig(fileConfig));
        
        bulletAppState.setDebugEnabled(phatSimulation.isDebug());
        seAPI.getHouseAppState().runCommand(new CreateHouseCommand(phatSimulation.getNameHouse(), Utils.getTypeHouse(phatSimulation.getTypeHouse())) );
                	
        bodiesAppState = new BodiesAppState();
        stateManager.attach(bodiesAppState);
        
        //Se crean los personajes y se posicionan en el espacio
        for(Body body: phatSimulation.getBodies()){
            bodiesAppState.createBody(Utils.getTypeBody(body.getType()), body.getName());
            bodiesAppState.setInSpace(body.getName(), phatSimulation.getNameHouse(), body.getPosition());
        }
        
        devicesAppState = new DevicesAppState();
        stateManager.attach(devicesAppState);
        
        devicesAppState.runCommand(new CreateSmartphoneCommand("SmartWatch1").setDimensions(0.03f, 0.03f, 0.01f));
        devicesAppState.runCommand(new SetDeviceOnPartOfBodyCommand("Patient1", "SmartWatch1", 
                SetDeviceOnPartOfBodyCommand.PartOfBody.Chest));
        
        devicesAppState.runCommand(new CreateSmartphoneCommand("SmartWatch2").setDimensions(0.03f, 0.03f, 0.01f));
        devicesAppState.runCommand(new SetDeviceOnPartOfBodyCommand("Patient1", "SmartWatch2", 
                SetDeviceOnPartOfBodyCommand.PartOfBody.RightHand));
        
        devicesAppState.runCommand(new SetDeviceOnFurnitureCommand("deviceId", "House1", "furnitureId"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        new Thread() { public void run() {
            launchRemoteXYChart(PHATServerManager.getAddress(),"Remote Chest Sensor"
                    ,"SmartWatch1");
            launchRemoteXYChart(PHATServerManager.getAddress(),"Remote Right Hand"
                    ,"SmartWatch2");

        } }.start();
        
        stateManager.attach(new AbstractAppState() {

        	@Override
            public void initialize(AppStateManager asm, Application aplctn) {}
            
            boolean init = false;

            @Override
            public void update(float f) {
            	if(!init){
            		AccelerometerControl ac = devicesAppState.getDevice("SmartWatch1").getControl(AccelerometerControl.class);
                    ac.setMode(AccelerometerControl.AMode.ACCELEROMETER_MODE);
                    XYAccelerationsChart chart = new XYAccelerationsChart("Chart - Accelometer Mode, Chest", "Smartphone 1 Mov", "m/s2", "x,y,z");
                    ac.add(chart);
                    chart.showWindow();
                    init = true;	
            	}
            }
        });
        
        stateManager.attach(new AbstractAppState() {

        	@Override
            public void initialize(AppStateManager asm, Application aplctn) {}
            
            boolean init = false;

            @Override
            public void update(float f) {
            	if(!init){
            		AccelerometerControl ac = devicesAppState.getDevice("SmartWatch2").getControl(AccelerometerControl.class);
                    ac.setMode(AccelerometerControl.AMode.GRAVITY_MODE);
                    XYAccelerationsChart chart = new XYAccelerationsChart("Chart - Gravity Mode, Right Hand", "Smartphone 2 Mov", "m/s2", "x,y,z");
                    ac.add(chart);
                    chart.showWindow();
                    init = true;	
            	}
            }
        });

        /*phatSimulation.setCommands(ReadJSON.initPhatSimCommands(url));
        if(phatSimulation.getCommands() != null && phatSimulation.getCommands().size() > 0){
        	for(Object obj: phatSimulation.getCommands().values()){
        		bodiesAppState.runCommand((PHATCommand) obj);
        	}
        }*/

        openObject("Patient","Fridge1");
        goCloseObjectSane("Patient1", new String[]{"Fridge1"}, false);
        app.getCamera().setLocation(new Vector3f(7f, 7.25f, 6.1f));
        app.getCamera().setRotation(new Quaternion(0.37133554f, -0.6016627f, 0.37115145f, 0.60196227f));
    }

    /**
     * Go to Close Object Sane.
     * @param source source
     * @param target target
     * @param forget forget
     */
    private void goCloseObjectSane(final String source, final String[] target, final boolean forget) {
    	logger.info("goToClose: " + source + ", object: " + target);
        GoCloseToObjectCommand gtc = new GoCloseToObjectCommand(source, target[0], new PHATCommandListener() {
        	
            @Override
            public void commandStateChanged(PHATCommand command) {

            	if (command.getState() == PHATCommand.State.Success) {
                	bodiesAppState.runCommand(new MovArmCommand(source, false, MovArmCommand.LEFT_ARM));
                	bodiesAppState.runCommand(new AlignWithCommand(source, target[0]));
                	if(!forget){
                		bodiesAppState.runCommand(new CloseObjectCommand(source, target[0]));
                	}
                	bodiesAppState.runCommand(new MovArmCommand(source,true, MovArmCommand.LEFT_ARM));
                }
                
                if(command.getState().equals(PHATCommand.State.Running)){
                	
                }
            }
        });
        gtc.setMinDistance(0.1f);
        bodiesAppState.runCommand(gtc);
    }
    
    private void goToCrazy(final String source, final String target){
    	logger.info("--> GoCrazy from source: " + source + " to " + target);
    	GoCloseToObjectCommand gtc = new GoCloseToObjectCommand(source, target, new PHATCommandListener() {
    		
            @Override
            public void commandStateChanged(PHATCommand command) {
                if (command.getState() == PHATCommand.State.Running) {
                	Node body = bodiesAppState.getBodiesNode();
                	HandTremblingControl htcR = new HandTremblingControl(HandTremblingControl.Hand.RIGHT_HAND);
                    body.addControl(htcR);  
                    HandTremblingControl htcL = new HandTremblingControl(HandTremblingControl.Hand.LEFT_HAND);
                    body.addControl(htcL);
                }
            }
        });
        gtc.setMinDistance(0.1f);
        bodiesAppState.runCommand(gtc);
    }
    
    /**
     * Open Door Actions.
     * @param source
     * @param target 
     */
    private void openObject(final String source, final String target) {
        OpenObjectCommand gtc = new OpenObjectCommand(source, target);
        bodiesAppState.runCommand(gtc);
    }
    
    /**
     * Main Class, executios Test.
     * @param args 
     */
    public static void main(String[] args) {
        ActvityVariationDemo app = new ActvityVariationDemo();
        PHATApplication phat = new PHATApplication(app);
        
        phat.setDisplayFps(false);
        phat.setDisplayStatView(false);
        phat.setShowSettings(false);
        
        phat.start();
    }

	/**
	 * @return the phatSimulation
	 */
	public PhatSimulationBean getPhatSimulation() {
		return phatSimulation;
	}

	/**
	 * @param phatSimulation the phatSimulation to set
	 */
	public void setPhatSimulation(PhatSimulationBean phatSimulation) {
		this.phatSimulation = phatSimulation;
	}

    public static void launchRemoteXYChart(final InetAddress host, final String title, final String sensor) {
        Service taccelService = ServiceManagerServer.getInstance().getServiceManager().getService("accel", sensor);
        while (taccelService == null) {
            // not ready
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            taccelService = ServiceManagerServer.getInstance().getServiceManager().getService("accel", sensor);
        }
        final Service accelService = taccelService;
        new Thread() {
            public void run() {
                Socket s = null;
                try {
                    final XYShiftingAccelerationsChart chart = new XYShiftingAccelerationsChart(title,
                            "Remote " + sensor + ":" + title + " accelerations", "m/s2", "x,y,z");
                    chart.showWindow();
                    for (int k = 0; k < 5 && s == null; k++)
                        try {
                            s = new Socket(host, accelService.getPort());
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                Thread.currentThread().sleep(500);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }

                        }

                    BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));

                    String objRead = null;
                    Long lastRead = new Date().getTime();
                    do {

                        objRead = is.readLine();
                        final long interval = new Date().getTime() - lastRead;
                        lastRead = new Date().getTime();
                        if (objRead != null && !objRead.isEmpty()) {
                            final SimSensorEvent sse = SimSensorEvent.fromString(objRead);
                            if (sse != null) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        AccelerationData ad = new AccelerationData(interval, sse.getValues()[0],
                                                sse.getValues()[1], sse.getValues()[2]);
                                        chart.update(null, ad);
                                        chart.repaint();
                                    }
                                });
                            }
                        }
                        ;
                    } while (objRead != null);
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }
   
}
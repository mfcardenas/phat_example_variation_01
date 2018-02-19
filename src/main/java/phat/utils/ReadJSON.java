/**
 * 
 */
package phat.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import phat.beans.Body;
import phat.beans.Command;
import phat.beans.PhatSimulationBean;

/**
 * Class read configuration from file JSON.
 * @author mcardenas
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReadJSON {

	private static final Logger logger = Logger.getLogger(ReadJSON.class.getName());
	
	/**
     * Initialing param for simulation 
     * @param url a
     * @return b
     */
    public static PhatSimulationBean initPhatSimConfig(String url) {
		
		JSONParser parser;
		JSONObject jsonObject;
		Gson gson = new Gson();
		
		PhatSimulationBean phatSimulation = null;
		Utils utl = new Utils();
		
		try {
			parser = new JSONParser();
//			Object obj_ = parser.parse(new FileReader(url));
			Object obj_ = parser.parse(utl.getFileConfig(url));
			jsonObject = (JSONObject) obj_;
			phatSimulation = (gson.fromJson(jsonObject.toString(), PhatSimulationBean.class));
		} catch(IOException | org.json.simple.parser.ParseException ex2){
			ex2.printStackTrace();
			logger.warning("Error preparing data config for simulations");
		}
		return phatSimulation;
	}
    
    /**
     * Initialing commands for execution in the simulation.
     * @param url a
     * @return commands.
     */
    public static HashMap<String, Object> initPhatSimCommands(String url) {
    	JSONObject jsonObject;
    	JSONArray temp;
    	
		JSONParser parser;
		Gson gson = new Gson();
		Object command;
		
		PhatSimulationBean phatSimulationBean = new PhatSimulationBean();
	    
		try {
			parser = new JSONParser();
			Object obj_ = parser.parse(new FileReader(url));
			jsonObject = (JSONObject) obj_;
			
			temp = (JSONArray) jsonObject.get("commands_");
			if(temp != null && temp.size() > 0){
				phatSimulationBean.setCommands(new HashMap<String,Object>());
				for (Object aTemp : temp) {
					command = new ArrayList<Object>();
					JSONObject obj = (JSONObject) aTemp;
					try {
						command = gson.fromJson(obj.toString(), Class.forName(obj.get("packageClass").toString() +
								"." + obj.get("typeClass").toString()));
						phatSimulationBean.getCommands().put(obj.get("id").toString(), command);
					} catch (JsonSyntaxException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		} catch(IOException | org.json.simple.parser.ParseException ex2){
			ex2.printStackTrace();
		}
		return phatSimulationBean.getCommands();
	}
    
    
	public static boolean initJSONConfig(String url) {
		
		boolean ret = false;
		JSONParser parser = new JSONParser();
		JSONArray temp;
		JSONObject jsonObject;
		HashMap<String, Object> config;
		
		List<Object> bodies;
		List<Object> commands;
		
		Gson gson = new Gson();
	    
		try {
			parser = new JSONParser();
			Object obj_ = parser.parse(new FileReader(url));
			jsonObject = (JSONObject) obj_;
			
			config = new HashMap();
			config.put("name", jsonObject.get("name").toString());
			config.put("debug", jsonObject.get("debug"));
			config.put("worlstation", jsonObject.get("worlstation").toString());
			config.put("nameHouse", jsonObject.get("nameHouse").toString());
			config.put("typeHouse", jsonObject.get("typeHouse").toString());
			
			temp = (JSONArray) jsonObject.get("bodies");
			if(temp.size() > 0){
				bodies = new ArrayList();
				for (Object aTemp : temp) {
					JSONObject obj = (JSONObject) aTemp;
					Body body = gson.fromJson(obj.toString(), Body.class);
					bodies.add(body);
				}	
			}
			
			temp = (JSONArray) jsonObject.get("commands");
			if(temp.size() > 0){
				commands = new ArrayList();
				for (Object aTemp : temp) {
					JSONObject obj = (JSONObject) aTemp;
					Command command = gson.fromJson(obj.toString(), Command.class);
					commands.add(command);
				}
			}
			ret = true;
		} catch(IOException | org.json.simple.parser.ParseException ex2){
			ex2.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * @param args a
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

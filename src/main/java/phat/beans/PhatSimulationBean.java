/**
 * 
 */
package phat.beans;

import java.util.HashMap;
import java.util.List;

/**
 * @author UCM
 * @param <T>
 *
 */
public class PhatSimulationBean<T> {
	
	/**
	 * Name Simulation Phat.
	 */
	private String name;
	
	/**
	 * Worlstation Simulation Phat.
	 */
	private String worlstation;
	
	/**
	 * debug Simulation Phat.
	 */
	private boolean debug;
	
	/**
	 * nameHouse Simulation Phat.
	 */
	private String nameHouse;
	
	/**
	 * typeHouse Simulation Phat.
	 */
	private String typeHouse;
	
	
	/**
	 * Bodies Simulation Phat.
	 */
	private Body[] bodies;
	
	/**
	 * List Commands for simulations.
	 */
	private HashMap<String, Object> commands;
	
	/**
	 * Script commands Simulation Phat.
	 */
	private ScriptCommand[] scriptCommands;
	
	/**
	 * Controls Simulation Phat.
	 */
	private List<Object> controls;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the worlstation
	 */
	public String getWorlstation() {
		return worlstation;
	}

	/**
	 * @param worlstation the worlstation to set
	 */
	public void setWorlstation(String worlstation) {
		this.worlstation = worlstation;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @return the nameHouse
	 */
	public String getNameHouse() {
		return nameHouse;
	}

	/**
	 * @param nameHouse the nameHouse to set
	 */
	public void setNameHouse(String nameHouse) {
		this.nameHouse = nameHouse;
	}

	/**
	 * @return the typeHouse
	 */
	public String getTypeHouse() {
		return typeHouse;
	}

	/**
	 * @param typeHouse the typeHouse to set
	 */
	public void setTypeHouse(String typeHouse) {
		this.typeHouse = typeHouse;
	}

	/**
	 * @return the bodies
	 */
	public Body[] getBodies() {
		return bodies;
	}

	/**
	 * @param bodies the bodies to set
	 */
	public void setBodies(Body[] bodies) {
		this.bodies = bodies;
	}

	/**
	 * @return the setCommands
	 */
	public ScriptCommand[] getScriptCommands() {
		return scriptCommands;
	}

	/**
	 * @param setCommands the setCommands to set
	 */
	public void setScriptCommands(ScriptCommand[] scriptCommands) {
		this.scriptCommands = scriptCommands;
	}

	/**
	 * @return the controls
	 */
	public List<Object> getControls() {
		return controls;
	}

	/**
	 * @param controls the controls to set
	 */
	public void setControls(List<Object> controls) {
		this.controls = controls;
	}

	/**
	 * @return the commands
	 */
	public HashMap<String, Object> getCommands() {
		return commands;
	}

	/**
	 * @param commands the commands to set
	 */
	public void setCommands(HashMap<String, Object> commands) {
		this.commands = commands;
	}
	
}

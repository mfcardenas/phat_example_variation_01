/**
 * 
 */
package phat.beans;

/**
 * @author UCM
 *
 */
public class ScriptCommand {
	
	/**
	 * Id command script.
	 */
	private String id;
	
	/**
	 * stateControl execution for script.
	 */
	private String stateControl;
	
	/**
	 * Active script.
	 */
	private boolean active;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the stateControl
	 */
	public String getStateControl() {
		return stateControl;
	}

	/**
	 * @param stateControl the stateControl to set
	 */
	public void setStateControl(String stateControl) {
		this.stateControl = stateControl;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}

/**
 * 
 */
package phat.beans;

/**
 * Class Command implementatios JSON read.
 * @author UCM
 * @version 0.1
 *
 */
public class Command {

	/**
	 * Type Class.
	 */
	private String typeClass;
	
	/**
	 * Name
	 */
	private String name;
	
	/**
	 * Id command. 
	 */
	private String id;
	
	/**
	 * Source applique command.
	 */
	private String source;
	
	/**
	 * Target applique command.
	 */
	private String target;
	
	/**
	 * Angular
	 */
	private float angular;
	
	/**
	 * Min Angle
	 */
	private float minAngle;
	
	/**
	 * Max Angle
	 */
	private float maxAngle;
	
	/**
	 * Side
	 */
	private String side;
	
	/**
	 * Body
	 */
	private String body;
	
	/**
	 * Axis
	 */
	private String axis;
	
	/**
	 * Active
	 */
	private boolean active;

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
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the axis
	 */
	public String getAxis() {
		return axis;
	}

	/**
	 * @param axis the axis to set
	 */
	public void setAxis(String axis) {
		this.axis = axis;
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

	/**
	 * @return the angular
	 */
	public float getAngular() {
		return angular;
	}

	/**
	 * @param angular the angular to set
	 */
	public void setAngular(float angular) {
		this.angular = angular;
	}

	/**
	 * @return the minAngle
	 */
	public float getMinAngle() {
		return minAngle;
	}

	/**
	 * @param minAngle the minAngle to set
	 */
	public void setMinAngle(float minAngle) {
		this.minAngle = minAngle;
	}

	/**
	 * @return the maxAngle
	 */
	public float getMaxAngle() {
		return maxAngle;
	}

	/**
	 * @param maxAngle the maxAngle to set
	 */
	public void setMaxAngle(float maxAngle) {
		this.maxAngle = maxAngle;
	}

	/**
	 * @return the side
	 */
	public String getSide() {
		return side;
	}

	/**
	 * @param side the side to set
	 */
	public void setSide(String side) {
		this.side = side;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

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
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	
	/**
	 * @return the typeClass
	 */
	public String getTypeClass() {
		return typeClass;
	}

	/**
	 * @param typeClass the typeClass to set
	 */
	public void setTypeClass(String typeClass) {
		this.typeClass = typeClass;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Command [typeClass=" + typeClass + ", name=" + name + ", id=" + id + ", source=" + source + ", target="
				+ target + ", angular=" + angular + ", minAngle=" + minAngle + ", maxAngle=" + maxAngle + ", side="
				+ side + ", body=" + body + ", axis=" + axis + ", active=" + active + "]";
	}
	
	
	
}

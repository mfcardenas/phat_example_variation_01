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
package phat.control;

import java.util.logging.Logger;

import com.jme3.animation.Bone;
import com.jme3.animation.SkeletonControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import com.jme3.scene.control.AbstractControl;

/**
 * It generate a trembling on character right arm.
 *
 * Depends on <b>SkeletonControl</b>
 *
 * @author ucm
 */
public class LeftArmMoveControl extends AbstractControl {
	
	/**
	 * logger.
	 */
	private static final Logger logger = Logger.getLogger(LeftArmMoveControl.class.getName());
    
	/**
	 * Position.
	 */
	private Vector3f position;
	
	/**
	 * Skeleton Control.
	 */
	private SkeletonControl skeletonControl;
	
	/**
	 * Rotation.
	 */
	private Quaternion rotation;
    
	/**
	 * Arm.
	 */
	private Bone arm;
	
	private boolean sw_up = true;

    Bone hand;

    private float[] angles = new float[3];
    private int index = 2;

    private float minAngle = FastMath.HALF_PI;
    private float maxAngle = 0;
    private float angular = FastMath.PI;

    /**
     * Constructor default.
     */
    public LeftArmMoveControl() {
		setMinAngle(-FastMath.PI/2);
		setMaxAngle(0);
		setAngular(FastMath.PI);
		setIndex(2);
		setAngles(new float[]{0f,0f,0f});
		
		init();
		
	}

    /**
     *
     * @param minAngle
     * @param maxAngle
     * @param angular
     */
    public LeftArmMoveControl(float minAngle, float maxAngle, float angular) {
		super();
		setMinAngle(minAngle);
		setMaxAngle(maxAngle);
		setIndex(2);
		setAngular(angular);
		
		init();
	}

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    /**
     * Init object class.
     */
    public void init(){
    	position = new Vector3f();
		rotation = new Quaternion();
    }  

	@Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (spatial != null) {
            skeletonControl = spatial.getControl(SkeletonControl.class);
            arm = skeletonControl.getSkeleton().getBone("LeftForeArm");
            rotation = new Quaternion();
        } else {
            resetForeArm();
            skeletonControl = null;
            rotation = null;
        }
    }

    private void resetForeArm() {
        logger.info(".....resetForeArm");
        if (arm != null) {
            setUserControlFrom(arm, true);
            arm.getCombinedTransform(position, rotation);
            setAngles(new float[]{0,0,0});
            rotation.fromAngles(getAngles());
            arm.setUserTransforms(position, rotation, Vector3f.UNIT_XYZ);
            updateBonePositions(arm);
        }
    }

    @Override
    protected void controlUpdate(float fps) {
        if (arm != null) {
            setUserControlFrom(arm, true);
            arm.getCombinedTransform(position, rotation);
            updateRotation(rotation, fps);
            arm.setUserTransforms(position, rotation, Vector3f.UNIT_XYZ);
            updateBonePositions(arm);
        }
    }

    private void updateRotation(Quaternion rotation, float tpf) {
        rotation.toAngles(getAngles());
        float angle = getAngles()[getIndex()];
        //logger.info("angle before: " + angle);
        
        if(angle > getMinAngle() && isSw_up()){
            angle -= getAngular() * tpf;
            //logger.info("rup " + toString() + ", diff: " + getAngular() * tpf + 
            //		" tpf: " + tpf + ", rotation:" + rotation.toString());
        }else{
        	//logger.info("rstop-up " + toString() + ", diff: " + getAngular() * tpf + 
        	//		" tpf: " + tpf + ", rotation:" + rotation.toString());
            setSw_up(false);
        }
        
        if(!isSw_up() && angle <= getMaxAngle()){
            angle += getAngular() * tpf;
            //logger.info("rdown " + toString() + ", diff: " + getAngular() * tpf + 
            //		" tpf: " + tpf + ", rotation:" + rotation.toString());
        }else{
        	//logger.info("rstop-down " + toString() + ", diff: " + getAngular() * tpf + 
        	//		" tpf: " + tpf + ", rotation:" + rotation.toString());
        }
        
        //logger.info("angle after: " + angle);
        getAngles()[getIndex()] = angle;
        //logger.info("" + getAngles()[0] + "," + getAngles()[1] + "," + getAngles()[2] +"," + tpf + "," + "left");
        rotation.fromAngles(getAngles());
    }

    private void updateBonePositions(Bone bone) {
        Transform t = new Transform();
        for (Bone b : bone.getChildren()) {
            t = b.getCombinedTransform(bone.getModelSpacePosition(), bone.getModelSpaceRotation());
            b.setUserTransformsWorld(t.getTranslation(), b.getModelSpaceRotation());
            updateBonePositions(b);
        }
    }

    private void setUserControlFrom(Bone bone, boolean userControl) {
        bone.setUserControl(userControl);
        for (Bone b : bone.getChildren()) {
            setUserControlFrom(b, userControl);
        }
    }


    /**
     * Constrol boolean.
     */
    public boolean isSw_up() {
        return sw_up;
    }

    public void setSw_up(boolean sw_up) {
        this.sw_up = sw_up;
    }

    public float[] getAngles() {
        return angles;
    }

    public void setAngles(float[] angles) {
        this.angles = angles;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getMinAngle() {
        return minAngle;
    }

    public void setMinAngle(float minAngle) {
        this.minAngle = minAngle;
    }

    public float getMaxAngle() {
        return maxAngle;
    }

    public void setMaxAngle(float maxAngle) {
        this.maxAngle = maxAngle;
    }

    public float getAngular() {
        return angular;
    }

    public void setAngular(float angular) {
        this.angular = angular;
    }
}

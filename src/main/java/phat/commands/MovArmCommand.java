/*
 * Copyright (C) 2016 UCM>
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
package phat.commands;

import com.jme3.app.Application;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import java.util.logging.Level;
import phat.body.BodiesAppState;
import phat.control.LeftArmMoveControl;
import phat.control.RightArmMoveControl;
import phat.commands.PHATCommand;
import phat.commands.PHATCommandListener;

/**
 * Class MovArmCommand util for move arm left and right.
 * @author ucm
 */
public class MovArmCommand extends PHATCommand {

    private String bodyId;
    private Boolean on;
    public static final String LEFT_ARM = "L";
    public static final String RIGHT_ARM = "R";
    private Float minAngle;
    private Float maxAngle;
    private Float angular;
    private String side;
    private int axis;
    
    public MovArmCommand() {
        super(null);
        logger.log(Level.INFO, "New Command: {0}", new Object[]{this});
    }

    /**
     *
     * @param bodyId
     * @param on
     * @param side
     * @param listener
     */
    public MovArmCommand(String bodyId, Boolean on, String side, PHATCommandListener listener) {
        super(listener);
        this.bodyId = bodyId;
        this.on = on;
        this.side = side;
        logger.log(Level.INFO, "New Command: {0}", new Object[]{this});
    }

    /**
     *
     * @param bodyId
     * @param on
     * @param side
     */
    public MovArmCommand(String bodyId, Boolean on, String side) {
        this(bodyId, on, side, null);
    }

    @Override
    public void runCommand(Application app) {
        BodiesAppState bodiesAppState = app.getStateManager().getState(BodiesAppState.class);

        Node body = bodiesAppState.getBody(bodyId);
        if (body != null) {
            if (on) {
                active(body);
            } else {
                desactive(body);
            }
        }else{
        	setState(State.Fail);
        }
        setState(State.Success);
    }

    @Override
    public void interruptCommand(Application app) {
        setState(State.Fail);
    }

    /**
     * Activate move in node left or right.
     * @param body 
     */
    private void active(Node body) {
    	
        if (side.equals(LEFT_ARM)) {
            LeftArmMoveControl htc = body.getControl(LeftArmMoveControl.class);
            if (htc == null) {
                htc = new LeftArmMoveControl();
                body.addControl(htc);
            }
            if (minAngle != null) {
                htc.setMinAngle(minAngle);
            }
            if (maxAngle != null) {
                htc.setMaxAngle(maxAngle);
            }
            if (angular != null) {
                htc.setAngular(angular);
            }
        } 
        if (side.equals(RIGHT_ARM)) {
            RightArmMoveControl htc = body.getControl(RightArmMoveControl.class);
            if (htc == null) {
                htc = new RightArmMoveControl();
                body.addControl(htc);
            }
            if (minAngle != null) {
                htc.setMinAngle(minAngle);
            }
            if (maxAngle != null) {
                htc.setMaxAngle(maxAngle);
            }
            if (angular != null) {
                htc.setAngular(angular);
            }
        }
    }

    /**
     * Desactivate move in node left or right.
     * @param body 
     */
    private void desactive(Node body) {
        if (side.equals(LEFT_ARM)) {
            LeftArmMoveControl lhtc = body.getControl(LeftArmMoveControl.class);
            if (lhtc != null) {
                body.removeControl(lhtc);
            }
        } 
        if (side.equals(RIGHT_ARM)) {
            RightArmMoveControl lhtc = body.getControl(RightArmMoveControl.class);
            if (lhtc != null) {
                body.removeControl(lhtc);
            }
        }
    }

    public Float getMinAngle() {
        return minAngle;
    }

    public void setMinAngle(Float minAngle) {
        this.minAngle = minAngle;
    }

    public Float getMaxAngle() {
        return maxAngle;
    }

    public void setMaxAngle(Float maxAngle) {
        this.maxAngle = maxAngle;
    }

    public Float getAngular() {
        return angular;
    }

    public void setAngular(Float angular) {
        this.angular = angular;
    }

    /**
	 * @return the axis
	 */
	public int getAxis() {
		return axis;
	}

	/**
	 * @param axis the axis to set
	 */
	public void setAxis(int axis) {
		this.axis = axis;
	}

	@Override
    public String toString() {
    	return getClass().getSimpleName() + "(" + bodyId + ", on=" + on + ", arm = " + (side.equals(LEFT_ARM) ? "left":"right") + ")";
    }
}

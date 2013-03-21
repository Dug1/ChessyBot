package com.bhrobotics.temp;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class Intake {
	public static final double MAX_SPEED = 1.0;
	public static final double FEEDER_POSITION = 0.0;
	public static final double GROUND_POSITION = 0.0;
	public static final double START_POSITION = 0.0;

	private double hingeSpeed = 0;
	private Talon hinge;
	private Talon rollerBottom;
	private Talon rollerTop;
	//private Encoder encoder;
	private double goalValue = 0;
	private DigitalInput topLimit;
	private DigitalInput bottomLimit;
	public final static double FLUSH_VALUE = MAX_SPEED;
	public final static double UP_VALUE = -MAX_SPEED;
	public final static double STOP = 0.0;

	public Intake(Talon hinge, Talon rollerBottom, Talon rollerTop, DigitalInput top, DigitalInput  bottom) {
		this.hinge = hinge;
		this.rollerBottom = rollerBottom;
		this.rollerTop = rollerTop;
		topLimit = top;
		bottomLimit = bottom;
		/*this.encoder = encoder;
		encoder.setDistancePerPulse(0.001);
		encoder.setMinRate(0);
		startEncoder();*/
	}

	/*public void startEncoder() {
		encoder.start();
	}

	public void stopEncoder() {
		encoder.stop();
	}*/

	public void turnOn() {
		rollerTop.set(UP_VALUE);
		rollerBottom.set(UP_VALUE);
	}
	
	public void turnOnTop() {
		rollerTop.set(UP_VALUE);
	}
	
	public void turnOffTop() {
		rollerTop.set(STOP);
	}
	
	public void turnOnBottom() {
		rollerBottom.set(UP_VALUE);
	}

	public void turnOffBottom() {
		rollerBottom.set(STOP);
	}
	
	public void turnOff() {
		rollerTop.set(STOP);
		rollerBottom.set(STOP);
	}

	public void flush() {
		rollerTop.set(FLUSH_VALUE);
		rollerBottom.set(FLUSH_VALUE);
	}

	public void bumpUp() {
		hinge.set(hingeSpeed);
	}

	public void bumpDown() {
		hinge.set(-hingeSpeed);
	}
        
        public void manualBump(double speed) {
               hinge.set(speed * MAX_SPEED);
        }
        
	public void stop() {
		hinge.set(STOP);
	}

	/*public boolean getHingeDistance() {
		encoder.start();
		return encoder.getStopped();
	}*/

	public void setHingeSpeed(double speed) {
		this.hingeSpeed = speed;
	}

	/*public void setHingePosition() {
		if (encoder.getDistance() < goalValue) {
			hinge.set(hingeSpeed);
		} else if (encoder.getDistance() > goalValue) {
			hinge.set(-hingeSpeed);
		} else {
			hinge.set(STOP);
		}
	}
	
	public void reset() {
		encoder.reset();
	}*/
	
	public boolean topPressed() {
		return topLimit.get();
	}
	
	public boolean bottomPressed() {
		return bottomLimit.get();
	}
}

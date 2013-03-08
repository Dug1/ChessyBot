package com.bhrobotics.temp;

import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.Joystick;

public class TurnCalculator implements DriveCalculator {

	private double magnitude;
	private double angle;
	private Joystick joystick;
	private int forwardAxis;
	private int turnAxis;

	public TurnCalculator(Joystick joystick, int forwardAxis, int turnAxis) {
		this.joystick = joystick;
		this.forwardAxis = forwardAxis;
		this.turnAxis = turnAxis;
	}

	public void recalculate() {
		double y = joystick.getRawAxis(forwardAxis);
		double x = joystick.getRawAxis(turnAxis);
		magnitude = Math.sqrt((y * y) + (x * x));
		if(magnitude != 0) {
			angle = MathUtils.acos(x / magnitude);
			if (y < 0) {
				angle = -angle;
			}
		} else {
			angle = 0;
		}
	}

	public double getMagnitude() {
		return magnitude;
	}

	public double getAngle() {
		return angle;
	}

}

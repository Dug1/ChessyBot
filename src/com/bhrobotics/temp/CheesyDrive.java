package com.bhrobotics.temp;

import edu.wpi.first.wpilibj.Joystick;

public class CheesyDrive implements DriveStyle {

	private Joystick joystick;

	public CheesyDrive(Joystick joystick) {
		this.joystick = joystick;
	}

	public double[] drive(boolean reverse, DriveCalculator calculator ) {
		double direction = 1.0;
		if (reverse) {
			direction = -1.0;
		}

		double magnitude = 0;
		
		if (calculator.getMagnitude() > 0.1) {
			magnitude = (1 - joystick.getRawAxis(3)) / 2;
		}
		
		calculator.recalculate();
		
		double x = Math.cos(calculator.getAngle());
		double y = direction * magnitude * Math.sin(calculator.getAngle());
		
		double[] coordinates = {x,y};	
		
		return coordinates;
	}
}

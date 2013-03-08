package com.bhrobotics.temp;


public class OneStickDrive implements DriveStyle {


	public double[] drive(boolean reverse, DriveCalculator calculator) {
		
		double direction = 1.0;
		if (reverse) {
			direction = -1.0;
		}

		calculator.recalculate();

		double x = calculator.getMagnitude() * Math.cos(calculator.getAngle());
		double y = direction * calculator.getMagnitude() * Math.sin(calculator.getAngle());
		
		double[] coordinates = {x,y};
		return coordinates;
	}

}

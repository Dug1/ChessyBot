package com.bhrobotics.temp;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class MotorModule {
	private Talon[] controllers = new Talon[2];
	private Solenoid out;
	private Solenoid in;
        public static final double THRESHOLD = 0.1;
                
	public MotorModule(int motorPortOne, int motorPortTwo, Solenoid solenoidOne, Solenoid solenoidTwo) {
		controllers[0] = new Talon(1, motorPortOne);
		controllers[1] = new Talon(1, motorPortTwo);
		in = solenoidOne;
		out = solenoidTwo;
		setHighSpeed();
	}

	public Talon[] getControllers() {
		return controllers;
	}

	public void set(double value) {
            if(Math.abs(value) < THRESHOLD) {
		getControllers()[0].set(0.0);
		getControllers()[1].set(0.0);
            } else {
                getControllers()[0].set((1/(1-THRESHOLD))*(value-((Math.abs(value)/value)*THRESHOLD)));
		getControllers()[1].set((1/(1-THRESHOLD))*(value-((Math.abs(value)/value)*THRESHOLD)));
            }
	}

	public double get() {
		return (getControllers()[0]).get();
	}

	public void setHighSpeed() {
		in.set(true);
		out.set(false);
	}

	public void setLowSpeed() {
		in.set(false);
		out.set(true);
	}

	public boolean isLowSpeed() {
		return out.get();
	}

	public boolean isHighSpeed() {
		return in.get();
	}
}
package com.bhrobotics.temp;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import java.util.Timer;
import java.util.TimerTask;

public class Shooter {
	public static final double MAX_SPEED = 1.0;
	public static final double AUTO_SPEED = 1.0;
        public static final double THRESHOLD = 0.001;
	private double speed = MAX_SPEED;
	private double stop = 0.0;
        private boolean armPushed = false;

	private Victor outside;
	private Victor inside;
	//private Encoder encoder;
	//private PIDController controller;
	private boolean stopped = false;
	private Solenoid bottomSolenoid = new Solenoid(1,3);
	private Solenoid topSolenoid = new Solenoid(1,4);
	private DigitalInput limitSwitch = new DigitalInput(4);
	private Timer timer = new Timer();
        //private Thread pistonThread;
	
	
	private class PistonThread implements Runnable {
		
		private Solenoid topSolenoid;
		private Solenoid bottomSolenoid;
		
		public PistonThread(Solenoid topSolenoid, Solenoid bottomSolenoid) {
			this.topSolenoid = topSolenoid;
			this.bottomSolenoid = bottomSolenoid;
		}
		
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				//ignore
			}
			if (topSolenoid.get() && !bottomSolenoid.get()) {
				topSolenoid.set(false);
				bottomSolenoid.set(true);
			} else {
				topSolenoid.set(true);
				bottomSolenoid.set(false);
			}
		}
	}
	
	public Shooter(Victor fast, Victor slow, Encoder encoder) {
		this.outside = fast;
		this.inside = slow;
		//this.encoder = encoder;
		//this.controller = new PIDController(1.3,0.1,0);
		topSolenoid.set(true);
		bottomSolenoid.set(false);
	}
	
	public void turnOn() {
		//controller.setGoal(speed);
                //outside.set(controller.adjust(encoder.getRate()/14000 * 60));
                inside.set(speed);
		//if(Math.abs(controller.getError()) < THRESHOLD) {
                    this.extend();
                //}
		//pistonThread = new Thread(new PistonThread(topSolenoid, bottomSolenoid));
		//pistonThread.start();
		//pistonThread.sleep(1000);
	}
	
	public void turnOff() {
                //controller.setGoal(0.0);
                //outside.set(controller.adjust(encoder.getRate()));
                inside.set(speed);
                retract();
		//pistonThread.interrupt();
	}

	public double getSpeed() {
		return outside.get();
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void start() {
		stopped = false;
		//encoder.start();
	}
	
	public void stop() {
		stopped = true;
		//encoder.stop();
	}
        
        public void extend() {
            if(!armPushed) {
                topSolenoid.set(false);
                bottomSolenoid.set(true);
                timer.schedule(new TimerTask() {
                        
                    public void run() {
                        retract();
                    }
                    
                }, 500);
            }
        }
        
        public void retract() {
            topSolenoid.set(true);
            bottomSolenoid.set(false);
            armPushed = false;
        }
}
package com.bhrobotics.temp;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import java.util.Timer;
import java.util.TimerTask;

public class Shooter {

    public static final double MAX_SPEED = 1.0;
    public static final double AUTO_SPEED = 1.0;
    public static final double THRESHOLD = 0.001;
    private double speed = MAX_SPEED;
    private double STOP = 0.0;
    private boolean armReady = true;
    private Talon outside;
    private Talon inside;
    //private Encoder encoder;
    //private PIDController controller;
    private boolean stopped = false;
    private Solenoid bottomSolenoid = new Solenoid(1, 3);
    private Solenoid topSolenoid = new Solenoid(1, 4);
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

    public Shooter(Talon fast, Talon slow, Encoder encoder) {
        this.outside = fast;
        this.inside = slow;
        //this.encoder = encoder;
        //this.controller = new PIDController(1.3,0.1,0);
        topSolenoid.set(true);
        bottomSolenoid.set(false);
    }

    public void turnOn() {
        //controller.setGoal(speed);
        outside.set(speed);
        inside.set(speed);
        //if(Math.abs(controller.getError()) < THRESHOLD) {
        //}
        //pistonThread = new Thread(new PistonThread(topSolenoid, bottomSolenoid));
        //pistonThread.start();
        //pistonThread.sleep(1000);
    }

    public void turnOff() {
        //controller.setGoal(0.0);
        outside.set(0.0);
        inside.set(0.0);
        timer.cancel();
        timer = new Timer();
        armReady = true;
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

    public void autoExtend(int linger, int time) {
        /*if(armReady) {
         extend();
         timer.schedule(new TimerTask() {
                        
         public void run() {
         retract();
         }
                    
         }, linger);
         timer.schedule(new TimerTask()
         public void run() {
         armReady = true;
         }
         }, time);
         armReady = false;
         }*/
        if (armReady) {
            armReady = false;
            timer.schedule(new TimerTask() {
                public void run() {
                    if (speed != STOP) {
                        extend();
                        System.out.println("Auto extend");
                        armReady = true;
                    }
                }
            }, time);
            timer.schedule(new TimerTask() {
                public void run() {
                    retract();
                    System.out.println("Auto retract");
                }
            }, (linger + time));
        }
    }
 
    public void extend() {
        /*if(armReady) {
         topSolenoid.set(false);
         bottomSolenoid.set(true);
         timer.schedule(new TimerTask() {
                        
         public void run() {
         retract();
         }
                    
         }, 500);
         timer.schedule(new TimerTask() {
         public void run() {
         armReady = true;
         }
         }, 1000);
         armReady = false;
         }*/
        topSolenoid.set(false);
        bottomSolenoid.set(true);
    }

    public void retract() {
        topSolenoid.set(true);
        bottomSolenoid.set(false);
    }

    public boolean armReady() {
        return armReady;
    }
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.bhrobotics.temp;

import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class ChessyBot extends IterativeRobot {

    private Joystick driverJoystick;
    private Joystick intakeJoystick;
    private MotorModule left;
    private MotorModule right;
    private Intake intake;
    private Shooter shooter;
    private DigitalInput reset;
    private Relay compressor;
    private CheesyDrive cheesy;
    private OneStickDrive stick;
    private TurnCalculator xAxis;
    private TurnCalculator twist;
    private DriveStyle style;
    private DriveCalculator calculator;
    private DigitalInput input;
    private DigitalInput other;
    private boolean runningAuto = false;
    private boolean initialWait = true;
    private Timer timer = new Timer();
    private DigitalInput valve;
    private double turningScale;
    private double normalTurning = 0.6;
    private double reallySlowTurning = 0.2;
    private Solenoid solenoidOne = new Solenoid(1, 1);
    private Solenoid solenoidTwo = new Solenoid(1, 2);
    private boolean shootReady;
    private static final int AWAYFROMPYRAMID = 0;
    private static final int TURN = AWAYFROMPYRAMID + 0;
    private static final int STOPMOVING = TURN + 0;
    private DigitalInput autoTurnToggle;

    public void robotInit() {
        driverJoystick = new Joystick(1);
        intakeJoystick = new Joystick(2);
        left = new MotorModule(1, 3, solenoidOne, solenoidTwo);
        right = new MotorModule(2, 4, solenoidOne, solenoidTwo);
        intake = new Intake(new Talon(1, 5), new Talon(1, 6), new Talon(1, 7), new DigitalInput(1, 1), new DigitalInput(1, 2));
        shooter = new Shooter(new Talon(1, 8), new Talon(1, 9), null);
        valve = new DigitalInput(1, 3);
        compressor = new Relay(1, 8);
        cheesy = new CheesyDrive(driverJoystick);
        stick = new OneStickDrive();
        //xAxis = new TurnCalculator(driverJoystick,2,1);
        twist = new TurnCalculator(driverJoystick, 2, 6);
        style = stick;
        calculator = twist;
        reset = new DigitalInput(1, 9);
        autoTurnToggle = new DigitalInput(1,1);
        //shooter.start();
    }

    public void autonomousPeriodic() {
        //Compressor
        if (!valve.get()) {
            compressor.set(Relay.Value.kForward);
        } else {
            compressor.set(Relay.Value.kOff);
        }
            
        //Shoot
        if (shootReady) {
             shooter.autoExtend(500, 4000);
        }
    }

    public void autonomousInit() {
        shooter.turnOn();
        shooter.retract();
        /*
         Timer timer = new Timer(); 

         //stop all functions
         timer.schedule(new TimerTask() {
         public void run() {
         shooter.setSpeed(0);
         intake.setHingeSpeed(0);
         while(!intake.bottomPressed()) {
         intake.bumpDown();
         }
         intake.stop();
         //intake.setHingePosition();
         }
         }, 10000);

         //turn on shooter
         timer.schedule(new TimerTask() {

         public void run() {
         shooter.setSpeed(Shooter.AUTO_SPEED);
         }

         }, 1000);

         intake.setHingeSpeed(Intake.UP_VALUE);*/
         shootReady = false;
         
         //Move Forward From Start
         timer.schedule(new TimerTask() {
             public void run() {
                 left.set(1);
                 right.set(-1);
             }
         }, AWAYFROMPYRAMID);
         
         //Turn
         if (autoTurnToggle.get()) {
            timer.schedule(new TimerTask() {
                public void run() {
                    left.set(1);                //Right turn if DI is on
                    right.set(1);
                }
            }, TURN);
         } else {
             timer.schedule(new TimerTask() {
                public void run() {
                    left.set(-1);               //Left turn if DI is off
                    right.set(-1);
                }
            }, TURN);
         }
          
         //Stop Moving
         timer.schedule(new TimerTask() {
             public void run() {
                 left.set(0);
                 right.set(0);
                 shootReady = true;
             }
         }, STOPMOVING);
    }

    private class StopMovementTask extends TimerTask {

        public void run() {
            left.set(0);
            right.set(0);
            runningAuto = false;
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        shooter.turnOff();
        shooter.start();
        shooter.retract();
        timer.cancel();
    }

    public void teleopPeriodic() {
        //Compressor
        if (!valve.get()) {
            compressor.set(Relay.Value.kForward);
        } else {
            compressor.set(Relay.Value.kOff);
        }

        // setStyle
		/*if (driverJoystick.getRawButton(9)) {
         style = cheesy;
         System.out.println("Switched to cheesy drive.");
         } else if (driverJoystick.getRawButton(10)) {
         style = stick;
         System.out.println("Switched to normal drive.");
         }*/

        // setTwist
		/*if (driverJoystick.getRawButton(11)) {
         calculator = twist;
         System.out.println("Switched to twist");
         } else if (driverJoystick.getRawButton(12)) {
         calculator = xAxis;
         System.out.println("Switched to x-axis");
         }*/

        // gear shift
        if (driverJoystick.getRawButton(1)) {
            left.setLowSpeed();
            right.setLowSpeed();
        } else {
            left.setHighSpeed();
            right.setHighSpeed();
        }

        // intake hinge
                /*
         if (intakeJoystick.getRawButton(9) && !intake.topPressed()) {
         intake.bumpUp();                    //start position
         } else if (intakeJoystick.getRawButton(11) && !intake.bottomPressed()) {
         intake.bumpDown();                     //ground position*/
        /*	} else */ if (intakeJoystick.getRawButton(1)) {
            intake.manualBump(intakeJoystick.getY() * intake.MAX_SPEED);
        } else {
            intake.stop();
        }

        //intake.setHingePosition();

        //reset intake
		/*if (reset.get()) {
         intake.reset();
         }*/

        // intake rollers


        if (intakeJoystick.getRawButton(11)) {
            intake.turnOnTop();
            /*
             if (intakeJoystick.getRawButton(5)) {
             intake.turnOnTop();
             } else {
             intake.turnOffTop();
             }
			
             if(intakeJoystick.getRawButton(3)) {
             intake.turnOnBottom();
             } else {
             intake.turnOffBottom();
             }
             */
        } else {
            intake.turnOffTop();
        }

        // shooter
        shooter.setSpeed(Shooter.MAX_SPEED);
        if (driverJoystick.getRawButton(2)) {
            shooter.turnOn();
            shooter.autoExtend(500, 2000);
            //intake.turnOnBottom();
        } else {
            shooter.turnOff();

        }

        /*
         if(driverJoystick.getRawButton(4)) {
         shooter.extend();
         } else {
         shooter.retract();
         }*/
         

        // turninSg scale
        if (driverJoystick.getRawButton(6)) {
            turningScale = reallySlowTurning;
        } else {
            turningScale = normalTurning;
        }

        //		if(!runningAuto) {
        //			TimerTask t = new StopMovementTask();
        //			if(driverJoystick.getRawButton(3)) {
        //				left.set(1);
        //				right.set(-1);
        //				timer.schedule(t, 1000);
        //				runningAuto = true;
        //			} else if(driverJoystick.getRawButton(4)) {
        //				left.set(1);
        //				right.set(-1);
        //				timer.schedule(t,2000);
        //				runningAuto = true;
        //			} else if(driverJoystick.getRawButton(5)) {
        //				left.set(1);
        //				right.set(-1);
        //				timer.schedule(t, 3000);
        //				runningAuto = true;
        //			} else if(driverJoystick.getRawButton(1)) {
        //				left.set(1);
        //				right.set(-1);
        //				timer.schedule(t, 4000);
        //				runningAuto = true;
        //			} else if(driverJoystick.getRawButton(6)) {
        //				left.set(1);
        //				right.set(-1);
        //				timer.schedule(t, 5000);
        //				runningAuto = true;
        //			}
        //		}


        //if (!runningAuto) {
        // drive train

        double[] coordinates = style.drive(driverJoystick.getRawButton(5), calculator);
        double x = coordinates[0];
        double y = coordinates[1];
        //if (left.isLowSpeed() && right.isLowSpeed()) {
        if (Math.abs(x) < Math.abs(0.6 * y) && Math.abs(x) < 0.1) {
            x = 0;
        } else if (Math.abs(y) < Math.abs(0.6 * x)) {
            y = 0;
        }
        /*} else {
         if (Math.abs(x) < Math.abs(y)) {
         x = 0;
         } else if (Math.abs(x) > Math.abs(y)) {
         y = 0;
         } else {
         x = 0;
         }
         }*/
        x *= turningScale;
        left.set(-y + x);
        right.set(y + x);
        //}

        // hang
                /*if (driverJoystick.getRawButton(8)) {
         left.set(1);
         right.set(1);
         }*/

        //System.out.println(debugString());
    }

    public String debugString() {
        return "[left]:" + left.get() + "[right]:" + right.get() + "[shooter]:" + shooter.getSpeed();
    }
}
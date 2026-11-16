package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Robot-oriented mecanum drive.
 *
 * Left stick  : forward / back (Y) and strafe left / right (X)
 * Right stick : turn (X)
 *
 * Hardware configuration names (must match the Robot Controller config exactly):
 *   LeftFront  -> Control Hub   motor port 0
 *   LeftBack   -> Control Hub   motor port 1
 *   RightFront -> Expansion Hub motor port 0
 *   RightBack  -> Expansion Hub motor port 1
 */
@TeleOp(name = "teleopLunix", group = "Drive")
public class teleopLunix extends LinearOpMode {

    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;

    @Override
    public void runOpMode() {
        frontLeftMotor  = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backLeftMotor   = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backRightMotor  = hardwareMap.get(DcMotor.class, "backRightMotor");

        // Same directions as your Pedro MecanumConfig: left side reversed, right side forward.
        // If a wheel spins the wrong way when you push the stick forward, flip that one motor.
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Brake when the sticks are released (matches manualBrakeMode = true in Constants.java).
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double drive  = -gamepad1.left_stick_y;  // push up = forward
            double strafe =  gamepad1.left_stick_x;  // push right = strafe right
            double turn   =  gamepad1.right_stick_x; // push right = turn right

            // Scale so no motor is asked for more than full power, keeping the direction of motion.
            double scale = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 1.0);

            double lf = (drive + strafe + turn) / scale;
            double lb = ((drive - strafe + turn) / scale);
            double rf = ((drive - strafe - turn) / scale);
            double rb = (drive + strafe - turn) / scale;
            double speed = 0.6;

            frontLeftMotor.setPower(lf * speed);
            backLeftMotor.setPower(lb  * speed);
            frontRightMotor.setPower(rf  * speed);
            backRightMotor.setPower(rb  * speed);

            telemetry.addData("Drive / Strafe / Turn", "%.2f / %.2f / %.2f", drive, strafe, turn);
            telemetry.addData("LF / LB", "%.2f / %.2f", lf, lb);
            telemetry.addData("RF / RB", "%.2f / %.2f", rf, rb);
            telemetry.update();
        }
    }
}
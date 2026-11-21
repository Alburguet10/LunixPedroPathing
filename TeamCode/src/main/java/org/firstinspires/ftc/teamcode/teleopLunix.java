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
 * Extra motors (gamepad1, PlayStation):
 *   Motor 1: Square = forward, Circle = backwards
 *   Motor 2: Triangle = forward, Cross = backwards
 *   Motor 3: R2 = forwards (analog)
 */
@TeleOp(name = "teleopLunix", group = "Drive")
public class teleopLunix extends LinearOpMode {

    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;

    // 3 extra motors (same model as the drive motors).
    private DcMotor shooter;
    private DcMotor indexer;
    private DcMotor intake;

    @Override
    public void runOpMode() {
        // Corrected order: each variable reads the config name that actually
        // reaches the physical motor it is named for (see header comment).
        frontLeftMotor  = hardwareMap.get(DcMotor.class, "backRightMotor");
        backLeftMotor   = hardwareMap.get(DcMotor.class, "frontRightMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backRightMotor  = hardwareMap.get(DcMotor.class, "backLeftMotor");

        // Directions belong to the physical motors: left FORWARD, right REVERSE.
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Extra motors — change the names to match your Robot Controller config.
        intake = hardwareMap.get(DcMotor.class, "shooter");
        indexer = hardwareMap.get(DcMotor.class, "indexer");
        shooter = hardwareMap.get(DcMotor.class, "intake");

        // If a motor spins the wrong way, flip that motor's direction here.
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
        indexer.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        indexer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double drive  = -gamepad1.left_stick_y;  // push up = forward (kept as in your local version)
            double strafe = -gamepad1.left_stick_x;  // FLIPPED: rollers are mirrored, so push left = strafe left
            double turn   =  gamepad1.right_stick_x; // push right = turn right

            strafe *= 0.5; // strafe at half speed only — slow strafe tracks correctly; drive and turn stay full speed

            double scale = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 1.0);

            double lf = (drive + strafe + turn) / scale;
            double lb = (drive - strafe + turn) / scale;
            double rf = (drive - strafe - turn) / scale;
            double rb = (drive + strafe - turn) / scale;

            // No speed cap on drive/turn — full stick is 1.0 (strafe is halved above).
            frontLeftMotor.setPower(lf);
            backLeftMotor.setPower(lb);
            frontRightMotor.setPower(rf);
            backRightMotor.setPower(rb);

            // Extra motors — PlayStation controller (gamepad1).
            // Motor 1: Square = forward, Circle = backwards.
            double shooterPower = 0;
            if (gamepad1.x) {        // Square
                shooterPower = 1.0;
            } else if (gamepad1.b) { // Circle
                shooterPower = -1.0;
            }
            shooter.setPower(shooterPower);

            // Motor 2: Triangle = forward, Cross = backwards.
            double indexerPower = 0;
            if (gamepad1.y) {        // Triangle
                indexerPower = 1.0;
            } else if (gamepad1.a) { // Cross
                indexerPower = -1.0;
            }
            indexer.setPower(indexerPower);

            // Motor 3: R2 = forwards, speed follows how far you press the trigger.
            double intakePower = gamepad1.right_trigger;
            intake.setPower(intakePower);

            telemetry.addData("Drive / Strafe / Turn", "%.2f / %.2f / %.2f", drive, strafe, turn);
            telemetry.addData("LF / LB", "%.2f / %.2f", lf, lb);
            telemetry.addData("RF / RB", "%.2f / %.2f", rf, rb);
            telemetry.addData("Motors 1 / 2 / 3", "%.2f / %.2f / %.2f", shooterPower, indexerPower, intakePower);
            telemetry.update();
        }
    }
}
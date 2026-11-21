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
 *   Intake: R2 = forward, L2 = backwards (analog)
 *   Indexer: Triangle = forward, Cross = backwards
 *   Shooter: Square = forwards
 */
@TeleOp(name = "teleopLunix", group = "Drive")
public class teleopLunix extends LinearOpMode {

    // Drive motors — named after the config device they read (see header).
    private DcMotor backRightMotor;   // physical front-left wheel
    private DcMotor frontRightMotor;  // physical back-left wheel
    private DcMotor frontLeftMotor;   // physical front-right wheel
    private DcMotor backLeftMotor;    // physical back-right wheel

    // Extra motors — named after the mechanism they actually control.
    private DcMotor intake;
    private DcMotor indexer;
    private DcMotor shooter;

    @Override
    public void runOpMode() {
        backRightMotor  = hardwareMap.get(DcMotor.class, "backRightMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        frontLeftMotor  = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor   = hardwareMap.get(DcMotor.class, "backLeftMotor");

        // Directions belong to the physical motors: the physical left wheels
        // (backRightMotor, frontRightMotor) are FORWARD, the physical right
        // wheels (frontLeftMotor, backLeftMotor) are REVERSE.
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Extra motors — the config names for intake and shooter are swapped
        // (see header): intake reads "shooter", shooter reads "intake".
        shooter  = hardwareMap.get(DcMotor.class, "shooter");
        indexer = hardwareMap.get(DcMotor.class, "indexer");
        intake = hardwareMap.get(DcMotor.class, "intake");

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        indexer.setDirection(DcMotorSimple.Direction.FORWARD);
        // Shooter direction is INVERTED (REVERSE) — it spins the right way
        // for shooting only with the direction flipped.
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        indexer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
            // Variables are named after the config device; the comments say
            // which physical wheel each one is.
            backRightMotor.setPower(lf);   // physical front-left
            frontRightMotor.setPower(lb);  // physical back-left
            frontLeftMotor.setPower(rf);   // physical front-right
            backLeftMotor.setPower(rb);    // physical back-right

            // Extra motors — PlayStation controller (gamepad1).
            // Intake: R2 = forward, L2 = backwards (analog — speed follows
            // how far you press each trigger; pressing both cancels out).
            double intakePower = gamepad1.right_trigger - gamepad1.left_trigger;
            intake.setPower(intakePower);

            // Indexer: Triangle = forward, Cross = backwards.
            double indexerPower = 0;
            if (gamepad1.x) {        // Triangle
                indexerPower = 1.0;
            } else if (gamepad1.a) { // Cross
                indexerPower = -1.0;
            }
            indexer.setPower(indexerPower);

            // Shooter: Square = forwards.
            double shooterPower = 0;
            if (gamepad1.b) { // Square
                shooterPower = 1.0;
            }
            shooter.setPower(shooterPower);

            telemetry.addData("Drive / Strafe / Turn", "%.2f / %.2f / %.2f", drive, strafe, turn);
            telemetry.addData("LF / LB", "%.2f / %.2f", lf, lb);
            telemetry.addData("RF / RB", "%.2f / %.2f", rf, rb);
            telemetry.addData("Intake / Indexer / Shooter", "%.2f / %.2f / %.2f", intakePower, indexerPower, shooterPower);
            telemetry.update();
        }
    }
}
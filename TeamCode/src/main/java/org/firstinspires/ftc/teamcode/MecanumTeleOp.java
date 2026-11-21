package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class MecanumTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        // DRIVE MOTORS
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");

        // INTAKE AND INDEXER
        DcMotor intake = hardwareMap.dcMotor.get("intake");
        DcMotor indexer = hardwareMap.dcMotor.get("indexer");

        // SHOOTER
        DcMotor shooter = hardwareMap.dcMotor.get("shooter");

        // MOTOR DIRECTIONS
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            // MECANUM DRIVE
            double y = gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = -gamepad1.right_stick_x;

            double denominator = Math.max(
                    Math.abs(y) + Math.abs(x) + Math.abs(rx), 1
            );

            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            // INTAKE AND INDEXER
            double mechanismPower = 0;

            if (gamepad1.right_trigger > 0.1 &&
                    gamepad1.left_trigger < 0.1) {

                mechanismPower = gamepad1.right_trigger;

            } else if (gamepad1.left_trigger > 0.1 &&
                    gamepad1.right_trigger < 0.1) {

                mechanismPower = -gamepad1.left_trigger;
            }

            intake.setPower(mechanismPower);
            indexer.setPower(mechanismPower);

            // SHOOTER - BUTTON A
            if (gamepad1.a) {
                shooter.setPower(-1.0);
            } else {
                shooter.setPower(0);
            }
        }
    }
}
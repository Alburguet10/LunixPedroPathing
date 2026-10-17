package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(
            robotMecanum -> {
                robotMecanum.frontLeftName.set("llantaFi");
                robotMecanum.backLeftName.set("llantaTi");
                robotMecanum.frontRightName.set("llantaFd");
                robotMecanum.backRightName.set("llantaTd");

                robotMecanum.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
                robotMecanum.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
                robotMecanum.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
                robotMecanum.backRightDirection.set(DcMotorSimple.Direction.FORWARD);

                robotMecanum.manualBrakeMode.set(true);
            }
    );

    public static PinpointConfig localizerConfig = new PinpointConfig(
            robotOdometry -> {
                robotOdometry.name.set("pinpoint");
                robotOdometry.xPodOffset.set(2.187);
                robotOdometry.yPodOffset.set(-4.572);
                robotOdometry.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
                robotOdometry.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
            }
    );

    public static ForesightConfig foresightConfig = new ForesightConfig(
            robotControl -> {
                Controller primaryTranslationalForward = Controller.proportional(0.3);
                Controller secondaryTranslationalForward = Controller.proportional(0.1);
                Controller primaryTranslationalLateral = Controller.proportional(0.3);
                Controller secondaryTranslationalLateral = Controller.proportional(0.1);

                robotControl.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                robotControl.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                robotControl.coast.set(Controller.proportionalFeedforward(0.010978350889324107));
                robotControl.brake.set(Controller.proportionalFeedforward(0.008731598255925491));

                robotControl.headingFeedback.set(Controller.proportional(5.258721785960744));
                robotControl.headingBrakeCoefficients.set(Vector2D.cartesian(0.05642143125655298, 0.0063829525363003695));

                robotControl.linearBrakeCoefficients.set(Matrix.diag(0.10605894992901523, 0.08719146175596092));
                robotControl.quadraticBrakeCoefficients.set(Matrix.diag(0.0014663966976606565, 0.0013837064502458813));

                robotControl.maxAchievableForwardVelocity.set(72.72923108818539);
                robotControl.maxAchievableStrafeVelocity.set(52.34323936525474);
                robotControl.naturalForwardDeceleration.set(85.01144677379789);
                robotControl.naturalStrafeDeceleration.set(104.49787535782846);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}
// ATTEMTION!!! MY JAVA IS MESSED UP SO NO IMPORTS ARE IMPORTED :(

@TeleOp
public class PlutoTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException{
        //We get the motors here
        DcMotor frontLeftMotor = hardwareMap.DcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.DcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.DcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.DcMotor.get("backRightMotor");

        // Reverses the Right motors bc of some wierd gearbox stuff idk
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //we get IMU for field centric stuff
        IMU imu = hardwareMap.get(IMU.class, "imu");
        //orientation parameters for the imu
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.UP,
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        waitForStart();

        if (isStopRequested()) return;

        white(opModeIsActive()); { //idk why i need semicolon here
            double y = -gamepad1.left_stick_y; 
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Wierd ass math i looked in the documentation
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);
        }
    }


    
}

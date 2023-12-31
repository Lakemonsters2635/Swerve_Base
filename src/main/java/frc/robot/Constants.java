package frc.robot;

public final class Constants {
    // joystick channels
    public static final int RIGHT_JOYSTICK_CHANNEL = 1;
    public static final int LEFT_JOYSTICK_CHANNEL = 0;

    // Fix joystick speed

    public static final double FIX_JOYSTICK_SPEED = 1;

    public static final double INCHES_PER_METER = 39.37;
    public static final double LENGTH_OF_BOT = 35 / INCHES_PER_METER;
    public static final double FIELD_OFFSET_FROM_NODE_TO_APRILTAG = 0.36;
    public static final double FIELD_OFFSET_FROM_SUBSTATION_TO_APRILTAG = -15 / INCHES_PER_METER;
    public static final double MID_SCORING_STANDOFF_DISTANCE = (25 - 4) / INCHES_PER_METER;
    public static final double BUMPER_THICKNESS = 3 / INCHES_PER_METER;

    // FRONT LEFT
    public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR = 1; 
    public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_ENCODER = 0; 
    public static final int DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR = 2; 
    public static final double FRONT_LEFT_ANGLE_OFFSET_COMPETITION = Math.toRadians(6.46); //3.01

    // FRONT RIGHT
    public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR = 3; 
    public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_ENCODER = 1;
    public static final int DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR = 4;
    public static final double FRONT_RIGHT_ANGLE_OFFSET_COMPETITION = Math.toRadians(12.67); // 3.0775

    // BACK LEFT
    public static final int DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR = 5; 
    public static final int DRIVETRAIN_BACK_LEFT_ANGLE_ENCODER = 2;
    public static final int DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR = 9; 
    public static final double BACK_LEFT_ANGLE_OFFSET_COMPETITION = Math.toRadians(118.06); //2.9835

    // BACK RIGHT
    public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR = 7;
    public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_ENCODER = 3;
    public static final int DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR = 8; 
    public static final double BACK_RIGHT_ANGLE_OFFSET_COMPETITION = Math.toRadians(-23.57); //3.0346

    // hat constants 
    public static final int HAT_JOYSTICK_TRIM_POSITION = RIGHT_JOYSTICK_CHANNEL;
    public static final int HAT_JOYSTICK_TRIM_ROTATION_ARM = LEFT_JOYSTICK_CHANNEL;
    public static final double HAT_POWER_MOVE = 0.2;
    public static final double HAT_POWER_ROTATE = 0.2;
    // Hat trim target speed is 15 degrees per second
    // One time step is 0.02 seconds
    // 0.3 degrees per time step is our target change when the hat is active
    public static final double HAT_POSE_TARGET_PER_TIME_STEP = -0.3; // negative is raising the arm
    public static final int HAT_POV_MOVE_LEFT = 270;
    public static final int HAT_POV_MOVE_RIGHT = 90;
    public static final int HAT_POV_MOVE_FORWARD = 0;
    public static final int HAT_POV_MOVE_BACK = 180;
    public static final int HAT_POV_ARM_UP = 0;
    public static final int HAT_POV_ARM_DOWN = 180;
    public static final int HAT_POV_ROTATE_LEFT = 270;
    public static final int HAT_POV_ROTATE_RIGHT = 90;

    public static final double kMaxModuleAngularSpeedRadiansPerSecond = 2 * Math.PI;
    public static final double kMaxModuleAngularAccelerationRadiansPerSecondSquared = 2 * Math.PI;

    public static final int kEncoderCPR = 42; // neo encoder ticks per revolution
    public static final double kWheelDiameterMeters = 4.11 / 39.37;
    public static final double kDriveEncoderDistancePerPulse =
        // Assumes the encoders are directly mounted on the wheel shafts
        (kWheelDiameterMeters * Math.PI) * (1.0 / (60.0 / 15.0) / (20.0 / 24.0) / (40.0 / 16.0));

    // put into manual mode, manually read position and rotate wheel

    public static final double kTurningEncoderDistancePerPulse =
        // Assumes the encoders are on a 1:1 reduction with the module shaft.
        (2 * Math.PI) / (double) kEncoderCPR;

    public static final double kPModuleTurningController = 0.5;

    public static final double kPModuleDriveController = 0;

    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;

    // VISION CONSTANTS
    public static double OBJECT_DETECTION_LATENCY = 0.217; // seconds

    // how far away you quit driving w/ vision
    // TODO: figure out these distances
    public static final int TARGET_TRIGGER_DISTANCE_CONE = 10;       // cone is skinny
    public static final int TARGET_TRIGGER_DISTANCE_CUBE = 10;       // cube is not as skinny
    public static final int TARGET_TRIGGER_DISTANCE_ANY = 10;        // cube is not as skinny
    public static final int TARGET_TRIGGER_DISTANCE_APRIL_TAG = 0;   // april tag approach distance for score.  Can't see tag if closer
}
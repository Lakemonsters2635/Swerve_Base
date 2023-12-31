// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner14;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class DrivetrainSubsystem extends SubsystemBase {
  private Joystick hatJoystickTrimPosition;
  private Joystick hatJoystickTrimRotationArm;
    public static final double kMaxSpeed = 3.63; // 3.63 meters per second
    public final double kMaxAngularSpeed = Math.PI; // 1/2 rotation per second
  
    public final double m_drivetrainWheelbaseWidth = 18.5 / Constants.INCHES_PER_METER;
    public final double m_drivetrainWheelbaseLength = 28.5 / Constants.INCHES_PER_METER;

    // x is forward       robot is long in the x-direction, i.e. wheelbase length
    // y is to the left   robot is short in the y-direction, i.e. wheelbase width
    // robot front as currently labled on the motors (requires -x trajectory to go out into the +x field direction)
    public final Translation2d m_frontLeftLocation = 
            new Translation2d(m_drivetrainWheelbaseLength/2, m_drivetrainWheelbaseWidth/2);
    public final Translation2d m_frontRightLocation = 
            new Translation2d(m_drivetrainWheelbaseLength/2, -m_drivetrainWheelbaseWidth/2);
    public final Translation2d m_backLeftLocation = 
            new Translation2d(-m_drivetrainWheelbaseLength/2, m_drivetrainWheelbaseWidth/2);
    public final Translation2d m_backRightLocation = 
            new Translation2d(-m_drivetrainWheelbaseLength/2, -m_drivetrainWheelbaseWidth/2);

    public final SwerveModule m_frontLeft = new SwerveModule(Constants.DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR, 
                                                              Constants.DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR, 
                                                              Constants.DRIVETRAIN_FRONT_LEFT_ANGLE_ENCODER, 
                                                              Constants.FRONT_LEFT_ANGLE_OFFSET_COMPETITION,
                                                              1.0);
    public final SwerveModule m_frontRight = new SwerveModule(Constants.DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR, 
                                                              Constants.DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR, 
                                                              Constants.DRIVETRAIN_FRONT_RIGHT_ANGLE_ENCODER, 
                                                              Constants.FRONT_RIGHT_ANGLE_OFFSET_COMPETITION,
                                                              1.0);
    public final SwerveModule m_backLeft = new SwerveModule(Constants.DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR, 
                                                              Constants.DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR, 
                                                              Constants.DRIVETRAIN_BACK_LEFT_ANGLE_ENCODER, 
                                                              Constants.BACK_LEFT_ANGLE_OFFSET_COMPETITION,
                                                              1.0);
    public final SwerveModule m_backRight = new SwerveModule(Constants.DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR, 
                                                              Constants.DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR, 
                                                              Constants.DRIVETRAIN_BACK_RIGHT_ANGLE_ENCODER, 
                                                              Constants.BACK_RIGHT_ANGLE_OFFSET_COMPETITION,
                                                              1.0);
  
    public final AHRS m_gyro = new AHRS(SPI.Port.kMXP, (byte) 200);
  
    private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
      m_frontLeftLocation,
      m_frontRightLocation, 
      m_backLeftLocation, 
      m_backRightLocation);
  
    public final SwerveDriveOdometry m_odometry =
        new SwerveDriveOdometry(
            m_kinematics,
            m_gyro.getRotation2d(),
            new SwerveModulePosition[] {
              m_frontLeft.getPosition(),
              m_frontRight.getPosition(),
              m_backLeft.getPosition(),
              m_backRight.getPosition()
            });

  /** Creates a new DrivetrianSubsystem. */
  public DrivetrainSubsystem() {
    m_gyro.calibrate();
    getPose();
  }

  private static double xPowerCommanded = 0;
  private static double yPowerCommanded = 0;
  private static double rotCommanded = 0;

  // three setters here and then call the setteres from the sd execute
  public static void setXPowerCommanded(double xPower) {
    xPowerCommanded = xPower;
  }

  public static void setYPowerCommanded(double yPower) {
    yPowerCommanded = yPower;
  }

  public static void setRotCommanded(double rot) {
    rotCommanded = rot;
  }

  @Override
  public void periodic() {
      //Hat Power Overides for Trimming Position and Rotation
      hatJoystickTrimPosition = RobotContainer.rightJoystick;
      hatJoystickTrimRotationArm = RobotContainer.leftJoystick;
      if(hatJoystickTrimPosition.getPOV()==Constants.HAT_POV_MOVE_FORWARD ){
        yPowerCommanded = Constants.HAT_POWER_MOVE;
      }
      else if(hatJoystickTrimPosition.getPOV()==Constants.HAT_POV_MOVE_BACK){
        yPowerCommanded = Constants.HAT_POWER_MOVE*-1.0;
      }
      else if(hatJoystickTrimPosition.getPOV()==Constants.HAT_POV_MOVE_RIGHT){
        xPowerCommanded = Constants.HAT_POWER_MOVE;
      }
      else if(hatJoystickTrimPosition.getPOV()==Constants.HAT_POV_MOVE_LEFT){
        xPowerCommanded = Constants.HAT_POWER_MOVE*-1.0;
      }
      // else{
      //   yPowerCommanded =0;
      //   xPowerCommanded =0;
      // }

      if(hatJoystickTrimRotationArm.getPOV()==Constants.HAT_POV_ROTATE_RIGHT){
        rotCommanded = Constants.HAT_POWER_ROTATE*-1.0;
      }
      else if(hatJoystickTrimRotationArm.getPOV()==Constants.HAT_POV_ROTATE_LEFT){
        rotCommanded = Constants.HAT_POWER_ROTATE;
      }
      // else{
      //   rotCommanded = 0;
      // }



      if(hatJoystickTrimPosition.getPOV()==-1){
        yPowerCommanded= 0;
        xPowerCommanded= 0;
      }

      if(hatJoystickTrimRotationArm.getPOV()==-1){
        rotCommanded = 0;
      }

      if (hatJoystickTrimPosition.getY()>0.05 || hatJoystickTrimPosition.getY()<-0.05) {
        yPowerCommanded = hatJoystickTrimPosition.getY() * Constants.FIX_JOYSTICK_SPEED *-1;
      }

      if (hatJoystickTrimPosition.getX()>0.05 || hatJoystickTrimPosition.getX()<-0.05) {
        xPowerCommanded = hatJoystickTrimPosition.getX() * Constants.FIX_JOYSTICK_SPEED;
      }

      if (hatJoystickTrimPosition.getTwist()>0.05 || hatJoystickTrimPosition.getTwist()<-0.05) {
        rotCommanded = hatJoystickTrimPosition.getTwist() * Constants.FIX_JOYSTICK_SPEED*-1;
      }
      


      System.out.println(Double.toString(rotCommanded) + "\t" +Double.toString(yPowerCommanded)+ "\t" +Double.toString(xPowerCommanded));

      
      this.drive(xPowerCommanded * DrivetrainSubsystem.kMaxSpeed, 
                 yPowerCommanded * DrivetrainSubsystem.kMaxSpeed,
                 MathUtil.applyDeadband(-rotCommanded * this.kMaxAngularSpeed, 0.2), 
                 true);
    
    updateOdometry();

    putDTSToSmartDashboard();
    // tuneAngleOffsetPutToDTS();
  }

  public void recalibrateGyro() {
    System.out.println(m_gyro.getRotation2d());
    m_gyro.calibrate();
    System.out.println(m_gyro.getRotation2d());
  }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed Speed of the robot in the x direction (forward).   -1.0 ... +1.0
   * @param ySpeed Speed of the robot in the y direction (sideways).  -1.0 ... +1.0
   * @param rot Angular rate of the robot.                            -1.0 ... +1.0
   * @param fieldRelative Whether the provided x and y speeds are relative to the field.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
    var swerveModuleStates =
        m_kinematics.toSwerveModuleStates(
            fieldRelative
                ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, m_gyro.getRotation2d())
                : new ChassisSpeeds(xSpeed, ySpeed, rot));
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, kMaxSpeed);
    m_frontLeft.setDesiredState(swerveModuleStates[0]);
    m_frontRight.setDesiredState(swerveModuleStates[1]);
    m_backLeft.setDesiredState(swerveModuleStates[2]);
    m_backRight.setDesiredState(swerveModuleStates[3]);
  }

  /** Updates the field relative position of the robot. */
  public void updateOdometry() {
    m_odometry.update(
        m_gyro.getRotation2d(),
        new SwerveModulePosition[] {
          m_frontLeft.getPosition(),
          m_frontRight.getPosition(),
          m_backLeft.getPosition(),
          m_backRight.getPosition()
        });
  }

  /** Get pose from odometry field **/
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public SwerveDriveKinematics getSwerveDriveKinematics() {
    return m_kinematics; 
  }

  /** zeroes drivetrain odometry **/
  public void zeroOdometry() {
    resetOdometry(new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * https://github.com/wpilibsuite/allwpilib/blob/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/swervecontrollercommand/subsystems/DriveSubsystem.java
   * 
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    m_odometry.resetPosition(
        m_gyro.getRotation2d(),
        new SwerveModulePosition[] {
          m_frontLeft.getPosition(),
          m_frontRight.getPosition(),
          m_backLeft.getPosition(),
          m_backRight.getPosition()
        },
        pose);
  }

  /** Sets the swerve ModuleStates.
   * @param cs The desired SwerveModule states as a ChassisSpeeds object
   */
  private void setDesiredStates(ChassisSpeeds cs) {
    SwerveModuleState[] desiredStates = m_kinematics.toSwerveModuleStates(cs);

    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, 4);

    m_frontLeft.setDesiredState(desiredStates[0]);
    m_frontRight.setDesiredState(desiredStates[1]);
    m_backLeft.setDesiredState(desiredStates[2]);
    m_backRight.setDesiredState(desiredStates[3]);
  } 

  /** Sets the swerve ModuleStates. Accept a center of rotation for when you DON'T want to rotate
   * around the center of the robot
   * @param cs The desired SwerveModule states as a ChassisSpeeds object
   * @param centerOfRotation Center of rotation. Ex. location of camera
   */
  private void setDesiredStates(ChassisSpeeds cs, Translation2d centerOfRotation) {
    // System.out.println("vX: " + Math.round(cs.vxMetersPerSecond*100.0)/100.0 + "  vY: " + Math.round(cs.vyMetersPerSecond));
    SwerveModuleState[] desiredStates = m_kinematics.toSwerveModuleStates(cs, centerOfRotation);

    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, 4);

    m_frontLeft.setDesiredState(desiredStates[0]);
    m_frontRight.setDesiredState(desiredStates[1]);
    m_backLeft.setDesiredState(desiredStates[2]);
    m_backRight.setDesiredState(desiredStates[3]);
  } 

public ChassisSpeeds getChassisSpeeds() {
    ChassisSpeeds chassisSpeeds = m_kinematics.toChassisSpeeds(m_frontLeft.getState(), m_frontRight.getState(), m_backLeft.getState(), m_backRight.getState());
    return chassisSpeeds;
  }

  public AHRS getGyroscope() {
    return m_gyro; 
  }

  /**Sets the swerve ModuleStates.
   * @param desiredStates The desired SwerveModule states. Array of `SwerveModuleState[]`
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, DrivetrainSubsystem.kMaxSpeed);
    m_frontLeft.setDesiredState(desiredStates[0]);
    m_frontRight.setDesiredState(desiredStates[1]);
    m_backLeft.setDesiredState(desiredStates[2]);
    m_backRight.setDesiredState(desiredStates[3]);
  }
  
  /** Displays all 4 module positions + robot pose (forward/back) in SmartDashboard. 
   * </p> For debugging
   */
  public void putDTSToSmartDashboard() {}

  /**
   * Procedure for tuning:
   * </p>   1. Put tuneAngleOffsetPutToDTS() in periodic(). 
   * </p>   2. Read the angles when the wheels are lined up. 
   * </p>   3. Add/subtract degrees from the angle offsets in Constants until they all read 0/pi/2pi when perfectly lined up
   */
  public void tuneAngleOffsetPutToDTS() {
    // TUNE ANGLE OFFSETS
    SmartDashboard.putNumber("FL encoder pos", Math.toDegrees(m_frontLeft.getTurningEncoderRadians()));
    SmartDashboard.putNumber("FR encoder pos", Math.toDegrees(m_frontRight.getTurningEncoderRadians()));
    SmartDashboard.putNumber("BL encoder pos", Math.toDegrees(m_backLeft.getTurningEncoderRadians()));
    SmartDashboard.putNumber("BR encoder pos", Math.toDegrees(m_backRight.getTurningEncoderRadians())); 
  }
}
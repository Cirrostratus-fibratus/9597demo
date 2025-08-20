// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.drive;
import frc.robot.subsystems.CANdleSystem;

import frc.robot.subsystems.CANdleSystem.*;

import com.ctre.phoenix6.hardware.CANcoder;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  //joysick
  private final CommandXboxController m_driverController = new CommandXboxController(0);//two xbox controller
  
  //subsystems
  public final drive m_drive_subsystem = new drive();

  public final CANdleSystem m_candle_system = new CANdleSystem();

  public final drive m_test_motor2 = new drive();


  //构造函数
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }


  private void configureBindings() {
    // //电机到位50
    // m_driverJoystick.leftBumper().onTrue(motor.Motor_Move_Position1());

    // //电机到位100
    // m_driverJoystick.rightBumper().onTrue(motor.Motor_Move_Position2());

    // //释放电机
    // m_driverJoystick.start().onTrue(Commands.runOnce(()->motor.releaseMotor()));

    m_driverController.b()
        .onTrue(m_drive_subsystem.cmd_motor_SetPosition_velocity(10,50)
        .andThen(m_drive_subsystem.teskStop())
        .andThen( m_candle_system.setColorFlowWithMotor()));
      
    m_driverController.a()
        .onTrue(m_drive_subsystem.cmd_motor_SetPosition_velocity(-10,0)
        .andThen(m_drive_subsystem.teskStop())
        .andThen( m_candle_system.setFireWithMotor()));
      }

}

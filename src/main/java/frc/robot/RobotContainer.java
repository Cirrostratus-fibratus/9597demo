// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
//包：功能包，负责某一类特定的功能

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.CANdleSystem;
// import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.Drive;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.FireAnimation;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger; //从其他文件夹里import



//从其他程序import进来

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drive m_DriveSubsystem = new Drive(); //实例化：要用另外一个类里的方法，实例化就是告诉程序去哪个类里找

  private final int LedCount = 300;
   private final CANdleSystem m_candle = new CANdleSystem();

   private Animation m_toAnimate = null;

   public enum AnimationTypes{
    Fire,
 }

 private AnimationTypes m_currentAnimation;
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  public void setFire() {
        m_toAnimate = new FireAnimation(0.5, 0.7, LedCount, 0.7, 0.5);
    }


  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

    //某个按键.ontrue(这里面填你要执行的命令):实现了按键和命令的绑定
    //ontrue：按下就触发一次
    //whileTrue：一直按着就一直出发

    // new Trigger(m_exampleSubsystem::exampleCondition) //Trigger对象 这是官方写的语法 //"::"调用对象不执行 
    //     .onTrue(new ExampleCommand(m_exampleSubsystem));//摁下就是true 然后执行 你一直摁着也只执行一次

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.

    //实际在程序里要写就用这个写法
    m_driverController.b() //Trigger对象
    .whileTrue(m_DriveSubsystem.Motor_Position_Command(2)); //摁下的时候
                                //Motor_Position_Command
    
    m_driverController.a() //Trigger对象
    .onTrue(m_DriveSubsystem.Motor_Position_Command2(10.1 , 10)
    .andThen(m_candle.set_Fire()));
    
    m_driverController.x() //Trigger对象
    .onTrue(m_DriveSubsystem.Motor_Position_Command2(0, -10)
    .andThen(m_candle.set_Larson()));
  }

  // /**
  //  * Use this to pass the autonomous command to the main {@link Robot} class.
  //  *
  //  * @return the command to run in autonomous
  //  */
  // public Command getAutonomousCommand() {
  //   // An example command will be run in autonomous
  //   return Autos.exampleAuto(m_exampleSubsystem);
  // }
}

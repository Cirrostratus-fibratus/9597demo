// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class drive extends SubsystemBase {

  private final TalonFX m_test_motor = new TalonFX(11, "rio");
  private final VelocityTorqueCurrentFOC m_test_motor_request = new VelocityTorqueCurrentFOC(0.0);
  private final TalonFX m_test_motor_2 = new TalonFX(11, "rio");
  private final VelocityTorqueCurrentFOC m_test_motor_request_2 = new VelocityTorqueCurrentFOC(0.0);
  private final TalonFX m_test_motor_3 = new TalonFX(11, "rio");
  private final VelocityTorqueCurrentFOC m_test_motor_request_3 = new VelocityTorqueCurrentFOC(0.0);
  private final TalonFX m_test_motor_4 = new TalonFX(11, "rio");
  private final VelocityTorqueCurrentFOC m_test_motor_request_4 = new VelocityTorqueCurrentFOC(0.0);

  public void setmotorVelocity(double vel) {
    m_test_motor.setControl(m_test_motor_request.withVelocity(vel));
    //m_test_motor_2.setControl(m_test_motor_request_2.withPosition(pos));

  }
  public void setmotorVelocity_2(double vel) {
    m_test_motor.setControl(m_test_motor_request_3.withVelocity(vel));
    //m_test_motor_4.setControl(m_test_motor_request_4.withPosition(pos));
    
  }
  //控制
  public Command mortor_Velocity_command(double vel) {
    return runOnce(() -> {
      setmotorVelocity(vel);
    });
  }
  public Command mortor_Velocity_command_2(double pos) {
    return runEnd(
      () -> {
        setmotorVelocity_2(pos);
      },
      () -> {
        setmotorVelocity_2(0);
      }
    );
  }
  
  /** Creates a new ExampleSubsystem. */
  public drive() {
    var motorConfigs = new TalonFXConfiguration();

     motorConfigs.Slot0.kS = 1.5;
    motorConfigs.Slot0.kV = 0.0;
    motorConfigs.Slot0.kA = 0;
    motorConfigs.Slot0.kP = 7;
    motorConfigs.Slot0.kI = 0;
    motorConfigs.Slot0.kD = 0.1;

    motorConfigs.MotionMagic.MotionMagicAcceleration = 100; // Acceleration is around 40 rps/s
    motorConfigs.MotionMagic.MotionMagicCruiseVelocity = 200; // Unlimited cruise velocity
    motorConfigs.MotionMagic.MotionMagicExpo_kV = 0.12; // kV is around 0.12 V/rps
    motorConfigs.MotionMagic.MotionMagicExpo_kA = 0.1; // Use a slower kA of 0.1 V/(rps/s)
    motorConfigs.MotionMagic.MotionMagicJerk = 0; // Jerk is around 0

    m_test_motor.getConfigurator().apply(motorConfigs);
    m_test_motor_2.getConfigurator().apply(motorConfigs);
    m_test_motor_3.getConfigurator().apply(motorConfigs);
    m_test_motor_4.getConfigurator().apply(motorConfigs);
  }

}
/*Set all gains to zero.调零

Determine Kg if using an elevator or arm.增加Kg直到电梯不往下掉

Select the appropriate Static Feedforward Sign for your closed-loop type.速度控制用velocity，位置控制用闭环

Increase Ks until just before the motor moves.Ks在动之前越大越好

If using velocity setpoints, increase Kv until the output velocity closely matches the velocity setpoints.5.如果你用速度控制，且需要设定特殊->增加Kv直到速度达到预期

Increase Kp until the output starts to oscillate around the setpoint.增加Kp直到他开始在目标位置晃悠

Increase Kd as much as possible without introducing jittering to the response.加Kd直到真的开始震
Kp决定了电机劲大小=）
 */

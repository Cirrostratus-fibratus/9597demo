// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
//import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class drive extends SubsystemBase {

  private final TalonFX m_test_motor = new TalonFX(2, "rio");
  private final VoltageOut m_test_motor_request = new VoltageOut(0.0);
  private final TalonFX m_test_motor_2 = new TalonFX(1, "rio");
  private final VoltageOut m_test_motor_request_2 = new VoltageOut(0.0);
  private final TalonFX m_test_motor_3 = new TalonFX(3, "rio");
  private final VoltageOut m_test_motor_request_3 = new VoltageOut(0.0);
  private final TalonFX m_test_motor_4 = new TalonFX(4, "rio");
  private final VoltageOut m_test_motor_request_4 = new VoltageOut(0.0);
  public void setmotorVoltage(double vol) {
    m_test_motor.setControl(m_test_motor_request.withOutput(vol));
    m_test_motor_2.setControl(m_test_motor_request_2.withOutput(vol));

  }
  public void setmotorVoltage_2(double vol) {
    m_test_motor_3.setControl(m_test_motor_request_3.withOutput(vol));
    m_test_motor_4.setControl(m_test_motor_request_4.withOutput(vol));
    
  }
  //控制电压
  public Command mortor_Voltage_command(double voltage) {
    return runOnce(() -> {
      setmotorVoltage(voltage);
    });
  }
  public Command mortor_Voltage_command_2(double voltage) {
    return runEnd(
      () -> {
        setmotorVoltage_2(voltage);
      },
      () -> {
        setmotorVoltage_2(0);
      }
    );
  }
  
  /** Creates a new ExampleSubsystem. */
  public drive() {
    var motorConfigs = new TalonFXConfiguration();

     motorConfigs.Slot0.kS = 0.2;
    motorConfigs.Slot0.kV = 0.0;
    motorConfigs.Slot0.kA = 0;
    motorConfigs.Slot0.kP = 3;
    motorConfigs.Slot0.kI = 0;
    motorConfigs.Slot0.kD = 0;
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
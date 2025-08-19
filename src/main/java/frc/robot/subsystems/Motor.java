// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Motor extends SubsystemBase {

  //step1
  private final TalonFX m_test_motor = new TalonFX(1, "rio");
  private final MotionMagicVoltage m_test_motor_request = new MotionMagicVoltage(0.0).withSlot(0);

  private final VoltageOut m_test_motor_voltage_request = new VoltageOut(0.0);

  private final CANcoder m_motor_encoder = new CANcoder(4, "rio");

  //setp2
  public void setmotorPosition(double position) {
    m_test_motor.setControl(m_test_motor_request.withPosition(position));
  }

  public void releaseMotor() {
    m_test_motor.setControl(m_test_motor_voltage_request.withOutput(0.0));
  }

  //构造函数
  public Motor() {

      //step3
      //motor configs
      var motorEncoderConfigs = new CANcoderConfiguration();
      motorEncoderConfigs.MagnetSensor.MagnetOffset=0.0;//offset
      motorEncoderConfigs.MagnetSensor.AbsoluteSensorDiscontinuityPoint=0.5;
      motorEncoderConfigs.MagnetSensor.SensorDirection=SensorDirectionValue.Clockwise_Positive;

      m_motor_encoder.getConfigurator().apply(motorEncoderConfigs);

      // motorEncoder configs
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

      //set motor feedback using CANcoder data
      motorConfigs.Feedback.FeedbackRemoteSensorID = m_motor_encoder.getDeviceID();
      motorConfigs.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;
      motorConfigs.Feedback.RotorToSensorRatio = 13;

      m_test_motor.getConfigurator().apply(motorConfigs);


  }

  public Command Motor_Move_Position1(){
    return run(()->{
      setmotorPosition(50); // Set the motor to move at 1000 units per second
    });
  }


  public Command Motor_Move_Position2(){
    return run(()->{
      setmotorPosition(100); // Set the motor to move at 1000 units per second
    });
  }


}

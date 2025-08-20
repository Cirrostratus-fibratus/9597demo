// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.hardware.CANcoder;

public class drive extends SubsystemBase {
  private final CANcoder cancoder_fl= new CANcoder(5,"rio");
  private final TalonFX m_test_motor = new TalonFX(1, "rio");
  private final PositionTorqueCurrentFOC m_test_motor_request = new PositionTorqueCurrentFOC(0.0);
  private final TalonFX m_test_motor_2 = new TalonFX(1, "rio");
  private final PositionTorqueCurrentFOC m_test_motor_request_2 = new PositionTorqueCurrentFOC(0.0);
  private final TalonFX m_test_motor_3 = new TalonFX(11, "rio");
  private final PositionTorqueCurrentFOC m_test_motor_request_3 = new PositionTorqueCurrentFOC(0.0);
  private final TalonFX m_test_motor_4 = new TalonFX(11, "rio");
  private final PositionTorqueCurrentFOC m_test_motor_request_4 = new PositionTorqueCurrentFOC(0.0);
  double expected_position = 50;
  double current_position=0;
  double error= 1.0;
  public void setmotorPosition_2(double pos) {
    m_test_motor.setControl(m_test_motor_request_3.withPosition(pos));
    m_test_motor_4.setControl(m_test_motor_request_4.withPosition(pos));
    
  }
  //控制
  
  /*public Command mortor_Position_command_2(double pos) {
    return runEnd(
      () -> {
        setmotorPosition_2(pos);
      },
      () -> {
        setmotorPosition_2(0);
      }
    );
  }*/
  public Boolean isAtPostion(){
    current_position=m_test_motor.getPosition().getValueAsDouble();
    return (Math.abs(expected_position-current_position)<=error);
  }
  public Command mortor_Position_command_2(double pos) {
    return run(
      () -> {
        setmotorPosition_2(pos);
      }).until(()->isAtPostion());
  }

  /** Creates a new ExampleSubsystem. */
  public drive() {
        //电机参数配置
    var motorConfigs = new TalonFXConfiguration();
    motorConfigs.Slot0.kS = 0.14;
    motorConfigs.Slot0.kV = 0.0;
    motorConfigs.Slot0.kA = 0;
    motorConfigs.Slot0.kP = 3;
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

    //与电机产生联系
    motorConfigs.Feedback.FeedbackRemoteSensorID=cancoder_fl.getDeviceID();
    motorConfigs.Feedback.FeedbackSensorSource=FeedbackSensorSourceValue.FusedCANcoder;
    motorConfigs.Feedback.RotorToSensorRatio = 13;//减速比（也许

//cancoder参数配置
    var motorEncoderConfigs= new CANcoderConfiguration();
    motorEncoderConfigs.MagnetSensor.MagnetOffset=0;//字面
    motorEncoderConfigs.MagnetSensor.AbsoluteSensorDiscontinuityPoint=0.5;//实际范围映射的范围
    motorEncoderConfigs.MagnetSensor.SensorDirection=SensorDirectionValue.Clockwise_Positive;
    cancoder_fl.getConfigurator().apply(motorEncoderConfigs);


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

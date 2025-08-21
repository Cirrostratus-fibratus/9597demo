// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class drive extends SubsystemBase {

  private final CANcoder  cancoder_fl = new CANcoder(Constants.MOTOR.CANCODER_ID,"rio");

  private final TalonFX m_test_motor = new TalonFX(Constants.MOTOR.MORTOR1_ID, "rio");
  private final TalonFX m_test_motor2 = new TalonFX(Constants.MOTOR.MORTOR2_ID, "rio");
  private final MotionMagicVoltage m_test_motor_request = new MotionMagicVoltage(0.0);
  private final VelocityTorqueCurrentFOC m_test_motor2_request = new VelocityTorqueCurrentFOC(0.0);

  // public void setmotorPosition(double vol) {
  //   m_test_motor.setControl(m_test_motor_request.withPosition(vol));

  // }

  double expected_position = 10;
  double current_position = 0;
  double error = 1.0;//设置三个变量用来判断电机有没有达到范围内

  public void setmotorPosition(double position) {
    m_test_motor.setControl(m_test_motor_request.withPosition(position));

  }//用位置控制电机

  public void setmotorVol(double Velocity){
    m_test_motor2.setControl(m_test_motor2_request.withVelocity(Velocity));
  }//用速度控制电机

  public Command motor_Position_command(double Position){

    return runOnce(()->{
                  setmotorPosition(Position);
                  });
  }
  //写出位置控制的电机的相应方法

  public drive() {
    //设置两个电机，声明他们

    //cancoder电机参数配置
    var motorEncoderConfigs = new CANcoderConfiguration();
    motorEncoderConfigs.MagnetSensor.MagnetOffset=0.0;//offset
    motorEncoderConfigs.MagnetSensor.AbsoluteSensorDiscontinuityPoint=0.5;//实际生活中的电机位什么范围
    motorEncoderConfigs.MagnetSensor.SensorDirection=SensorDirectionValue.Clockwise_Positive;
    cancoder_fl.getConfigurator().apply(motorEncoderConfigs);

    var motorConfigs = new TalonFXConfiguration();

    //位置控制电机1参数配置
     motorConfigs.Slot0.kS = 0.142;
    motorConfigs.Slot0.kV = 0.0;
    motorConfigs.Slot0.kA = 0;
    motorConfigs.Slot0.kP = 2;
    motorConfigs.Slot0.kD = 0;
    motorConfigs.MotionMagic.MotionMagicAcceleration = 100; // Acceleration is around 40 rps/s
    motorConfigs.MotionMagic.MotionMagicCruiseVelocity = 200; // Unlimited cruise velocity
    motorConfigs.MotionMagic.MotionMagicExpo_kV = 0.12; // kV is around 0.12 V/rps
    motorConfigs.MotionMagic.MotionMagicExpo_kA = 0.1; // Use a slower kA of 0.1 V/(rps/s)
    motorConfigs.MotionMagic.MotionMagicJerk = 0; // Jerk is around 0

    //feedback,建立电机和cancoder的联系
    motorConfigs.Feedback.FeedbackRemoteSensorID = cancoder_fl.getDeviceID();
    motorConfigs.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;
    motorConfigs.Feedback.RotorToSensorRatio = 13;

    m_test_motor.getConfigurator().apply(motorConfigs);

    //速度控制电机2参数配置
    var motorConfigs2 = new TalonFXConfiguration();
   motorConfigs2.Slot0.kS = 0.16;
   motorConfigs2.Slot0.kV = 0.0;
   motorConfigs2.Slot0.kA = 0;
   motorConfigs2.Slot0.kP = 2;
   motorConfigs2.Slot0.kI = 0;
   motorConfigs2.Slot0.kD = 0;

   m_test_motor2.getConfigurator().apply(motorConfigs2);

  }

  //返回电机1的当前位置
  public double getMotorPosition(){
     return m_test_motor.getPosition().getValueAsDouble();
  }
 
  //位置控制电机1
  public void m_setMotorPosition(double position){
    m_test_motor.setControl(m_test_motor_request.withPosition(position));
}

   //速度控制电机2
  public void m_setMotorVel(double Vel){
    m_test_motor2.setControl(m_test_motor2_request.withVelocity(Vel));
  }

  //判断电机是否达到位
  public Boolean isAtPosition(double expected_position){
    current_position = m_test_motor.getPosition().getValueAsDouble();
    return (Math.abs(expected_position-current_position)<=error);
  }

  //打包一起位置和速度控制的电机，这样他们能同时运行
   public Command  cmd_motor_SetPosition_velocity(double vel,double position){
      return run(
          ()->{
             m_setMotorPosition(position);
             m_setMotorVel(vel);
          })
          .until(()->isAtPosition(position))
          .finallyDo(()->{ //运行完一些列动作后，控制电机停止运行
               m_setMotorVel(0);
               m_setMotorPosition(getMotorPosition());
            });
    }
  }

  
//Manual tuning typically follows this process:

// Set all gains to zero.

// Determine kg if using an elevator or arm.
//克服重力的参数，kg从0开始逐渐增加，直到松手电梯能够大概稳定在当前位置，不会下坠

// Select the appropriate Static Feedforward Sign for your closed-loop type.
//如果是速度控制，就用velocitysign，位置控制就用closedloopsign

// Increase ks until just before the motor moves.
//逐步增加ks直到电机微微有反应，处在一种临界有反应要动但是没动的状态
//ks克服静摩擦力

// If using velocity setpoints, increase kv
//  until the output velocity closely matches the velocity setpoints.
//如果你用速度控制并且需要设定速度到某个值，可以逐步增加kv直到你的速度达到设定值
//kv是一个放大系数，当我的速度不够的时候，用这个来提高我的速度到预期值

// Increase kp until the output starts to oscillate around the setpoint.
//逐步增加kp直到我的当前位置（设定速度）开始在设定的位置（设定速度）附近震动

// Increase kd as much as possible without introducing jittering to the response.
//逐步增加kd直到引入了新的震动

//通俗的理解：kp决定了电机的劲儿大还是小，握拳，用尽全力握拳就开始发抖，同样电机开始震颤，
//ki一般不用
//kd用的也少
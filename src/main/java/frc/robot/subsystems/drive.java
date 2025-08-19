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


//Manual tuning typically follows this process:

// Set all gains to zero.

// Determine kg if using an elevator or arm.
//克服重力的参数，kg从0开始逐渐增加，直到松手电梯能够大概稳定在当前位置，不会下坠

// Select the appropriate Static Feedforward Sign for your closed-loop type.
//如果是速度控制，就用velocitysign，位置控制就用closedloopsign

// Increase ks until just before the motor moves.
//逐步增加ks直到电机微微有反应，处在一种临界有反应要动但是没动的状态

// If using velocity setpoints, increase kv
//  until the output velocity closely matches the velocity setpoints.
//如果你用速度控制并且需要设定速度到某个值，可以逐步增加kv直到你的速度达到设定值
//kv'是一个放大系数，当我的速度不够的时候，用这个来提高我的速度到预期值

// Increase kp until the output starts to oscillate around the setpoint.
//逐步增加kp直到我的当前位置（设定速度）开始在设定的位置（设定速度）附近震动

// Increase kd as much as possible without introducing jittering to the response.
//逐步增加kd直到引入了新的震动

//通俗的理解：kp决定了电机的劲儿大还是小，握拳，用尽全力握拳就开始发抖，同样电机开始震颤，
//kp尽可能大胆
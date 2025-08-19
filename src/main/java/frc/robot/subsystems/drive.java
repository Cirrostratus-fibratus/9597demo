// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//基类：基础的类

public class Drive extends SubsystemBase {

  private final TalonFX m_test_motor = new TalonFX(2, "rio");
  private final TalonFX m_test_motor2 = new TalonFX(1, "rio");
  private final TalonFX m_test_motor3 = new TalonFX(3, "rio");
  private final TalonFX m_test_motor4 = new TalonFX(4, "rio");



    //特性：请求制 需要一个request
    private final VoltageOut m_test_motor_request = new VoltageOut(0.0);
    private final VoltageOut m_test_motor2_request = new VoltageOut(0.0);
    private final VoltageOut m_test_motor3_request = new VoltageOut(0.0);
    private final VoltageOut m_test_motor4_request = new VoltageOut(0.0);
    //通过m_test_motor_request向上发送请求
    
    //实际控制
    //封装出来的方法
    //控制电机的方法：
    //1：速度
    //2：位置
    //高级的控制方法  本质就是优化速度和位置控制
    //withPosition能够把高级的控控制请求和底层的位置控制建立联系
    //withVelocity能够把高级的控控制请求和底层的速度控制建立联系

    public void setmotorVoltage(double vol) {
      m_test_motor.setControl(m_test_motor_request.withOutput(vol));
      m_test_motor2.setControl(m_test_motor_request.withOutput(vol));

    }

    public void setmotorVoltage2(double vol) {
      m_test_motor3.setControl(m_test_motor_request.withOutput(vol));
      m_test_motor4.setControl(m_test_motor_request.withOutput(vol)); //声明这个控制是速度还是位置
    }




    public Command Motor_Voltage_Command(double vol){ //runEnd能够让你摁住的时候转，松开的时候停
      return runEnd(()->{ //run == runOnce
                        setmotorVoltage(vol); // Set the motor to move at 1000 units per second
                        },
                    ()-> {
                          setmotorVoltage(0);
                         });
      }

    public Command Motor_Voltage_Command2(double vol){ //runEnd能够让你摁住的时候转，松开的时候停
      return run(()->{ //run == runOnce
                          setmotorVoltage(vol); // Set the motor to move at 1000 units per second
                          });
      }
  
  //构造函数：初始化子系统，读取电机的固定参数
  public Drive() {

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
      m_test_motor2.getConfigurator().apply(motorConfigs);
  }

  // /**
  //  * Example command factory method.
  //  *
  //  * @return a command
  //  */
  // public Command exampleMethodCommand() {  //一个方法 return 的类型是 command 类型
  //   // Inline construction of command goes here.
  //   // Subsystem::RunOnce implicitly requires `this` subsystem.
  //   return runOnce(
  //       () -> { //这个大括号里可以执行多个操作 //这是runOnce固定语法
  //         //子系统1
  //         //子系统2
  //         //子系统3
  //         /* one-time action goes here */
  //       });
  // }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
  //  * return value of some boolean subsystem state, such as a digital sensor.
  //  */
  // public boolean exampleCondition() { //之前的类里没有的不用写Override
  //   // Query some boolean state, such as a digital sensor.
  //   return false;
  // }

  // @Override
  // public void periodic() {
  //   // This method will be called once per scheduler run
  // }

  // @Override
  // public void simulationPeriodic() {
  //   // This method will be called once per scheduler run during simulation
  // }
}

//拆分：把复杂的东西简单化

//Subsystem：
//系统和子系统
//把小的部分联合起来就能控制一整个车

//command:
//一个简单的方法
//指令：告诉机器人执行什么动作
//封装好一些方法，在command里调用
//简单的command和封装的写法几乎一样
//复杂的command就是很多简单的command在一起
//command的返回值也是command类型

//FRC编程
//其他情况下的编程 循环是 for while
//FRC里没有显示的for和while，每次运行的时间不一定完全一样
//FRC里最大的用periodic()：每隔20毫秒循环一次
//periodic()：检查一下我的视觉检查结果，那我就可以在periodic里面写一个For或者while循环
//来遍历我的检测结果

//必须保持机器人一直动，所以用periodic


//clone完成以后：远程仓库 = 远程远程仓库
//本地落后于远程
//要注意!!! 本地  不落后于  远程
//远程有一个我本地没有出现过的操作
//用git pull 同步

//远程 a  →  b      本地 a  →  c ❌

//远程 a  →  b      本地 a  →  b  ， a  →  c ✔ 

//因为 你在本地 有过 a  →  b 的操作 所以本地不落后于远程
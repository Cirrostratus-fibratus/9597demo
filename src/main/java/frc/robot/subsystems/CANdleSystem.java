// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.



package frc.robot.subsystems;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.FireAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.RgbFadeAnimation;
import com.ctre.phoenix.led.SingleFadeAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;
import com.ctre.phoenix.led.TwinkleAnimation.TwinklePercent;
import com.ctre.phoenix.led.TwinkleOffAnimation;
import com.ctre.phoenix.led.TwinkleOffAnimation.TwinkleOffPercent;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class CANdleSystem extends SubsystemBase {
    private final CANdle m_candle = new CANdle(1, "rio"); //声明灯带对象
    private final int LedCount = 300; //灯珠数量 ，设得比较小 那灯带有可能只亮一半
    private XboxController joystick;

    private Animation m_toAnimate = null; //要变换到的状态
                                          // 一种灯效    →   一种状态
                                          // 灯效的切换  →   状态切换

    public enum AnimationTypes { //包含所有要枚举的变量   枚举 == 选一个出来   //属性  //特点
        ColorFlow,
        Fire,
        Larson,
        Rainbow,
        RgbFade,
        SingleFade,
        Strobe,
        Twinkle,
        TwinkleOff,
        SetAll
    }
    private AnimationTypes m_currentAnimation;

    public CANdleSystem() { //构造函数   特点1 ：名字得跟类的名字保持一样  特点2 ：没有类型
        //this.joystick = joy;
        changeAnimation(AnimationTypes.SetAll);
        CANdleConfiguration configAll = new CANdleConfiguration();
        configAll.statusLedOffWhenActive = true;
        configAll.disableWhenLOS = false;
        configAll.stripType = LEDStripType.GRB;
        configAll.brightnessScalar = 0.1;
        configAll.vBatOutputMode = VBatOutputMode.Modulated;
        m_candle.configAllSettings(configAll, 100);
    }

    //一般情况下，我们程序里涉及到几个固定状态之间的切换
    //通常我们采用标志位
    //flag
    //int flag 当flag为 1 的时候 状态为 1 
    //int flag 当flag为 2 的时候 状态为 2
    //innt next_flag 下一个要切换的状态

    // This method is used to increase the animation state 
    public void incrementAnimation() {  //方法  //Animation == 状态  //状态递增 //状态 1 变到状态 2
        switch(m_currentAnimation) {
            case ColorFlow: changeAnimation(AnimationTypes.Fire); break;
            case Fire: changeAnimation(AnimationTypes.Larson); break;
            case Larson: changeAnimation(AnimationTypes.Rainbow); break;
            case Rainbow: changeAnimation(AnimationTypes.RgbFade); break;
            case RgbFade: changeAnimation(AnimationTypes.SingleFade); break;
            case SingleFade: changeAnimation(AnimationTypes.Strobe); break;
            case Strobe: changeAnimation(AnimationTypes.Twinkle); break;
            case Twinkle: changeAnimation(AnimationTypes.TwinkleOff); break;
            case TwinkleOff: changeAnimation(AnimationTypes.ColorFlow); break;
            case SetAll: changeAnimation(AnimationTypes.ColorFlow); break;
        }
    }   //ColorFlow → Fire → Larson

    // This method is used to decrease the animation state 
    public void decrementAnimation() {  //方法
        switch(m_currentAnimation) {
            case ColorFlow: changeAnimation(AnimationTypes.TwinkleOff); break;
            case Fire: changeAnimation(AnimationTypes.ColorFlow); break;
            case Larson: changeAnimation(AnimationTypes.Fire); break;
            case Rainbow: changeAnimation(AnimationTypes.Larson); break;
            case RgbFade: changeAnimation(AnimationTypes.Rainbow); break;
            case SingleFade: changeAnimation(AnimationTypes.RgbFade); break;
            case Strobe: changeAnimation(AnimationTypes.SingleFade); break;
            case Twinkle: changeAnimation(AnimationTypes.Strobe); break;
            case TwinkleOff: changeAnimation(AnimationTypes.Twinkle); break;
            case SetAll: changeAnimation(AnimationTypes.ColorFlow); break;
        }
    }
    public void setColors() {
        changeAnimation(AnimationTypes.SetAll);
    }

    public void setOff() {
        m_candle.animate(null);
        // m_candle.animate(null);
        // m_candle.setLEDs(0, 0, 0);
        m_candle.setLEDs(0, 0, 0);   //像素值最高255 ， r = 255 纯红色
        changeAnimation(AnimationTypes.SetAll);

    }

    public void setFire() {
        m_toAnimate = new FireAnimation(0.5, 0.7, LedCount, 0.7, 0.5);
    }

    public void setColorFlow() {
        m_toAnimate = new ColorFlowAnimation(128, 20, 70, 0, 0.7, LedCount, Direction.Forward);
    }

    /* Wrappers so we can access the CANdle from the subsystem */
    public double getVbat() { return m_candle.getBusVoltage(); }
    public double get5V() { return m_candle.get5VRailVoltage(); }
    public double getCurrent() { return m_candle.getCurrent(); }
    public double getTemperature() { return m_candle.getTemperature(); }
    public void configBrightness(double percent) { m_candle.configBrightnessScalar(percent, 0); }
    public void configLos(boolean disableWhenLos) { m_candle.configLOSBehavior(disableWhenLos, 0); }
    public void configLedType(LEDStripType type) { m_candle.configLEDType(type, 0); }
    public void configStatusLedBehavior(boolean offWhenActive) { m_candle.configStatusLedState(offWhenActive, 0); }

    public void changeAnimation(AnimationTypes toChange) {
        m_currentAnimation = toChange; //更新状态 //更新状态标志位 时刻和机器人一起更新
        
        switch(toChange) // toChange 是标志位的判断
        {
            case ColorFlow:
                m_toAnimate = new ColorFlowAnimation(128, 20, 70, 0, 0.7, LedCount, Direction.Forward);
                //创建出 m_toAnimate 对象 后面再执行实际操作
                break;
            case Fire:
                m_toAnimate = new FireAnimation(0.5, 0.7, LedCount, 0.7, 0.5);
                break;
            case Larson:
                m_toAnimate = new LarsonAnimation(0, 255, 46, 0, 1, LedCount, BounceMode.Front, 3);
                break;
            case Rainbow:
                m_toAnimate = new RainbowAnimation(1, 0.1, LedCount);
                break;
            case RgbFade:
                m_toAnimate = new RgbFadeAnimation(0.7, 0.4, LedCount);
                break;
            case SingleFade:
                m_toAnimate = new SingleFadeAnimation(50, 2, 200, 0, 0.5, LedCount);
                break;
            case Strobe:
                m_toAnimate = new StrobeAnimation(240, 10, 180, 0, 98.0 / 256.0, LedCount);
                break;
            case Twinkle:
                m_toAnimate = new TwinkleAnimation(30, 70, 60, 0, 0.4, LedCount, TwinklePercent.Percent6);
                break;
            case TwinkleOff:
                m_toAnimate = new TwinkleOffAnimation(70, 90, 175, 0, 0.8, LedCount, TwinkleOffPercent.Percent100);
                break;
            case SetAll:
                m_toAnimate = null;
                break;
        }
        System.out.println("Changed to " + m_currentAnimation.toString());
    }


    public Command setFireWithMotor() {

        return startEnd(
                () ->{
                    m_toAnimate = new FireAnimation(0.5, 0.7, LedCount, 0.7, 0.5);
                },
                () ->{
                    setOff();
                }

        );

    }

    public Command set_Fire(){
        return runOnce(()->{ //run == runOnce
            m_toAnimate = new FireAnimation(0.5, 0.7, LedCount, 0.7, 0.5);
            });
    }

    public Command set_Larson(){
        return runOnce(()->{ //run == runOnce
            m_toAnimate = new LarsonAnimation(0, 255, 46, 0, 1, LedCount, BounceMode.Front, 3);
            });
    }

    public Command set_Candle_off(){
        return run(()->{ //run == runOnce
            setOff();
            });
    }


   

    //创建出一个火焰灯效 m_toAnimate
    //下一步 应用 .animate(要用的灯效对象)
    //生效一次 等待就亮一次 然后灭掉

    //有一个比较炫酷的灯效 想让他整场亮
    //用 periodic
    
    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // if(m_toAnimate == null) {
        //     m_candle.setLEDs((int)(joystick.getLeftTriggerAxis() * 255), 
        //                       (int)(joystick.getRightTriggerAxis() * 255), 
        //                       (int)(joystick.getLeftX() * 255));
        // } else {
        m_candle.animate(m_toAnimate);
        // }
       // m_candle.modulateVBatOutput(joystick.getRightY());
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}

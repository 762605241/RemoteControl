package com.lxl.robot;

//先介绍一下robot类的简单使用
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * 使用robot
 * @author 哑元
 *
 */
public class RobotTest {
    public static void main(String[] args) throws AWTException {
        Robot r = new Robot();
        r.mouseMove(300, 500);//鼠标移动
        r.mousePress(InputEvent.BUTTON1_MASK ); //鼠标按下
        r.mouseRelease(InputEvent.BUTTON1_MASK);//鼠标松开
        r.keyPress((int)'A');   //键盘按下  (int)'A'表示将A转换成键盘对应的key
        r.keyRelease((int)'A'); //键盘松开
    }
}


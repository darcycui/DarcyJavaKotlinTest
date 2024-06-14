package design.mode.command;

import design.mode.command.bean.fan.Fan;
import design.mode.command.bean.fan.IFan;
import design.mode.command.bean.light.ILight;
import design.mode.command.bean.light.Light;
import design.mode.command.command.ICommand;
import design.mode.command.command.impl.FanOffCommand;
import design.mode.command.command.impl.FanOnCommand;
import design.mode.command.command.impl.LightOffCommand;
import design.mode.command.command.impl.LightOnCommand;

/**
 * 命令模式: 将一个请求封装成一个命令，从而可以统一执行请求和撤销请求。
 */
public class Client {
    public static void main(String[] args) {
        // create a light
        ILight light = new Light();
        // use the light to create a on command
        ICommand lightOnCommand = new LightOnCommand(light);
        // use the light to create a off command
        ICommand lightOffCommand = new LightOffCommand(light);

        // create a remote control
        RemoteControl remoteControl = new RemoteControl();
        // set the light command to the remote control
        remoteControl.setCommand(0, lightOnCommand, lightOffCommand);
        // use the remote control to turn on the light
        remoteControl.onButtonWasPushed(0);
        // use the remote control to turn off the light
        remoteControl.offButtonWasPushed(0);

        IFan fan = new Fan();
        ICommand fanOnCommand = new FanOnCommand(fan);
        ICommand fanOffCommand = new FanOffCommand(fan);
        remoteControl.setCommand(1, fanOnCommand, fanOffCommand);
        remoteControl.onButtonWasPushed(1);
        remoteControl.offButtonWasPushed(1);
    }

}

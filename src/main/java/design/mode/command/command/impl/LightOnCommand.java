package design.mode.command.command.impl;

import design.mode.command.bean.light.ILight;
import design.mode.command.command.ICommand;

/**
 * Light on command
 */
public class LightOnCommand implements ICommand {
    /**
     * has a light (Receiver)
     */
    private ILight light;

    public LightOnCommand(ILight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        /**
         * execute command by the Receiver light
         */
        light.turnOn();
    }

    @Override
    public void undo() {

    }
}

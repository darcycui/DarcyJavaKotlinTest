package design.mode.command.command.impl;

import design.mode.command.bean.light.ILight;
import design.mode.command.command.ICommand;

/**
 * Light off command.
 */
public class LightOffCommand implements ICommand {
    /**
     * has a light (Receiver)
     */
    private ILight light;

    public LightOffCommand(ILight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        /**
         * execute command by the Receiver light
         */
        light.turnOff();
    }

    @Override
    public void undo() {

    }
}

package design.mode.command.command.impl;

import design.mode.command.bean.fan.IFan;
import design.mode.command.command.ICommand;

/**
 * fan off command
 */
public class FanOffCommand implements ICommand {
    /**
     * has a fan (Receiver)
     */
    private IFan fan;

    public FanOffCommand(IFan fan) {
        this.fan = fan;
    }

    @Override
    public void execute() {
        /**
         * execute command by the Receiver fan
         */
        fan.off();
    }

    @Override
    public void undo() {

    }
}

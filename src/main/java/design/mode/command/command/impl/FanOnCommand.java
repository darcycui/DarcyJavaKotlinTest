package design.mode.command.command.impl;

import design.mode.command.bean.fan.IFan;
import design.mode.command.command.ICommand;

public class FanOnCommand implements ICommand {
    /**
     * has a fan (Receiver)
     */
    private IFan fan;

    public FanOnCommand(IFan fan) {
        this.fan = fan;
    }

    @Override
    public void execute() {
        /**
         * execute command by the Receiver fan
         */
        fan.fast();
    }

    @Override
    public void undo() {

    }
}

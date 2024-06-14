package design.mode.command;

import design.mode.command.command.ICommand;
import design.mode.command.command.impl.NoCommand;

/**
 * Invoker: the one who call the command to execute
 */
public class RemoteControl {
    /**
     * on commands
     */
    ICommand[] onCommands;
    /**
     * off commands
     */
    ICommand[] offCommands;
    int COUNT = 3;

    public RemoteControl() {
        onCommands = new ICommand[COUNT];
        offCommands = new ICommand[COUNT];

        for (int i = 0; i < COUNT; i++) {
            /**
             * initialize with no command
             */
            onCommands[i] = new NoCommand();
            offCommands[i] = new NoCommand();
        }
    }

    /**
     * set command on and off in one slot button
     * @param slot slot button
     * @param onCommand command on
     * @param offCommand command off
     */
    public void setCommand(int slot, ICommand onCommand, ICommand offCommand) {
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }

    /**
     * press on button
     * @param slot slot button
     */
    public void onButtonWasPushed(int slot) {
        onCommands[slot].execute();
    }

    /**
     * press off button
     * @param slot slot button
     */
    public void offButtonWasPushed(int slot) {
        offCommands[slot].execute();
    }
}

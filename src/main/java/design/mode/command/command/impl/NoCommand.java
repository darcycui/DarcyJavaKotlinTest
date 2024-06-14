package design.mode.command.command.impl;

import design.mode.command.command.ICommand;

/**
 * default empty command
 * used to avoid null pointer exception
 */
public class NoCommand implements ICommand {
    @Override
    public void execute() {
        System.out.println("NoCommand execute");
    }

    @Override
    public void undo() {

    }
}

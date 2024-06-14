package design.mode.command.command;

/**
 * Command interface
 */
public interface ICommand {
    /**
     * execute command
     */
    void execute();

    void undo();
}

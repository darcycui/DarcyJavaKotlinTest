package design.mode.command.bean.fan;

/**
 * fan entity
 * functions: slow, fast, off
 */
public class Fan implements IFan{
    @Override
    public void slow() {
        System.out.println("Fan slow");
    }

    @Override
    public void fast() {
        System.out.println("Fan fast");
    }

    @Override
    public void off() {
        System.out.println("Fan off");
    }
}

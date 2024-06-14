package design.mode.command.bean.light;

/**
 * light entity
 * functions : turn on and turn off
 */
public class Light implements ILight{
    @Override
    public void turnOn() {
        System.out.println("Light is on");
    }

    @Override
    public void turnOff() {
        System.out.println("Light is off");
    }
}

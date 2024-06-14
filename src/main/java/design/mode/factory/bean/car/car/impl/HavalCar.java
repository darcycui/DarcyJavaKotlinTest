package design.mode.factory.bean.car.car.impl;

import design.mode.factory.bean.car.car.ICar;
import design.mode.factory.bean.car.engine.IEngine;
import design.mode.factory.bean.car.wheel.IWheel;

public class HavalCar implements ICar {
    private final IEngine engine;
    private final IWheel wheel;

    private final String TAG = this.getClass().getSimpleName() + ":";

    public HavalCar(IEngine engine, IWheel wheel) {
        this.engine = engine;
        this.wheel = wheel;
    }

    @Override
    public void run() {
        engine.turnOn();
        wheel.forward();
        wheel.backward();
        System.out.println(TAG + "run...");
        engine.turnOff();
    }
}

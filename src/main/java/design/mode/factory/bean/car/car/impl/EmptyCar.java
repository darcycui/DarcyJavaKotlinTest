package design.mode.factory.bean.car.car.impl;

import design.mode.factory.bean.car.car.ICar;
import design.mode.factory.bean.car.engine.IEngine;
import design.mode.factory.bean.car.wheel.IWheel;

public class EmptyCar implements ICar {
    private final String TAG = this.getClass().getSimpleName() + ":";

    public EmptyCar(IEngine engine, IWheel wheel) {

    }

    @Override
    public void run() {
        System.out.println(TAG + "run ERROR.");
    }
}

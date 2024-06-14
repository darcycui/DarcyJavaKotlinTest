package design.mode.factory.bean.car.engine.impl;

import design.mode.factory.bean.car.engine.IEngine;

public class BYDEngine implements IEngine {
    private final String TAG = this.getClass().getSimpleName() + ":";
    @Override
    public void turnOn() {
        System.out.println(TAG + "turnOn");
    }

    @Override
    public void turnOff() {
        System.out.println(TAG + "turnOff");
    }
}

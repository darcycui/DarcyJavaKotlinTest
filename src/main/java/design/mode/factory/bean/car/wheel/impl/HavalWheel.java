package design.mode.factory.bean.car.wheel.impl;

import design.mode.factory.bean.car.wheel.IWheel;

public class HavalWheel implements IWheel {
    private final String TAG = this.getClass().getSimpleName() + ":";
    @Override
    public void forward() {
        System.out.println(TAG + "forward+++");
    }

    @Override
    public void backward() {
        System.out.println(TAG + "backward---");
    }
}

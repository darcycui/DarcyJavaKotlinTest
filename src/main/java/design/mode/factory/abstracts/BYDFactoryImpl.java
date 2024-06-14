package design.mode.factory.abstracts;

import design.mode.factory.bean.car.engine.IEngine;
import design.mode.factory.bean.car.engine.impl.BYDEngine;
import design.mode.factory.bean.car.wheel.IWheel;
import design.mode.factory.bean.car.wheel.impl.BYDWheel;

/**
 * BYD工厂
 */
public class BYDFactoryImpl implements ICarFactory {

    /**
     * 造BYD发动机
     */
    @Override
    public IEngine createEngine() {
        return new BYDEngine();
    }

    /**
     * 造BYD轮子
     */
    @Override
    public IWheel createWheel() {
        return new BYDWheel();
    }
}

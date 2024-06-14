package design.mode.factory.abstracts;

import design.mode.factory.bean.car.engine.IEngine;
import design.mode.factory.bean.car.engine.impl.HavalEngine;
import design.mode.factory.bean.car.wheel.IWheel;
import design.mode.factory.bean.car.wheel.impl.HavalWheel;

/**
 * 哈佛工厂
 */
public class HavalFactoryImpl implements ICarFactory {

    /**
     * 造哈佛发动机
     */
    @Override
    public IEngine createEngine() {
        return new HavalEngine();
    }

    /**
     * 造哈佛轮子
     */
    @Override
    public IWheel createWheel() {
        return new HavalWheel();
    }
}

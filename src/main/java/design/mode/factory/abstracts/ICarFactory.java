package design.mode.factory.abstracts;

import design.mode.factory.bean.car.engine.IEngine;
import design.mode.factory.bean.car.wheel.IWheel;

/**
 * 抽象工厂
 */
public interface ICarFactory {
    IEngine createEngine();
    IWheel createWheel();
}

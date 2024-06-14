package design.mode.factory.abstracts;

import design.mode.factory.bean.car.engine.IEngine;
import design.mode.factory.bean.car.wheel.IWheel;

/**
 * 抽象工厂模式: 有多个抽象方法负责创建产品，每个抽象方法负责创建一个产品，
 */
public interface ICarFactory {
    /**
     * 造发送机
     */
    IEngine createEngine();

    /**
     * 造轮子
     */
    IWheel createWheel();
}

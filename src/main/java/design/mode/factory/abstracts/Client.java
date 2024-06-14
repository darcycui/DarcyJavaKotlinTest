package design.mode.factory.abstracts;

import design.mode.factory.bean.car.car.ICar;
import design.mode.factory.bean.car.car.impl.BYDCar;
import design.mode.factory.bean.car.car.impl.EmptyCar;
import design.mode.factory.bean.car.car.impl.HavalCar;

public class Client {
    public static void main(String[] args) {
        BYDFactoryImpl bydFactory = new BYDFactoryImpl();
        ICar iCar = new BYDCar(bydFactory.createEngine(), bydFactory.createWheel());
        iCar.run();

        System.out.println("==========");
        ICarFactory havalFactory = new HavalFactoryImpl();
        iCar = new HavalCar(havalFactory.createEngine(), havalFactory.createWheel());
        iCar.run();

        System.out.println("==========");
        ICarFactory emptyFactory = new HavalFactoryImpl();
        iCar = new EmptyCar(emptyFactory.createEngine(), emptyFactory.createWheel());
        iCar.run();
    }
}

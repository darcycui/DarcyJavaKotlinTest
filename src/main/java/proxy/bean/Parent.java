package proxy.bean;

import proxy.IService;

public class Parent implements IService {
    private int x;
    private int y;
    private Child son;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Child getSon() {
        return son;
    }

    public void setSon(Child son) {
        this.son = son;
    }

    public Parent(int x, int y, Child son) {
        this.x = x;
        this.y = y;
        this.son = son;
    }

    @Override
    public void doSomething(String param) {
        System.out.println("Person 正在做事..." + son.getName());
    }
}

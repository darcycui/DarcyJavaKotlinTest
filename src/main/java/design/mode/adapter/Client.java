package design.mode.adapter;

import design.mode.adapter.bean.current.CurrentImpl;
import design.mode.adapter.bean.current.ICurrent;
import design.mode.adapter.bean.target.ITarget;
import design.mode.adapter.bean.target.TargetImpl;

/**
 * 适配器模式: 将已有类适配成目标类
 */
public class Client {
    public static void main(String[] args) {
        ITarget iTarget = new TargetImpl();
        iTarget.create();
        iTarget.bind();

        System.out.println("==========");
        ICurrent iCurrent = new CurrentImpl();
        iTarget = new FeedAdapter(iCurrent);
        iTarget.create();
        iTarget.bind();
    }
}

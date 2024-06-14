package design.mode.adapter.bean.target;

public class TargetImpl implements ITarget {
    private final String TAG = this.getClass().getSimpleName() + ":";

    @Override
    public void create() {
        System.out.println(TAG + "create");
    }

    @Override
    public void bind() {
        System.out.println(TAG + "bind");
    }
}

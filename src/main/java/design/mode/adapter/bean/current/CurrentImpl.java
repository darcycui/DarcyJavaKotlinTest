package design.mode.adapter.bean.current;

public class CurrentImpl implements ICurrent {
    private final String TAG = this.getClass().getSimpleName() + ":";

    @Override
    public void onBindViewHolder(int i) {
        System.out.println(TAG + "onBindViewHolder执行-" + i);
    }

    @Override
    public void onCreateViewHolder(int i) {
        System.out.println(TAG + "onCreateViewHolder执行-" + i);
    }
}

package design.mode.adapter;

import design.mode.adapter.bean.current.ICurrent;
import design.mode.adapter.bean.target.ITarget;

/**
 * 适配器实现目标接口
 */
public abstract class BaseAdapter implements ITarget {
    private final String TAG = this.getClass().getSimpleName() + ":";
    private final ICurrent iCurrent;

    public BaseAdapter(ICurrent iCurrent) {
        this.iCurrent = iCurrent;
    }

    abstract int getItemCount();

    @Override
    public void create() {
        System.out.println(TAG + "create");
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            iCurrent.onCreateViewHolder(i);
        }
    }

    @Override
    public void bind() {
        System.out.println(TAG + "bind");
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            iCurrent.onBindViewHolder(i);
        }
    }
}

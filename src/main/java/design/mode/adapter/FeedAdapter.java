package design.mode.adapter;

import design.mode.adapter.bean.current.ICurrent;

public class FeedAdapter extends BaseAdapter {
    public FeedAdapter(ICurrent iCurrent) {
        super(iCurrent);
    }

    @Override
    int getItemCount() {
        return 3;
    }
}

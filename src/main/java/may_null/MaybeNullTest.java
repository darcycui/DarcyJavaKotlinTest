package may_null;

import java.util.ArrayList;
import java.util.List;

public class MaybeNullTest {
    private static List<Msg> dataStore = null;

    private static void init() {
        dataStore = new ArrayList<>(10);
        dataStore.add(null);
        dataStore.add(new Msg(0, new SubBean()));
    }

    public static void main(String[] args) {
        init();
        if (dataStore.get(0) != null) {
            dataStore.get(0).sayHello();
        }
    }
}

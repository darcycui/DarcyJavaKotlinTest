package may_null;

public class Msg {
    public int what;
    public SubBean subBean;

    public Msg(int what, SubBean subBean) {
        this.what = what;
        this.subBean = subBean;
    }

    public void sayHello() {
        System.out.println("this is Msg.");
    }
}

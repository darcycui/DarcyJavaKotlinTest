package jni;

public class TestJni {
    public native String getJniText();
    static {
//        System.loadLibrary("TestJni");
        System.load("C:\\Projects\\IdeaProjects\\KotlinTest\\lib\\libTestJni.dll");
    }

    public static void main(String[] args) {
        System.out.println(new TestJni().getJniText());
    }
}

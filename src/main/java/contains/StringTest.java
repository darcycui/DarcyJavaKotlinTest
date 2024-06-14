package contains;

import java.util.regex.Pattern;

public class StringTest {
    public static void main(String[] args) {
        String str = "/storage/emulated/0/Android/data/org.telegram.messenger.beta/cache/img-17047058479193d69c337b7e0478b6810e98eb64616d4aa37610f35533e96d1de225458cfe661.jpg";
        String target1 = "0/Android/data/org.telegram.messenger*/files/Pictures";
        String target2 = "0/Android/data/org.telegram.messenger*/cache/";
        System.out.println("str contains " + target1 + "==" + contains(str, target1));
        System.out.println("str contains " + target2 + "==" + contains(str, target2));
    }

    /**
     * 匹配是否包含
     *
     * @param source 需要查找的字符串
     * @param regex  正则表达式
     * @return 是否包含
     */
    public static boolean contains(String source, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(source).find();
    }
}

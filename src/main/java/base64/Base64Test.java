package base64;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

public class Base64Test {
    public static void main(String[] args) {
//        final String text = "{notes:代码}";
        final String text = "1234";
        System.out.println("text-->" + text);
        //base64编码
        final String encodedText = encode(text);
        //base64解码
        decode(encodedText);
        decode(text);
        decode("aaaa");
        // 加号（+）变空格问题
        plusToSpaceTest();
    }

    private static void plusToSpaceTest() {
        // 空格URLEncoder编码是加号
        String text = " ";
        String urlEncodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        System.out.println("URLEncode-->" + urlEncodedText); // 输出:+
        // 加号URLDecoder解码是空格
        System.out.println("URLDecode-->" + URLDecoder.decode("+", StandardCharsets.UTF_8) + "aaa"); // 输出:空格aaa

        // 没有经过URLEncoder编码，直接使用URLDecoder解码，加号变成空格：e25vdGVzOuS7o eggX0
        String urlEncodedText2 = "e25vdGVzOuS7o+eggX0=";
        System.out.println("URLDecode2-->" + URLDecoder.decode(urlEncodedText2, StandardCharsets.UTF_8));

        // 先经过URLEncoder编码，再使用URLDecoder解码，解码正常
        String urlEncodedText3 = "e25vdGVzOuS7o+eggX0=";
        urlEncodedText3 = URLEncoder.encode(urlEncodedText3, StandardCharsets.UTF_8);
        System.out.println("URLEncode3-->" + urlEncodedText3);
        System.out.println("URLDecode3-->" + URLDecoder.decode(urlEncodedText3, StandardCharsets.UTF_8));
    }

    public static String encode(String text) {
        final byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
        final Base64.Encoder encoder = Base64.getEncoder();
        final String encodedText = encoder.encodeToString(textByte);
        System.out.println("encodedText-->" + encodedText);
        return encodedText;
    }

    public static String decode(String text) {
        final Base64.Decoder decoder = Base64.getDecoder();
        String decodedText = null;
        if (isBase64Traverse(text)) {
            decodedText = new String(decoder.decode(text), StandardCharsets.UTF_8);
            System.out.println("decodedText-->" + decodedText);
        } else {
            System.out.println("not base64 originalText-->" + text);
        }
        return decodedText;
    }

    /**
     * 正则表达式 判断是否是base64
     */
    private static boolean isBase64Pattern(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }


    /**
     * 遍历 判断是否是base64
     */
    private static boolean isBase64Traverse(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        } else {
            if (str.length() % 4 != 0) {
                return false;
            }

            char[] strChars = str.toCharArray();
            for (char c : strChars) {
                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')
                        || c == '+' || c == '/' || c == '=') {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        }
    }
}

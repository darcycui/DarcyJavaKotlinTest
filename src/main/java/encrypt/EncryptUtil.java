package encrypt;

import kotlin.Pair;

public class EncryptUtil {

    /**
     * 将 16 进制字符串转换为字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("16 进制字符串的长度必须是偶数");
        }

        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 将 byte[] 转换为 16 进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static byte[] appendArrays(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] result = new byte[length];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static Pair<byte[], byte[]> splitArray(byte[] array, int subArrayLength) {
        Pair<byte[], byte[]> result = new Pair<>(new byte[subArrayLength], new byte[subArrayLength]);
        System.arraycopy(array, 0, result.getFirst(), 0, subArrayLength);
        System.arraycopy(array, subArrayLength, result.getSecond(), 0, subArrayLength);
        return result;
    }
}

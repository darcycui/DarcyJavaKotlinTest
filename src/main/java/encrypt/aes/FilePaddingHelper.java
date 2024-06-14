package encrypt.aes;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FilePaddingHelper {

    /**
     * 去除文件末尾的连续字节0
     * @param path 文件路径
     */
    public static void deletePadding0(String path) {
        if (path == null || path.isEmpty()) {
            return;
        }
        RandomAccessFile raf = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                return;
            }
            raf = new RandomAccessFile(file, "rw");
            long length = raf.length();
            if (length == 0) {
                raf.close();
                return;
            }
            length--;
            raf.seek(length);
            byte b;
            int count = 0;
            while (length >= 0) {
                raf.seek(length);
                b = raf.readByte();
                if (b != 0) {
                    raf.setLength(length + 1);
                    break;
                }
                length--;
                count++;
            }
            System.out.print("[customlog] End DELETE: " + count + "bit 0");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 文件长度不是16的倍数 则在文件末尾添加字节0
     * @param path 文件路径
     */
    public static void addPadding0(String path) {
        if (path == null || path.isEmpty()) {
            return;
        }
        try {
            File file = new File(path);
            long fileLength = file.length();
            if (fileLength % 16 != 0) {
                long paddingLength = 16 - (fileLength % 16);
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.seek(fileLength);
                for (long i = 0; i < paddingLength; i++) {
                    raf.writeByte(0);
                }
                raf.close();
                System.out.print("[customlog] Not 16 multiple. ADD " + paddingLength + "bit 0");
            } else {
                System.out.print("[customlog] Already 16 multiple. ADD 0 bit 0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

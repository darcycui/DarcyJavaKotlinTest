package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileTest {
    public static void main(String[] args) {
        deleteEndPadding("D:\\aaa\\download\\5_6181215571175017841 - 副本.jpg");
    }

    private static void deleteEndPadding(String path) {
        try {
            File file = new File(path);
            RandomAccessFile raf = new RandomAccessFile(file, "rw");

            long length = raf.length();
            if (length == 0) {
                // 文件为空，无需处理
                raf.close();
                return;
            }

            length--;  // 从文件末尾开始逐个向前读取字节
            raf.seek(length);
            byte b;

            while (length >= 0) {
                raf.seek(length);
                b = raf.readByte();
                if (b != 0) {
                    // 找到第一个非零字节，截取文件并保存
                    raf.setLength(length + 1);
                    break;
                }
                length--;
            }

            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

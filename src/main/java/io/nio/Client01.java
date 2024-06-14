package io.nio;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

//TCP协议Socket：客户端
public class Client01 {
    public static void main(String[] args) throws IOException {
        //创建套接字对象socket并封装ip与port
        Socket socket = new Socket("127.0.0.1", 8000);
        //根据创建的socket对象获得一个输出流
        OutputStream outputStream = socket.getOutputStream();
        //控制台输入以IO的形式发送到服务器
        System.out.println("1##TCP连接成功 \n请输入：");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        byte[] car = new Scanner(System.in).nextLine().getBytes();
                        outputStream.write(car);
                        System.out.println("1##TCP协议的Socket发送成功");
                        //刷新缓冲区
                        outputStream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    InputStream inputStream = socket.getInputStream();
//                    BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
//                    String line = null;
//                    while ((line = buffer.readLine()) != null) {
//                        System.out.println("1##收到消息：" + line);
//                    }
                    InputStream in = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    //读取数据
                    System.out.println("1##收到消息：");
                    while ((len = in.read(buffer)) > 0) {
                        System.out.println(new String(buffer, 0, len));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


    }
}

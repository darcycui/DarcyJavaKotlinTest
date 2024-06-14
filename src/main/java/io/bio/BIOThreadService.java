package io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

//TCP协议Socket使用多线程BIO进行通行：服务端
public class BIOThreadService {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8000);
            System.out.println("服务端启动成功，监听端口为8000，等待客户端连接... ");
            while (true) {
                Socket socket = server.accept();//等待客户连接
                Scanner scanner = new Scanner(System.in);
                System.out.println("客户连接成功，客户信息为：" + socket.getRemoteSocketAddress());
                //针对每个连接创建一个线程， 去处理I0操作
                //创建多线程创建开始
                Thread threadReceive = new Thread(new Runnable() {
                    public void run() {
                        try {
                            InputStream in = socket.getInputStream();
                            byte[] buffer = new byte[1024];
                            int len = 0;
                            //读取客户端的数据
                            while ((len = in.read(buffer)) > 0) {
                                System.out.println(new String(buffer, 0, len));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                threadReceive.start();

                //针对每个连接创建一个线程， 去处理I0操作
                //创建多线程创建开始
                Thread threadSend = new Thread(new Runnable() {
                    public void run() {
                        try {
                            //向客户端写数据
                            while (true) {
                                System.out.println("server写数据:");
                                OutputStream out = socket.getOutputStream();
                                byte[] car = new Scanner(System.in).nextLine().getBytes();
                                out.write(car);
                                out.flush();
                                System.out.println("TCP协议的Socket发送成功");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                threadSend.start();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

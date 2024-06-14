package threadpool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class Task implements Runnable {

    @Override
    public void run() {
        System.out.println("task start");
        for (int i = 0; i < 10; i++) {
            httpGet();
            System.out.println("task execute " + i);
            if (Thread.interrupted()) {
                System.out.println("线程已中断，退出执行");
                break;
            }
        }
        System.out.println("task finish");
    }

    private void httpGet() {

        String url = "https://stackoverflow.com/";
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url;
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
//                 遍历所有的响应头字段
//                for (String key : map.keySet()) {
//                    System.out.println(key + "--->" + map.get(key));
//                }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "/n" + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
//            System.out.print(result);
    }
}

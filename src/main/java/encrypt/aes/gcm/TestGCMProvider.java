package encrypt.aes.gcm;

import javax.crypto.Cipher;
import java.security.Provider;
import java.security.Security;

public class TestGCMProvider {
    /**
     * 输出 SunJCE → JDK 内置标准实现
     * 版本 ≥ 8u271 / 11.0.9 → 安全
     */
    public static void main(String[] args) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        System.out.println("算法提供者: " + cipher.getProvider().getName());
        System.out.println("JDK 版本: " + System.getProperty("java.version"));

        System.out.println("----------------------------------------------");
        Provider[] providers = Security.getProviders();
        for (Provider p : providers) {
            System.out.println(p.getName() + " (版本 " + p.getVersionStr() + ")");
            // 可选：查看该提供者支持的服务
            // p.getServices().forEach(s -> System.out.println("  " + s.getAlgorithm()));
        }
    }
}

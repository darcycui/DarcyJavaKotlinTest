package encrypt.kdf;

import encrypt.kdf.salt.SaltUtil;

/**
 * KDF:从简单密码导出复杂密码的函数
 */
public class TestKDF {
    private static final int SALT_BYTE_SIZE = 16;         //盐的长度
    public static void main(String[] args) {
        testHashSalt();
        testPBKDF2();
        testBcrypt();
        testScrypt();
        testArgon2();
    }

    /**
     * 普通 hash + salt
     * 缺乏计算成本控制和内存硬性要求 容易被暴力破解(如 ASIC)
     */
    private static void testHashSalt() {
        String originalPwd = "123456abc";
        System.out.println("originalPwd=" + originalPwd);
        // 生成salt 盐值
        String salt = SaltUtil.generateSaltString(SALT_BYTE_SIZE);
        long start = System.currentTimeMillis();
        String hashSaltPwd = HashSaltUtil.getHashedPassword(originalPwd, salt);
        long end = System.currentTimeMillis();
        System.out.println("hashSaltPwd=" + hashSaltPwd);
        System.out.println("耗时：" + (end - start) / 1000.0D + "秒");
        if (HashSaltUtil.checkPassword(originalPwd, hashSaltPwd, salt)) {
            System.out.println("校验成功");
        } else {
            System.out.println("校验失败");
        }
    }

    /**
     * PBKDF2 增加计算成本，抵抗暴力破解
     */
    private static void testPBKDF2() {
        String originalPwd = "123456abc";
        System.out.println("originalPwd=" + originalPwd);
        String salt = SaltUtil.generateSaltString(SALT_BYTE_SIZE);
        long start = System.currentTimeMillis();
        String pbkdf2Pwd = PBKDF2Util.getHashedPassword(originalPwd, salt);
        long end = System.currentTimeMillis();
        System.out.println("pbkdf2Pwd=" + pbkdf2Pwd);
        System.out.println("耗时：" + (end - start) / 1000.0D + "秒");
        if (PBKDF2Util.checkPassword(originalPwd, pbkdf2Pwd, salt)) {
            System.out.println("校验成功");
        } else {
            System.out.println("校验失败");
        }
    }

    /**
     * Bcrypt 计算成本高，抵抗暴力破解
     */
    private static void testBcrypt() {
        String originalPwd = "123456abc";
        System.out.println("originalPwd=" + originalPwd);
        long start = System.currentTimeMillis();
        String bcryptPwd = BcryptUtil.getHashedPassword(originalPwd);
        long end = System.currentTimeMillis();
        System.out.println("bcryptPwd=" + bcryptPwd);
        System.out.println("耗时：" + (end - start) / 1000.0D + "秒");
        if (BcryptUtil.checkPassword(originalPwd, bcryptPwd)) {
            System.out.println("校验成功");
        } else {
            System.out.println("校验失败");
        }
    }

    /**
     * Scrypt 计算成本高且消耗大量内存，抵抗暴力破解和GPU加速攻击
     */
    private static void testScrypt() {
        String originalPwd = "123456abc";
        System.out.println("originalPwd=" + originalPwd);
        String salt = SaltUtil.generateSaltString(SALT_BYTE_SIZE);
        long start = System.currentTimeMillis();
        String scryptPwd = ScryptUtil.getHashedPassword(originalPwd, salt);
        long end = System.currentTimeMillis();
        System.out.println("scryptPwd=" + scryptPwd);
        System.out.println("耗时：" + (end - start) / 1000.0D + "秒");
        if (ScryptUtil.checkPassword(originalPwd, scryptPwd, salt)) {
            System.out.println("校验成功");
        } else {
            System.out.println("校验失败");
        }
    }

    /**
     * Argon2 计算成本高且消耗大量内存，抵抗暴力破解、GPU加速攻击、侧信道攻击
     */
    private static void testArgon2() {
        String originalPwd = "123456abc";
        System.out.println("originalPwd=" + originalPwd);
        long start = System.currentTimeMillis();
        String argon2Pwd = Argon2Util.getHashedPassword(originalPwd);
        long end = System.currentTimeMillis();
        System.out.println("argon2Pwd=" + argon2Pwd);
        System.out.println("耗时：" + (end - start) / 1000.0D + "秒");
        if (Argon2Util.checkPassword(originalPwd, argon2Pwd)) {
            System.out.println("校验成功");
        } else {
            System.out.println("校验失败");
        }
    }
}

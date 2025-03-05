//package encrypt.aes.attack;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Arrays;
//
//public class PaddingOracleAttack {
//
//    private static final byte[] IV = "AbcdefAbcdefAbcd".getBytes(); // 16 bytes IV
//
//    public static void main(String[] args) throws Exception {
//        byte[] ciphertext = hexStringToByteArray("832848775be3d55cdb472885634450fd"); // Replace with your actual ciphertext
//        byte[] iv = IV;
//        byte[] plaintext = new byte[ciphertext.length - IV.length];
//
//        System.out.println("Ciphertext: " + bytesToHexString(ciphertext));
//        System.out.println("IV: " + bytesToHexString(iv));
//
//        for (int blockIndex = 0; blockIndex < ciphertext.length / 16 - 1; blockIndex++) {
//            byte[] currentBlock = Arrays.copyOfRange(ciphertext, blockIndex * 16, (blockIndex + 1) * 16);
//            byte[] previousBlock = Arrays.copyOfRange(ciphertext, (blockIndex - 1) * 16, blockIndex * 16);
//
//            byte[] modifiedPreviousBlock = new byte[16];
//            byte[] intermediateValue = new byte[16];
//
//            for (int byteIndex = 15; byteIndex >= 0; byteIndex--) {
//                for (int guess = 0; guess < 256; guess++) {
//                    System.arraycopy(previousBlock, 0, modifiedPreviousBlock, 0, 16);
//                    for (int i = 15; i > byteIndex; i--) {
//                        modifiedPreviousBlock[i] ^= (byte) (16 - byteIndex) ^ intermediateValue[i];
//                    }
//                    modifiedPreviousBlock[byteIndex] = (byte) guess;
//
//                    byte[] modifiedCiphertext = Arrays.copyOf(ciphertext, ciphertext.length);
//                    System.arraycopy(modifiedPreviousBlock, 0, modifiedCiphertext, blockIndex * 16, 16);
//
//                    if (PaddingOracle.isPaddingValid(modifiedCiphertext)) {
//                        intermediateValue[byteIndex] = (byte) (guess ^ (16 - byteIndex));
//                        plaintext[blockIndex * 16 + (15 - byteIndex)] = (byte) (intermediateValue[byteIndex] ^ previousBlock[byteIndex]);
//                        System.out.println("Found byte at index " + (blockIndex * 16 + (15 - byteIndex)) + ": " + plaintext[blockIndex * 16 + (15 - byteIndex)]);
//                        break;
//                    }
//                }
//            }
//        }
//
//        System.out.println("Recovered plaintext: " + new String(plaintext));
//    }
//
//    public static byte[] hexStringToByteArray(String s) {
//        int len = s.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
//                    + Character.digit(s.charAt(i + 1), 16));
//        }
//        return data;
//    }
//    public static String bytesToHexString(byte[] bytes) {
//        StringBuilder sb = new StringBuilder();
//        for (byte b : bytes) {
//            sb.append(String.format("%02x", b));
//        }
//        return sb.toString();
//    }
//}
//

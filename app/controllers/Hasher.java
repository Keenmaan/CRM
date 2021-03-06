package controllers;

/**
 * Created by keen on 10/7/14.
 */
public class Hasher {

    public static String getHash(String txt, String hashType) {
        try {
            if (txt.isEmpty())
                System.out.println("KUPA");
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            //error action
        }
        return null;
    }
}

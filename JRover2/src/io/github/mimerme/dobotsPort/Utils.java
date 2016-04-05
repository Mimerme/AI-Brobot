package io.github.mimerme.dobotsPort;

public class Utils {
    public static void waitSomeTime(int millis) {
        try {
            Thread.sleep((long) millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static String intToHex(int value) {
        return Integer.toHexString(value);
    }
}

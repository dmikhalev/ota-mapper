package cs.vsu.otamapper.utils;

public class StringUtils {

    public static String unescape(String val) {
        if (val.endsWith("\"") && val.startsWith("\"")) {
            val = val.substring(1, val.length() - 1);
        }
        return val;
    }
}

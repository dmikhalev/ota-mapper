package cs.vsu.otamapper.mapper;

import cs.vsu.otamapper.parser.Parser;
import cs.vsu.otamapper.parser.RegExp;

public class Matcher {

    public static boolean matches(String value, String regExpStr) {
        RegExp regExp = Parser.parseRegExp(regExpStr);
        if (regExp != null) {
            return regExp.execute(value);
        }
        return false;
    }
}

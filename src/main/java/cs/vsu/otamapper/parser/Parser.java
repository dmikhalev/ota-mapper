package cs.vsu.otamapper.parser;


import cs.vsu.otamapper.parser.term.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Parser {

    private static final String IF = "if";
    private static final String THEN = "then";

    enum PartType {
        BRACKETS, KEYWORD, AND, OR
    }

    public static RegExp parseRegExp(String regExpStr) {
        Pair<String, Integer> expToCode = getExpStrToCode(regExpStr);
        if (expToCode == null) {
            return null;
        }
        RegExpTerm parentTerm;
        try {
            parentTerm = parseExpression(expToCode.getFirst());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return new RegExp(parentTerm, expToCode.getSecond());
    }

    private static Pair<String, Integer> getExpStrToCode(String regExpStr) {
        regExpStr = regExpStr.toLowerCase().trim();
        if (!regExpStr.startsWith(IF)) {
            log.error("Regexp does not start with \"if\"");
            return null;
        }
        regExpStr = regExpStr.substring(IF.length());
        int thenIndex = regExpStr.lastIndexOf(THEN);
        if (thenIndex < 0) {
            log.error("Failed to parse regexp");
            return null;
        }
        String codeStr = regExpStr.substring(thenIndex + THEN.length() + 1);
        int code;
        try {
            code = Integer.parseInt(codeStr);
        } catch (NumberFormatException e) {
            log.error("Failed to parse regexp code");
            return null;
        }
        String expStr = regExpStr.substring(0, thenIndex).trim();
        if (expStr.isEmpty()) {
            log.error("Wrong regexp format");
            return null;
        }
        return Pair.of(expStr, code);
    }

    private static RegExpTerm parseExpression(String strPart) throws IllegalArgumentException {
        //  (('Executive') or 'Club') and ('room')
        //  'Executive' or 'Club' and 'room' or 'King'
        List<RegExpPart> parts;
        try {
            parts = toParts(strPart);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return null;
        }

        if (parts.size() == 1) {
            return new Keyword(parts.get(0).getValue());
        }

        for (int i = parts.size() - 2; i >= 0; i -= 2) {
            RegExpPart part = parts.get(i);
            if (part.getType() == PartType.OR) {
                Or or = new Or();
                setRegExpChild(parts.subList(0, i), or, true);
                setRegExpChild(parts.subList(i + 1, parts.size()), or, false);
                return or;
            }
        }

        for (int i = parts.size() - 2; i >= 0; i -= 2) {
            RegExpPart part = parts.get(i);
            if (part.getType() == PartType.AND) {
                And and = new And();
                setRegExpChild(parts.subList(0, i), and, true);
                setRegExpChild(parts.subList(i + 1, parts.size()), and, false);
                return and;
            }
        }
        return null;
    }

    private static void setRegExpChild(List<RegExpPart> parts, BaseParentTerm parent, boolean isLeft) {
        if (parts == null || parts.isEmpty()) {
            return;
        }
        if (parts.size() == 1) {
            String val = parts.get(0).getValue();
            if (isLeft) {
                parent.setLeft(new Keyword(val));
            } else {
                parent.setRight(new Keyword(val));
            }
            return;
        }
        for (int i = parts.size() - 2; i >= 0; i -= 2) {
            RegExpPart part = parts.get(i);
            if (part.getType() == PartType.OR) {
                Or or = new Or();
                setRegExpChild(parts.subList(0, i), or, true);
                setRegExpChild(parts.subList(i + 1, parts.size()), or, false);
                if (isLeft) {
                    parent.setLeft(or);
                } else {
                    parent.setRight(or);
                }
            }
        }
        if (isLeft && parent.getLeft() == null
                || !isLeft && parent.getRight() == null) {
            for (int i = parts.size() - 2; i >= 0; i -= 2) {
                RegExpPart part = parts.get(i);
                if (part.getType() == PartType.AND) {
                    And and = new And();
                    setRegExpChild(parts.subList(0, i), and, true);
                    setRegExpChild(parts.subList(i + 1, parts.size()), and, false);
                    if (isLeft) {
                        parent.setLeft(and);
                    } else {
                        parent.setRight(and);
                    }
                }
            }
        }
    }

    private static List<RegExpPart> toParts(String strPart) throws IllegalArgumentException {
        strPart = strPart.trim();
        if (strPart.isEmpty()) {
            throw new IllegalArgumentException("Parsing error");
        }
        char[] chars = strPart.toCharArray();
        List<RegExpPart> parts = new ArrayList<>();
        int pos = 0;
        while (pos < chars.length) {
            if (chars[pos] == '(') {
                int startPos = pos;
                int openInnerBracketCount = 0;
                pos++;
                if (pos == chars.length) {
                    throw new IllegalArgumentException("Bracket parsing error");
                }
                while (openInnerBracketCount != 0 || chars[pos] != ')') {
                    if (chars[pos] == '(') {
                        openInnerBracketCount++;
                    } else if (chars[pos] == ')') {
                        openInnerBracketCount--;
                    }
                    pos++;
                    if (pos == chars.length) {
                        throw new IllegalArgumentException("Bracket parsing error");
                    }
                }
                RegExpPart part = new RegExpPart(strPart.substring(startPos, pos + 1), PartType.BRACKETS);
                addPartToList(part, parts);
                pos++;
            } else if (chars[pos] == '\'') {
                int startPos = pos;
                pos++;
                if (pos == chars.length) {
                    throw new IllegalArgumentException("Keyword parsing error");
                }
                while (chars[pos] != '\'') {
                    pos++;
                    if (pos == chars.length) {
                        throw new IllegalArgumentException("Keyword parsing error");
                    }
                }
                RegExpPart part = new RegExpPart(strPart.substring(startPos, pos + 1), PartType.KEYWORD);
                addPartToList(part, parts);
                pos++;
            } else if (pos + 2 < chars.length && chars[pos] == 'a' && chars[pos + 1] == 'n' && chars[pos + 2] == 'd') {
                RegExpPart part = new RegExpPart(strPart.substring(pos, pos + 3), PartType.AND);
                addPartToList(part, parts);
                pos += 3;
            } else if (pos + 1 < chars.length && chars[pos] == 'o' && chars[pos + 1] == 'r') {
                RegExpPart part = new RegExpPart(strPart.substring(pos, pos + 2), PartType.OR);
                addPartToList(part, parts);
                pos += 2;
            } else if (chars[pos] == ' ') {
                pos++;
            } else {
                throw new IllegalArgumentException("Parsing error");
            }
        }
        return openBrackets(parts);
    }

    private static void addPartToList(RegExpPart part, List<RegExpPart> list) throws IllegalArgumentException {
        switch (part.getType()) {
            case BRACKETS: {
                if (list.isEmpty()) {
                    list.add(part);
                    return;
                }
                RegExpPart lastItem = list.get(list.size() - 1);
                if (lastItem.getType() == PartType.BRACKETS) {
                    throw new IllegalArgumentException("Brackets cannot be after brackets");
                }
                if (lastItem.getType() == PartType.KEYWORD) {
                    throw new IllegalArgumentException("Brackets cannot be after keyword");
                }
                break;
            }
            case KEYWORD: {
                if (list.isEmpty()) {
                    list.add(part);
                    return;
                }
                RegExpPart lastItem = list.get(list.size() - 1);
                if (lastItem.getType() == PartType.BRACKETS) {
                    throw new IllegalArgumentException("Keyword cannot be after brackets");
                }
                if (lastItem.getType() == PartType.KEYWORD) {
                    throw new IllegalArgumentException("Keyword cannot be after keyword");
                }
                break;
            }
            case OR: {
                if (list.isEmpty()) {
                    throw new IllegalArgumentException("OR cannot be the first element");
                }
                RegExpPart lastItem = list.get(list.size() - 1);
                if (lastItem.getType() == PartType.OR) {
                    throw new IllegalArgumentException("OR cannot be after OR");
                }
                if (lastItem.getType() == PartType.AND) {
                    throw new IllegalArgumentException("OR cannot be after AND");
                }
                break;
            }
            case AND: {
                if (list.isEmpty()) {
                    throw new IllegalArgumentException("AND cannot be the first element");
                }
                RegExpPart lastItem = list.get(list.size() - 1);
                if (lastItem.getType() == PartType.OR) {
                    throw new IllegalArgumentException("AND cannot be after OR");
                }
                if (lastItem.getType() == PartType.AND) {
                    throw new IllegalArgumentException("AND cannot be after AND");
                }
                break;
            }
        }
        list.add(part);
    }

    private static List<RegExpPart> openBrackets(List<RegExpPart> parts) {
        List<RegExpPart> result = new ArrayList<>();
        List<RegExpPart> temp = new ArrayList<>();
        for (RegExpPart part : parts) {
            switch (part.getType()) {
                case BRACKETS: {
                    String val = part.getValue();
                    List<RegExpPart> openParts = toParts(val.substring(1, val.length() - 1).trim());
                    if (temp.isEmpty() || openParts.size() == 1) {
                        temp.addAll(openParts);
                    } else {
                        // remove last AND from temp
                        temp.remove(temp.size() - 1);
                        temp = multiply(temp, openParts);
                    }
                    break;
                }
                case KEYWORD: {
                    if (temp.isEmpty()) {
                        temp.add(part);
                    } else {
                        // remove last AND from temp
                        temp.remove(temp.size() - 1);
                        temp = multiply(temp, List.of(part));
                    }
                    break;
                }
                case OR: {
                    result.addAll(temp);
                    result.add(part);
                    temp.clear();
                    break;
                }
                case AND: {
                    temp.add(part);
                    break;
                }
            }
        }
        result.addAll(temp);
        return result;
    }


    // 'Executive' or 'Club'  X  'room1' or 'room2'
    // 'Executive' and 'room1' or 'Club' and 'room1' or 'Executive' and 'room1' or 'Club' and 'room1'
    private static List<RegExpPart> multiply(List<RegExpPart> parts1, List<RegExpPart> parts2) {
        final RegExpPart andPart = new RegExpPart("and", PartType.AND);
        final RegExpPart orPart = new RegExpPart("or", PartType.OR);

        List<RegExpPart> result = new ArrayList<>();
        List<RegExpPart> temp1 = new ArrayList<>();
        List<RegExpPart> temp2 = new ArrayList<>();
        for (RegExpPart part1 : parts1) {
            switch (part1.getType()) {
                case AND: {
                    temp1.add(part1);
                    break;
                }
                case KEYWORD: {
                    temp1.add(part1);
                    if (parts1.indexOf(part1) != parts1.size() - 1) {
                        break;
                    }
                }
                case OR: {
                    for (RegExpPart part2 : parts2) {
                        switch (part2.getType()) {
                            case AND: {
                                temp2.add(part2);
                                break;
                            }
                            case KEYWORD: {
                                temp2.add(part2);
                                if (parts2.indexOf(part2) != parts2.size() - 1) {
                                    break;
                                }
                            }
                            case OR: {
                                if (!result.isEmpty()) {
                                    result.add(orPart);
                                }
                                result.addAll(temp1);
                                result.add(andPart);
                                result.addAll(temp2);
                                temp2.clear();
                                break;
                            }
                            case BRACKETS: {
                                log.error("Error in openBrackets logic");
                            }
                        }
                    }
                    temp1.clear();
                    break;
                }
                case BRACKETS: {
                    log.error("Error in openBrackets logic");
                }
            }
        }
        return result;
    }
}

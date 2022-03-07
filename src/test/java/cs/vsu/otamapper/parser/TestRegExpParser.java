package cs.vsu.otamapper.parser;

import cs.vsu.otamapper.parser.term.BaseParentTerm;
import cs.vsu.otamapper.parser.term.RegExpTerm;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRegExpParser {

    @Test
    public void testParsing() {
        Map<String, String> regExpToResult = new HashMap<>() {{
            put("IF 'Club' THEN 1",
                    "'club' -> 1");

            put("IF ('Club') THEN 2",
                    "'club' -> 2");

            put("IF 'Club' OR 'Room' THEN 3",
                    "or 'club' 'room' -> 3");

            put("IF 'Club' AND 'Room' THEN 4",
                    "and 'club' 'room' -> 4");

            put("IF 'Club' OR 'Room' OR 'Executive' THEN 5",
                    "or or 'club' 'room' 'executive' -> 5");

            put("IF ('Club' OR 'Room') OR 'Executive' THEN 6",
                    "or or 'club' 'room' 'executive' -> 6");

            put("IF 'Club' AND 'Room' AND 'Executive' THEN 7",
                    "and and 'club' 'room' 'executive' -> 7");

            put("IF ('Club' AND 'Room') AND 'Executive' THEN 8",
                    "and and 'club' 'room' 'executive' -> 8");

            put("IF ('Executive' AND 'Club') OR 'room' THEN 9",
                    "or and 'executive' 'club' 'room' -> 9");

            put("IF ('Executive' OR 'Club') AND 'Room' THEN 10",
                    "or and 'executive' 'room' and 'club' 'room' -> 10");


            put("IF ('Executive' OR 'Club') AND ('Room' OR 'King') THEN 11",
                    // "or or or and 'executive' 'room' and 'executive' 'king' and 'club' 'room' and 'club' 'king' -> 11");
                    "or or and 'executive' 'room' or and 'executive' 'king' and 'club' 'room' and 'club' 'king' -> 11");
        }};

        for (Map.Entry<String, String> pair : regExpToResult.entrySet()) {
            RegExp regExp = Parser.parseRegExp(pair.getKey());
            String result = buildRegExpTree(regExp, new StringBuilder());
            assertEquals(pair.getValue().toLowerCase(), result.toLowerCase());
        }
    }

    private String buildRegExpTree(RegExp regExp, StringBuilder sb) {
        RegExpTerm term = regExp.getParentTerm();
        sb.append(term.toString());
        if (term instanceof BaseParentTerm) {
            BaseParentTerm parent = (BaseParentTerm) term;
            if (parent.getLeft() != null) {
                append(parent.getLeft(), sb);
            }
            if (parent.getRight() != null) {
                append(parent.getRight(), sb);
            }
        }
        sb.append(" -> ").append(regExp.getCode());
        return sb.toString();
    }

    private void append(RegExpTerm term, StringBuilder sb) {
        sb.append(" ").append(term.toString());
        if (term instanceof BaseParentTerm) {
            BaseParentTerm parent = (BaseParentTerm) term;
            if (parent.getLeft() != null) {
                append(parent.getLeft(), sb);
            }
            if (parent.getRight() != null) {
                append(parent.getRight(), sb);
            }
        }
    }
}

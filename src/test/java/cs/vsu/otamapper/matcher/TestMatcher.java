package cs.vsu.otamapper.matcher;

import cs.vsu.otamapper.mapper.Matcher;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMatcher {

    @Test
    public void testPositiveMatches() {
        Map<String, String> paramValueToRegExp = new HashMap<>() {{
            put("IF 'Club' THEN 1",
                    "club");

            put("IF ('Club') THEN 2",
                    "club");

            put("IF 'Club' OR 'Room' THEN 3",
                    "Big room");

            put("IF 'Club' AND 'Room' THEN 4",
                    "Club room");

            put("IF 'Club' OR 'Room' OR 'Executive' THEN 5",
                    "Executive room");

            put("IF ('Club' OR 'Room') OR 'Executive' THEN 6",
                    "Club");

            put("IF 'Club' AND 'Room' AND 'Executive' THEN 7",
                    "Executive club room");

            put("IF ('Club' AND 'Room') AND 'Executive' THEN 8",
                    "room Executive club");

            put("IF ('Executive' AND 'Club') OR 'room' THEN 9",
                    "Executive club");

            put("IF ('Executive' OR 'Club') AND 'Room' THEN 10",
                    "Executive room");

            put("IF ('Executive' OR 'Club') AND ('Room' OR 'King') THEN 11",
                    "King club");
        }};

        for (Map.Entry<String, String> pair : paramValueToRegExp.entrySet()) {
            boolean result = Matcher.matches(pair.getValue(), pair.getKey());
            assertTrue(result);
        }
    }

    @Test
    public void testNegativeMatches() {
        Map<String, String> paramValueToRegExp = new HashMap<>() {{
            put("IF 'Club' THEN 1",
                    "room");

            put("IF 'Club' OR 'Room' THEN 3",
                    "Big");

            put("IF 'Club' AND 'Room' THEN 4",
                    "Club");

            put("IF 'Club' OR 'Room' OR 'Executive' THEN 5",
                    "Big");

            put("IF 'Club' AND 'Room' AND 'Executive' THEN 7",
                    "Executive room");

            put("IF ('Executive' AND 'Club') OR 'room' THEN 9",
                    "Executive");

            put("IF ('Executive' OR 'Club') AND 'Room' THEN 10",
                    "Executive club");

            put("IF ('Executive' OR 'Club') AND ('Room' OR 'King') THEN 11",
                    "King room");
        }};

        for (Map.Entry<String, String> pair : paramValueToRegExp.entrySet()) {
            boolean result = Matcher.matches(pair.getValue(), pair.getKey());
            assertFalse(result);
        }
    }
}
package cs.vsu.otamapper.parser;

import lombok.Data;

@Data
public class RegExpPart {

    private String value;
    private Parser.PartType type;

    public RegExpPart(String value, Parser.PartType type) {
        this.value = value;
        this.type = type;
    }
}

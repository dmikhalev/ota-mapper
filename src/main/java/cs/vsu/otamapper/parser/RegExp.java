package cs.vsu.otamapper.parser;

import cs.vsu.otamapper.parser.term.RegExpTerm;
import lombok.Data;

@Data
public class RegExp {
    private RegExpTerm parentTerm;
    private Integer code;

    public RegExp(RegExpTerm parentTerm, Integer code) {
        this.parentTerm = parentTerm;
        this.code = code;
    }
}

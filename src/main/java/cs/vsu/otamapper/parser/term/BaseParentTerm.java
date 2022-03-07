package cs.vsu.otamapper.parser.term;

import lombok.Data;

@Data
public class BaseParentTerm {

    protected RegExpTerm left;
    protected RegExpTerm right;
}

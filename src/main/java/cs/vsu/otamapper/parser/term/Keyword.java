package cs.vsu.otamapper.parser.term;

import lombok.Data;

@Data
public class Keyword implements RegExpTerm {

    private String value;

    public Keyword(String value) {
        this.value = value;
    }

    @Override
    public boolean execute(String paramValue) {
        return paramValue.contains(value.replace("'", ""));
    }

    @Override
    public String toString() {
        return value;
    }
}

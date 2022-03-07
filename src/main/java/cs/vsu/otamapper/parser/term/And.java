package cs.vsu.otamapper.parser.term;

public class And extends BaseParentTerm implements RegExpTerm {

    @Override
    public boolean execute(String paramValue) {
        return left.execute(paramValue) && right.execute(paramValue);
    }

    @Override
    public String toString() {
        return "AND";
    }
}

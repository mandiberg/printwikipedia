package info.bliki.wiki.filter;

/**
 * A pair of <code>String's</code> used for the creation of "table of
 * contents" (TOC)
 *
 */
public class StringPair {
    protected String fFirst;

    protected String fSecond;

    public StringPair(String one, String two) {
        this.fFirst = one;
        this.fSecond = two;
    }

    public String getFirst() {
        return fFirst;
    }

    public void setFirst(String one) {
        this.fFirst = one;
    }

    public String getSecond() {
        return fSecond;
    }

    public void setSecond(String two) {
        this.fSecond = two;
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof StringPair) {
            return ((StringPair) arg0).fFirst.equals(fFirst) && ((StringPair) arg0).fSecond.equals(fSecond);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return fFirst.hashCode();
    }

    @Override
    public String toString() {
        return "[" + fFirst + "], [" + fSecond + "]";
    }
}

package info.bliki.wiki.filter;

/**
 * A pair of <code>String's</code> used for the creation of "table of contents" (TOC)
 *
 */
public class SectionHeader extends StringPair {
    private final int fLevel;
    private final int fStartPosition;
    private final int fEndPosition;

    public SectionHeader(int level, int startPosition, int endPosition, String one, String two) {
        super(one, two);
        this.fLevel = level;
        this.fStartPosition = startPosition;
        this.fEndPosition = endPosition;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + fEndPosition;
        result = prime * result + fLevel;
        result = prime * result + fStartPosition;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        SectionHeader other = (SectionHeader) obj;
        if (fEndPosition != other.fEndPosition)
            return false;
        if (fLevel != other.fLevel)
            return false;
        if (fStartPosition != other.fStartPosition)
            return false;
        return true;
    }

}

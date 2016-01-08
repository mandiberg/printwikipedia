package info.bliki.wiki.model;

public class Counter {
    private int fValue;

    public Counter() {
        fValue = 0;
    }

    public Counter(int value) {
        fValue = value;
    }

    public int inc() {
        return ++fValue;
    }

    public int dec() {
        return --fValue;
    }

    public int getCounter() {
        return fValue;
    }
}

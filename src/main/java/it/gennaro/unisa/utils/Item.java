package it.gennaro.unisa.utils;

import java.math.BigInteger;

public class Item {
    private final String name;
    private final long val1;
    private final long val2;

    public Item(String name, BigInteger val1, BigInteger val2) {
        this.name = name;
        this.val1 = val1.intValue();
        this.val2 = val2.intValue();
    }

    public String getName() {
        return name;
    }
    public long getVal1() {
        return val1;
    }
    public long getVal2() {
        return val2;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", val1=" + val1 +
                ", val2=" + val2 +
                '}';
    }
}

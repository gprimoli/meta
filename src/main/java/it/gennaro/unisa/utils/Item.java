package it.gennaro.unisa.utils;

import java.math.BigInteger;

public class Item {
    private final String name;
    private final BigInteger val1;
    private final BigInteger val2;

    public Item(String name, BigInteger val1, BigInteger val2) {
        this.name = name;
        this.val1 = val1;
        this.val2 = val2;
    }

    public String getName() {
        return name;
    }
    public BigInteger getVal1() {
        return val1;
    }
    public BigInteger getVal2() {
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

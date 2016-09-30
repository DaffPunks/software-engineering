package com.god.damn;

public enum Permisions {
    READ(1),
    WRITE(2),
    EXECUTE(4);

    private int value;

    Permisions(int value) {
        this.value = value;
    }

    public int code() {
        return this.value;
    }
}

package com.Astar.type;

public enum TransferType {
    SERVER("SERVER"),
    CLIENT("CLIENT");

    private String type;

    TransferType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

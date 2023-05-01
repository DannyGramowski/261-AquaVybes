package com.estore.api.estoreapi.model.aqua;

public enum PackSize {
    NOTVALID(0),
    SINGLE(1),
    SIX(6),
    EIGHT(8),
    TWELVE(12);

    int size;
    PackSize(int newSize) {
        size = newSize;
    }

    public int getSize() {
        return size;
    }

    public static PackSize deserialize(String str) {
        switch(str) {
            case "SINGLE":
                return PackSize.SINGLE;
            case "SIX":
                return PackSize.SIX;
            case "EIGHT":
                return PackSize.EIGHT;
            case "TWELVE":
                return PackSize.TWELVE;
            default:
                return PackSize.NOTVALID;
        }
    }
}

package com.estore.api.estoreapi.services;

public class FlavorNotFound extends RuntimeException {
    private int id;

    public FlavorNotFound(int id) {
        super(String.format("id, %d, not found", id));
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}

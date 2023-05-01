package com.estore.api.estoreapi.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.estore.api.estoreapi.model.flavor.Flavor;

public interface FlavorService {
    public abstract ArrayList<Flavor> getFlavors(String flavorCode) throws IOException, FlavorNotFound;
}

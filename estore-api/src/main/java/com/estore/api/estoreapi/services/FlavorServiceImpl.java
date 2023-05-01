package com.estore.api.estoreapi.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.persistence.flavor.FlavorDAO;

@Service
public class FlavorServiceImpl implements FlavorService {
    private final FlavorDAO flavorDAO;

    public FlavorServiceImpl(FlavorDAO flavorDAO) {
        this.flavorDAO = flavorDAO;
    }

    @Override
    public ArrayList<Flavor> getFlavors(String flavorCode) throws IOException, FlavorNotFound {
        ArrayList<Flavor> flavors = new ArrayList<>();
        
        Pattern.compile("-")
                .splitAsStream(flavorCode)
                    .map(str -> Integer.parseInt(str))
                    .map(i -> {
                        try {
                            return flavorDAO.getFlavor(i);
                        } catch (IOException e) {
                            throw new FlavorNotFound(i);
                        }
                    }).forEach(flavor -> flavors.add(flavor));

        return flavors;
    }
}

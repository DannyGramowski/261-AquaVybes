
package com.estore.api.estoreapi.model.aqua ;

import java.util.ArrayList;
import com.estore.api.estoreapi.model.flavor.Flavor;

public class AquaValidateData {
    public ArrayList<Flavor> flavors;
    public PackSize packSize;

    public AquaValidateData(ArrayList<Flavor> flavors, PackSize packSize) {
        this.flavors = flavors;
        this.packSize = packSize;
    }

    @Override public String toString() {
        return flavors.toString() + ", " + packSize;
    }
}

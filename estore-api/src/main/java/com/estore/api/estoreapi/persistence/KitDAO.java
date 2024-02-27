package com.estore.api.estoreapi.persistence;
import java.io.IOException;

import com.estore.api.estoreapi.model.kit;

public interface KitDAO {
    kit CreateKit() throws IOException;

    kit[] getKits() throws IOException;

    kit[] findKits(String containsText) throws IOException;

    kit updaKit(kit kit) throws IOException;

    kit getKit(int id) throws IOException;

    boolean deleteKit(int id) throws IOException;


}

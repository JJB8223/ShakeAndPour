package com.estore.api.estoreapi.persistence;
import java.io.IOException;

import com.estore.api.estoreapi.model.kit;

public interface KitDAO {
    kit createKit(kit kit) throws IOException;

    kit[] getKits() throws IOException;

    kit[] findKits(String containsText) throws IOException;

    kit updateKit(kit kit) throws IOException;

    kit getKit(int id) throws IOException;

    boolean deleteKit(int id) throws IOException;


}

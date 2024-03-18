package com.estore.api.estoreapi.persistence;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Kit;

/**
 * Defines an interface for a KitDAO
 * @author Akhil Devarapalli
 */

public interface KitDAO {
    Kit createKit(Kit kit) throws IOException;

    Kit[] getKits() throws IOException;

    Kit[] findKits(String containsText) throws IOException;

    Kit updateKit(Kit kit) throws IOException;

    Kit getKit(int id) throws IOException;

    boolean deleteKit(int id) throws IOException;


}

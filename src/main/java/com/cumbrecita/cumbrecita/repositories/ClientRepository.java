

package com.cumbrecita.cumbrecita.repositories;

import com.cumbrecita.cumbrecita.entities.Client;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    
    @Query("SELECT c FROM Client c WHERE c.name = :name")
    public ArrayList<Client> searchByName(@Param("name") String name);
}

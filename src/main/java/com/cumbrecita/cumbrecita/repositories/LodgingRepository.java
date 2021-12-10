

package com.cumbrecita.cumbrecita.repositories;

import com.cumbrecita.cumbrecita.entities.Lodging;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LodgingRepository extends JpaRepository<Lodging, String> {
    
     @Query("SELECT c FROM Lodging c WHERE c.lastname = :lastname")
    public ArrayList<Lodging> searchByLastName(@Param("lastname") String lastname);
}

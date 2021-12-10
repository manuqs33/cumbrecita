

package com.cumbrecita.cumbrecita.repositories;

import com.cumbrecita.cumbrecita.entities.Owner;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, String>{
    @Query("SELECT c FROM Owner c WHERE c.lastname = :lastname")
    public ArrayList<Owner> searchByLastName(@Param("lastname") String lastname);
    
    @Query("SELECT c FROM Owner c WHERE c.mail = :mail")
    public Owner searchByEmail(@Param("mail")String mail);
}

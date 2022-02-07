package com.cumbrecita.cumbrecita.repositories;

import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.enumc.Type;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LodgingRepository extends JpaRepository<Lodging, String> {

    @Query("SELECT c FROM Lodging c WHERE c.name = :name")
    public List<Lodging> searchByName(@Param("name") String name);

    @Query("SELECT p FROM Lodging p WHERE p.name LIKE :q OR p.capacity LIKE :q")
    public List<Lodging> findByQ(@Param("q") String q);

    @Query("SELECT l FROM Lodging l WHERE l.id = :id")
    public Lodging getById(@Param("id") String id);

    @Query("SELECT l FROM Lodging l WHERE l.o.id = :id")
    public List<Lodging> searchByOwner(@Param("id") String id);

    @Query("SELECT l FROM Lodging l WHERE l.t = :type AND l.capacity >= :capacity")
    public List<Lodging> searchQuery(@Param("type") Type type, @Param("capacity") Integer capacity);
}

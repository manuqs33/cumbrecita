package com.cumbrecita.cumbrecita.repositories;

import com.cumbrecita.cumbrecita.entities.Reservation;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @Query("SELECT c FROM Reservation c WHERE c.client.id = :client")
    public ArrayList<Reservation> searchClient(@Param("client") String client);
}

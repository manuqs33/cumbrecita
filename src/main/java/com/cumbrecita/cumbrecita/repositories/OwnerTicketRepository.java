/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.repositories;

import com.cumbrecita.cumbrecita.entities.ClientTicket;
import com.cumbrecita.cumbrecita.entities.OwnerTicket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Flia Vasquez
 */
@Repository
public interface OwnerTicketRepository extends JpaRepository<OwnerTicket, String> {
    
    @Query("SELECT t FROM OwnerTicket t WHERE t.owner.id = :id")
    public List<OwnerTicket> showMyTickets(@Param("id") String id);
    
    
    
}

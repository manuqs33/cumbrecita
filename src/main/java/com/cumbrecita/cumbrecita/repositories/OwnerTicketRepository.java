/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.repositories;

import com.cumbrecita.cumbrecita.entities.OwnerTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Flia Vasquez
 */
@Repository
public interface OwnerTicketRepository extends JpaRepository<OwnerTicket, String> {
    
}

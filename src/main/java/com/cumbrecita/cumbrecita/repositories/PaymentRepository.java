

package com.cumbrecita.cumbrecita.repositories;


import com.cumbrecita.cumbrecita.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>{
 
}

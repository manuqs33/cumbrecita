/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.services;

import com.cumbrecita.cumbrecita.entities.ClientTicket;
import com.cumbrecita.cumbrecita.entities.Payment;
import com.cumbrecita.cumbrecita.entities.Reservation;
import com.cumbrecita.cumbrecita.entities.TicketAnswer;
import com.cumbrecita.cumbrecita.repositories.ClientTicketRepository;
import com.cumbrecita.cumbrecita.repositories.TicketAnswerRepository;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 *
 * @author Flia Vasquez
 */
@Service
public class ClientTicketService {
    
    private ClientTicketRepository ctr;
    private TicketAnswerRepository tar;
    
    @Transactional
    public void newTicket(Reservation r, Payment t, String subject, String text) throws ErrorService{
        validate(subject, text);
        
        ClientTicket ct = new ClientTicket();
        
        ct.setReservation(r);
        ct.setPayment(t);
        ct.setInitDate(new Date());
        ct.setIsactive(true);
        ct.setSubject(subject);
        ct.setText(text);
        
        ctr.save(ct);
    }
    
    @Transactional
    public void answerTicket(String content) throws ErrorService{
        if (content == null || content.equals("") || content.equals(" ")) {
            throw new ErrorService("La respuesta no puede estar vacia");
        }
        
        TicketAnswer ta = new TicketAnswer();
        
        ta.setContent(content);
        
        tar.save(ta);
    }
    
    @Transactional
    public void closeTicket(String id) throws ErrorService{
        
        Optional<ClientTicket> ans = ctr.findById(id);
        if (ans.isPresent()) {
            ClientTicket ct = ans.get();
            
            ct.setIsactive(false);
            
            ctr.save(ct);
        }else{
            throw new ErrorService("Hubo un problema al cerrar el ticket");
        }
        
    }
    
    public void validate(String subject, String text) throws ErrorService{
        if (subject == null || subject.equals(" ") || subject.equals("")) {
            throw new ErrorService("El asunto no puede estar vacio.");
        }
        if (subject.length() < 4 || subject.length() > 20) {
            throw new ErrorService("Recuerde que debe ser entre 4 y 20 caracteres.");
        }
        if (text == null || text.equals(" ") || text.equals("")) {
            throw new ErrorService("El texto no puede estar vacio.");
        }
        if (text.length() < 4 || text.length() > 500) {
            throw new ErrorService("Recuerde que debe ser entre 4 y 500 caracteres.");
        }
    }
    
}

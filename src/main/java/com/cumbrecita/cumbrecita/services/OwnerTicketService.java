/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.services;

import com.cumbrecita.cumbrecita.entities.Lodging;
import com.cumbrecita.cumbrecita.entities.OwnerTicket;
import com.cumbrecita.cumbrecita.entities.Photo;
import com.cumbrecita.cumbrecita.entities.Reservation;
import com.cumbrecita.cumbrecita.entities.TicketAnswer;
import com.cumbrecita.cumbrecita.repositories.OwnerTicketRepository;
import com.cumbrecita.cumbrecita.repositories.TicketAnswerRepository;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Flia Vasquez
 */
@Service
public class OwnerTicketService {
    
    @Autowired
    private OwnerTicketRepository otr;
    @Autowired
    private TicketAnswerRepository tar;
    @Autowired
    private PhotoService photoService;
    
    @Transactional
    public void newTicket(Lodging lodging, String subject, String text, MultipartFile file) throws ErrorService{//Photo photo
        validate(subject, text);
        
        OwnerTicket ot = new OwnerTicket();
        
        ot.setLodging(lodging);
        ot.setInitDate(new Date());
        ot.setIsactive(true);
        ot.setSubject(subject);
        ot.setText(text); 
        
        Photo photo = photoService.save(file);
        ot.setPhoto(photo);
        
        otr.save(ot);
    }
    
    @Transactional
    public void answerTicket(String content, MultipartFile file, String idTicket) throws ErrorService{
        if (content == null || content.equals("") || content.equals(" ")) {
            throw new ErrorService("La respuesta no puede estar vacia");
        }
        
        TicketAnswer ta = new TicketAnswer();
        
        OwnerTicket ot = otr.getById(idTicket);
        
        ta.setContent(content);
        
        Photo photo = photoService.save(file);
        ta.setPhoto(photo);
        
        ot.getTicketAnswer().add(ta);
        
        tar.save(ta);
    }
    
    @Transactional
    public void closeTicket(String id) throws ErrorService{
        
        Optional<OwnerTicket> ans = otr.findById(id);
        if (ans.isPresent()) {
            OwnerTicket ot = ans.get();
            ot.setIsactive(false);
            otr.save(ot);
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

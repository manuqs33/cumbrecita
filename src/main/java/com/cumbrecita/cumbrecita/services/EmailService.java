/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 *
 * @author Flia Vasquez
 */
@Service
public class EmailService{
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    public void send(String to, String body, String subject) throws ErrorService{
        
        validate(body, subject);
        
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setFrom("noreply.cumbrecita@gmail.com");
        message.setTo(to);
        message.setText(body);
        message.setSubject(subject);
        
        javaMailSender.send(message);
    }
    
    public void validate(String body, String subject) throws ErrorService{
        if (subject.length() < 6) {
            throw new ErrorService("EL asunto del mensaje no puede contener menos de 6 caracteres.");
        }
        if (body.length() < 20) {
            throw new ErrorService("El mensaje es muy corto, le pedimos por favor sea mas detallado.");
        }
    }
    
}

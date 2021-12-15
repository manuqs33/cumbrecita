/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Flia Vasquez
 */
@Entity
public class OwnerTicket {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    
    @OneToOne
    private Lodging lodging;
    @OneToOne
    private Reservation resevation;
    @OneToOne
    private Payment payment;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date initDate;
    
    private Boolean isactive;
    private String subject;
    private String text;
    private Photo photo;
    @OneToMany
    private TicketAnswer ticketAnswer;

    public OwnerTicket() {
    }
    
    public OwnerTicket(String id, Lodging lodging, Reservation resevation, Payment payment, Date initDate, Boolean isactive ) {
        this.id = id;
        this.lodging = lodging;
        this.resevation = resevation;
        this.payment = payment;
        this.initDate = initDate;
        this.isactive = isactive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Lodging getLodging() {
        return lodging;
    }

    public void setLodging(Lodging lodging) {
        this.lodging = lodging;
    }

    public Reservation getResevation() {
        return resevation;
    }

    public void setResevation(Reservation resevation) {
        this.resevation = resevation;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TicketAnswer getTicketAnswer() {
        return ticketAnswer;
    }

    public void setTicketAnswer(TicketAnswer ticketAnswer) {
        this.ticketAnswer = ticketAnswer;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumbrecita.cumbrecita.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class ClientTicket implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @OneToOne
    private Client client;
    @OneToOne
    private Reservation reservation;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date initDate;

    private Boolean isactive;
    private String subject;
    private String text;
    
    @OneToOne
    private Photo photo;
    @OneToMany
    private List<TicketAnswer> ticketAnswer;

    public ClientTicket(String id, Reservation reservation, Date initDate, Boolean isactive) {
        this.id = id;
        this.reservation = reservation;
        this.initDate = initDate;
        this.isactive = isactive;
    }

    public ClientTicket() {
        this.isactive = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
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

    public List<TicketAnswer> getTicketAnswer() {
        return ticketAnswer;
    }

    public void setTicketAnswer(List<TicketAnswer> ticketAnswer) {
        this.ticketAnswer = ticketAnswer;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}

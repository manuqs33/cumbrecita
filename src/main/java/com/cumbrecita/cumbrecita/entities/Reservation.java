

package com.cumbrecita.cumbrecita.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    @OneToOne
    private Client c;
    @OneToOne
    private Lodging l;
    private String observations;
    private Float price;

    public Reservation() {
    }

    public Reservation(String id, Date startDate, Date endDate, Client c, Lodging l, String observations, Float price) {
        this.id = id;


        this.startDate = startDate;
        this.endDate = endDate;

        this.c = c;
        this.l = l;
        this.observations = observations;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date start) {
        this.startDate = start;

    }

    public Date getEndDate() {
        return endDate;
    }


    public void setEndDate(Date end) {
        this.endDate = end;

    }

    public Client getC() {
        return c;
    }

    public void setC(Client c) {
        this.c = c;
    }

    public Lodging getL() {
        return l;
    }

    public void setL(Lodging l) {
        this.l = l;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
    
    
}

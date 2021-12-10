

package com.cumbrecita.cumbrecita.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Payment {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @OneToOne
    private Reservation r;
    private Double price;

    public Payment() {
    }

    public Payment(String id, Reservation r, Double price) {
        this.id = id;
        this.r = r;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Reservation getR() {
        return r;
    }

    public void setR(Reservation r) {
        this.r = r;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    
    
}

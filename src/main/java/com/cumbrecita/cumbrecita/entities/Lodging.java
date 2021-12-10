

package com.cumbrecita.cumbrecita.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

enum Type
{
    house, room;
}

@Entity
public class Lodging {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    private Boolean isactive;
    private String name;
    private String address;
    private Type t;
    private Integer capacity;
    private Double pricepernight;
    @ManyToOne
    private Owner o;

    public Lodging() {
    }

    public Lodging(String id, Boolean isactive, String name, String address, Type t, Integer capacity, Double pricepernight, Owner o) {
        this.id = id;
        this.isactive = isactive;
        this.name = name;
        this.address = address;
        this.t = t;
        this.capacity = capacity;
        this.pricepernight = pricepernight;
        this.o = o;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Type getT() {
        return t;
    }

    public void setT(Type t) {
        this.t = t;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getPricepernight() {
        return pricepernight;
    }

    public void setPricepernight(Double pricepernight) {
        this.pricepernight = pricepernight;
    }

    public Owner getO() {
        return o;
    }

    public void setO(Owner o) {
        this.o = o;
    }
    
    
}

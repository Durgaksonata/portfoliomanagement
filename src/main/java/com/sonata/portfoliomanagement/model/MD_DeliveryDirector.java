package com.sonata.portfoliomanagement.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "md_delivery_director")
public class MD_DeliveryDirector {

    public MD_DeliveryDirector(Integer id, String delivery_Director) {
        super();
        this.id = id;
        deliveryDirector = delivery_Director;
    }
    public MD_DeliveryDirector() {}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="Delivery_Director")
    private String deliveryDirector;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeliveryDirector() {
        return deliveryDirector;
    }

    public void setDeliveryDirector(String deliveryDirector) {
        this.deliveryDirector = deliveryDirector;
    }
}
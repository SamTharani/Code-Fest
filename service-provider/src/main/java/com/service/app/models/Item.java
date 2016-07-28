package com.service.app.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Item {

    @Id
    private int id;

    private String customerId;

    private String category;

    private Date  purchasedDate;

    public Item(int id, String customerId, String category, Date purchasedDate) {
        this.id = id;
        this.customerId = customerId;
        this.category = category;
        this.purchasedDate = purchasedDate;
    }

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(Date purchasedDate) {
        this.purchasedDate = purchasedDate;
    }
}

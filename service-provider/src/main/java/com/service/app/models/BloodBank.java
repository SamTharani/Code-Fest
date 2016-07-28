package com.service.app.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class BloodBank implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private int pinCode;
    private String bloodBankName;
    private String bloodBankLocation;
    private String sector;


    public BloodBank() {
    }

    public BloodBank(int pinCode, String bloodBankName, String bloodBankLocation, String sector) {
        this.pinCode = pinCode;
        this.bloodBankName = bloodBankName;
        this.bloodBankLocation = bloodBankLocation;
        this.sector = sector;
    }

    public String getBloodBankLocation() {
        return bloodBankLocation;
    }

    public void setBloodBankLocation(String bloodBankLocation) {
        this.bloodBankLocation = bloodBankLocation;
    }

    public String getBloodBankName() {
        return bloodBankName;
    }

    public void setBloodBankName(String bloodBankName) {
        this.bloodBankName = bloodBankName;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}

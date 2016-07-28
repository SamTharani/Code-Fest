package com.service.app.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class BloodDonors implements Serializable {
    //  private static final long serialVersionUID = 1L;
    @Id
    private int phoneNumber;
    private String donorName;
    private String bloodGroup;
    private String donorLocation;

    public BloodDonors() {
    }

    public BloodDonors(int phoneNumber, String donorName, String bloodGroup, String donorLocation) {
        this.phoneNumber = phoneNumber;
        this.donorName = donorName;
        this.bloodGroup = bloodGroup;
        this.donorLocation = donorLocation;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String blood_group) {
        this.bloodGroup = blood_group;
    }

    public String getDonorLocation() {
        return donorLocation;
    }

    public void setDonorLocation(String donorLocation) {
        this.donorLocation = donorLocation;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}

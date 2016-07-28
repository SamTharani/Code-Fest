package com.service.app.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class BloodBankDao {
    EntityManagerFactory emf;
    EntityManager em;

    public BloodBankDao() {
        this.emf = Persistence.createEntityManagerFactory("ServiceProviderJPAunit");

        this.em = emf.createEntityManager();
    }

    public java.util.List validPinCode(int pin) {
        Query query = em.createQuery("select pinCode from BloodBank where pinCode=" + pin + "");
        return query.getResultList();
    }

}

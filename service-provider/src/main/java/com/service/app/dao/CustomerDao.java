package com.service.app.dao;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CustomerDao {

    EntityManagerFactory emf;
    EntityManager em;

    public CustomerDao() {
        this.emf = Persistence.createEntityManagerFactory("ServiceProviderJPAunit");

        this.em = emf.createEntityManager();
    }



}

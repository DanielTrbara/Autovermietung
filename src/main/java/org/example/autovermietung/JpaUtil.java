package org.example.autovermietung;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("autovermietungPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
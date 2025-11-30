package org.example.autovermietung;

import jakarta.persistence.EntityManager;

import java.util.List;

public class CarRepository {

    public List<Car> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Car c", Car.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Car car) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(car);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
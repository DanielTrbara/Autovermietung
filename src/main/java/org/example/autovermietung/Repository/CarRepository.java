package org.example.autovermietung.Repository;

import jakarta.persistence.EntityManager;
import org.example.autovermietung.JpaUtil;
import org.example.autovermietung.Model.AddCar;

import java.util.List;

public class CarRepository {

    public List<AddCar> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM AddCar c", AddCar.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void save(AddCar addCar) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(addCar);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(AddCar addCar) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            AddCar managed = em.merge(addCar);
            em.remove(managed);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}

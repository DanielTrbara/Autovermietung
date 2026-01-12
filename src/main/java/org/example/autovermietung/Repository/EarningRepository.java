package org.example.autovermietung.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.example.autovermietung.JpaUtil;
import org.example.autovermietung.Model.Earning;

public class EarningRepository {
    // Alle Einnahmen aus der Datenbank abrufen
    public List<Earning> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Earning e", Earning.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Neue Einnahme speichern
    public void save(Earning earning) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(earning);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Einnahme löschen
    public void delete(Earning earning) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Earning managed = em.merge(earning); // sicherstellen, dass Entity managed ist
            em.remove(managed);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Optional: Einnahmen nach Auto filtern
    public List<Earning> findByCarId(int carId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Earning e WHERE e.carId = :carId", Earning.class)
                    .setParameter("carId", carId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
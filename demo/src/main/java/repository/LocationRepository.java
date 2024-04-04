package repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class LocationRepository {
    private EntityManagerFactory emf;

    public LocationRepository() {
        this.emf = Persistence.createEntityManagerFactory("your_persistence_unit_name_here");
    }

    public Location findByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Location> query = em.createQuery("SELECT l FROM Location l WHERE l.name = :name", Location.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
package repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class PlaceRepository {
    private EntityManagerFactory emf;

    public PlaceRepository() {
        this.emf = Persistence.createEntityManagerFactory("your_persistence_unit_name_here");
    }

    public List<Place> findByLocation(Location location) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Place> query = em.createQuery("SELECT p FROM Place p WHERE p.location = :location", Place.class);
            query.setParameter("location", location);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Place> findByCategory(Category category) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Place> query = em.createQuery("SELECT p FROM Place p WHERE p.category = :category", Place.class);
            query.setParameter("category", category);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

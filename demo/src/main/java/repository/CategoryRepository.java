package repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class CategoryRepository {
    private EntityManagerFactory emf;

    public CategoryRepository() {
        this.emf = Persistence.createEntityManagerFactory("your_persistence_unit_name_here");
    }

    public Category findByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}

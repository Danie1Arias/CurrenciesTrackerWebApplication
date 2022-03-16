package aar;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.logging.Logger;


/**
 * This class performs the insert, read, delete and search operations
 *
 *
 */
public class DatabaseService {

    Logger log = Logger.getLogger(DatabaseService.class.getName());


    public int insert(Currency coin) {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(coin);
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
           
            
        }
        return coin.getId();
    }

    public Currency read(int id) {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        Currency product = null;
        try {
            product = entityManager.find(Currency.class, id);
        } finally {
            entityManager.close();
           
            if (product == null) log.warning("No records records were found with given id value");

        }
        return product;
    }

    public boolean delete(int id) {
	    EntityManager entityManager = EntityManagerListener.createEntityManager();
        boolean result = false;
        try {
            entityManager.getTransaction().begin();
            Currency entity = null;
           
            entity = entityManager.find(Currency.class, id);
            if (entity != null) {
                entityManager.remove(entity);
                entityManager.getTransaction().commit();
                result = true;
            } else {
                log.warning("No records records were found with given id value !!");
                result = false;
            }
            
        } catch (Exception e) {
            result = false;
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
          
        }
        return result;
    }

    public List<Currency> search(String key, String value) {
	    EntityManager entityManager = EntityManagerListener.createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Currency> criteriaQuery = criteriaBuilder.createQuery(Currency.class);
            Root<Currency> root = criteriaQuery.from(Currency.class);
            criteriaQuery.select(root);
            Predicate where = criteriaBuilder.equal(root.get(key), value);
            criteriaQuery.where(where);
            return entityManager.createQuery(criteriaQuery).getResultList();
        } finally {
            entityManager.close();
           
        }
    }

    public List<Currency> findAll() {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Currency> criteriaQuery = criteriaBuilder.createQuery(Currency.class);
 
            Root<Currency> rootEntry = criteriaQuery.from(Currency.class);
            CriteriaQuery<Currency> all = criteriaQuery.select(rootEntry);
            TypedQuery<Currency> allQuery = entityManager.createQuery(all);
            return allQuery.getResultList();
        } finally {
            entityManager.close();
           
        }
    }
}

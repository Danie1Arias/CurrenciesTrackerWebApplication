package aar;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.logging.Logger;

public class DatabaseServiceExchange {

    Logger log = Logger.getLogger(DatabaseServiceExchange.class.getName());


    public int insert(Exchange exch) {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(exch);
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
           
            
        }
        return exch.getId();
    }

    public Exchange read(int id) {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        Exchange product = null;
        try {
            product = entityManager.find(Exchange.class, id);
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
            Exchange entity = null;
           
            entity = entityManager.find(Exchange.class, id);
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

    public List<Exchange> search(String key, String value) {
	    EntityManager entityManager = EntityManagerListener.createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Exchange> criteriaQuery = criteriaBuilder.createQuery(Exchange.class);
            Root<Exchange> root = criteriaQuery.from(Exchange.class);
            criteriaQuery.select(root);
            Predicate where = criteriaBuilder.equal(root.get(key), value);
            criteriaQuery.where(where);
            return entityManager.createQuery(criteriaQuery).getResultList();
        } finally {
            entityManager.close();
           
        }
    }

    public List<Exchange> findAll() {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Exchange> criteriaQuery = criteriaBuilder.createQuery(Exchange.class);
 
            Root<Exchange> rootEntry = criteriaQuery.from(Exchange.class);
            CriteriaQuery<Exchange> all = criteriaQuery.select(rootEntry);
            TypedQuery<Exchange> allQuery = entityManager.createQuery(all);
            return allQuery.getResultList();
        } finally {
            entityManager.close();
           
        }
    }
}

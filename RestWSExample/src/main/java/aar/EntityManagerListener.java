package aar;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class EntityManagerListener implements ServletContextListener{

    private static EntityManagerFactory entityManager;
    
    Logger log = Logger.getLogger(EntityManagerListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        entityManager = Persistence.createEntityManagerFactory("InMemH2DB");

        DatabaseService d = new DatabaseService();
        Currency coin = new Currency("usd");
        d.insert(coin);
        Currency coin2 = new Currency("eur");
        d.insert(coin2);
        Currency coin3 = new Currency("gbp");
        d.insert(coin3);
        Currency coin4 = new Currency("btc");
        d.insert(coin4);
        Currency coin5 = new Currency("eth");
        d.insert(coin5);
        Currency coin6 = new Currency("jpy");
        d.insert(coin6);
        Currency coin7 = new Currency("aud");
        d.insert(coin7);
        Currency coin8 = new Currency("cad");
        d.insert(coin8);
        Currency coin9 = new Currency("chf");
        d.insert(coin9);
        Currency coin10 = new Currency("mxn");
        d.insert(coin10);
        Currency coin11 = new Currency("inr");
        d.insert(coin11);
        Currency coin12 = new Currency("rub");
        d.insert(coin12);
                
        log.log(Level.INFO, "Initialized database correctly!");
    }
 
    @Override
    public void contextDestroyed(ServletContextEvent e) {
        entityManager.close();
        log.log(Level.INFO, "Destroying context.... tomcat stopping!");
    }

    public static EntityManager createEntityManager() {
        if (entityManager == null) {
            throw new IllegalStateException("Context is not initialized yet.");
        }

        return entityManager.createEntityManager();
    }
}
    

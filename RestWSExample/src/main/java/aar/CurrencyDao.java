package aar;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrencyDao {
	
	Logger log = Logger.getLogger(CurrencyDao.class.getName());

    final DatabaseService d = new DatabaseService();
	
    public int addCurrency(String name) {
        try {
            Currency coin = new Currency(name);
            d.insert(coin);
        } catch (Exception ex) {	
           log.log(Level.SEVERE, null, ex);
        }		
        return 1;
    }	
	
    public List<Currency> getAllCurrencies() {
    	try {
    		return d.findAll();
    	} catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
    	}
    	return null;
    }
   
    public Currency getCurrency(Integer id) {
        return d.read(id);
    }
   
    public boolean deleteCurrency(Integer id) {
    	return d.delete(id);
    }

}
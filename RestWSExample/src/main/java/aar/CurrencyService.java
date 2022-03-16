package aar;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/currencies")
public class CurrencyService {

   CurrencyDao CurrencyDao = new CurrencyDao();
   
   Logger log = Logger.getLogger(CurrencyService.class.getName());

   @GET
   @Path("/")
   @Produces(MediaType.APPLICATION_JSON)
   public List<Currency> getCurrenciesJson() {
	   return CurrencyDao.getAllCurrencies();
   }	
 
   @POST
   @Path("/")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes("application/x-www-form-urlencoded")
   public Currency addCurrency(@FormParam("name") String name) {
	   try {
		   CurrencyDao.addCurrency(name);
		   log.log(Level.INFO, "Inserted user "+name);
      
		   List<Currency> currencyDaoList = CurrencyDao.getAllCurrencies();
		   Currency currency = null;
		   
		   for(Currency c : currencyDaoList) {
			   if(c.getName().equals(name)) {
				   currency = c;
			   }
		   }
		   
		   return currency;
      } catch(Exception e) {
    	  return null;
      }
   }
   
   @DELETE
   @Path("/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response removeUser(@PathParam("id") Integer id) {
      try {
    	  boolean deletedOk = CurrencyDao.deleteCurrency(id);
      
    	  if(deletedOk == true) 
    		  log.log(Level.INFO, "deleted currency "+id+" correctly ");
      
    	  return Response.status(200).build();
      } catch(Exception e) {
    	  return Response.status(400).build();
      }
   }
     
}
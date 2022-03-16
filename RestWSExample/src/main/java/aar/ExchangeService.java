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
import org.json.*;

@Path("/exchange-rates")
public class ExchangeService {

   ExchangeDao ExchangeDao = new ExchangeDao();
   CurrencyDao CurrencyDao = new CurrencyDao();
   JSONObject myObject;
   Logger log = Logger.getLogger(ExchangeService.class.getName());

   @GET
   @Path("/")
   @Produces(MediaType.APPLICATION_JSON)
   public List<Exchange> getExchangesJson() {
	   return ExchangeDao.getAllExchanges();
   }	

   @GET
   @Path("/current/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Exchange getCurrentExchange(@PathParam("id") Integer id) {
	   Exchange exchange = ExchangeDao.updateExchange(id);
	   return exchange;
   }
   
   @GET
   @Path("/max/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public String getMaxExchange(@PathParam("id") Integer id) {
	   Exchange exchange = ExchangeDao.getExchange(id);
	   JSONObject JSONmessage = new JSONObject();
	   JSONmessage.put("date_exchange", exchange.getDate_max());
	   JSONmessage.put("price_max", String.valueOf(exchange.getMax()));
	   JSONmessage.put("id", id.toString());
	   return JSONmessage.toString();
   }
  
   @POST
   @Path("/")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes("application/x-www-form-urlencoded")
   public Exchange addExchange(@FormParam("coin1") String coin1, @FormParam("coin2") String coin2) {
	   try {
		   List<Currency> currencyDaoList = CurrencyDao.getAllCurrencies();
		   Currency first_currency = null, second_currency = null;
		   
		   for(Currency c : currencyDaoList) {
			   if(c.getName().equals(coin1)) {
				   first_currency = c;
			   }
			   if(c.getName().equals(coin2)) {
				   second_currency = c;
			   }
		   }
		   
		   ExchangeDao.addExchange(first_currency, second_currency);	
		   Exchange exchange = null;
		   List<Exchange> listExchanges = ExchangeDao.getAllExchanges();
		   for(Exchange ex : listExchanges) {
			   if(ex.getCoin1().getName().equals(coin1) && ex.getCoin2().getName().equals(coin2)) {
				   exchange = ex;
			   }
		   }
		   		   
		   log.log(Level.INFO, "Inserted exchange "+ coin1 + " and " + coin2);
		return 	exchange;   
      } catch(Exception e) {
    	  return null;
      }
   }
   
   @DELETE
   @Path("/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response removeExchange(@PathParam("id") Integer id) {
      try {
    	  boolean deletedOk = ExchangeDao.deleteExchange(id);
      
    	  if(deletedOk == true) 
    		  log.log(Level.INFO, "deleted exchange-rate "+id+" correctly ");
      
    	  return Response.status(200).build();
      } catch(Exception e) {
    	  return Response.status(400).build();
      }
   }
   
   
     
}
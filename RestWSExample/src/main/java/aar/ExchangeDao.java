package aar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jayway.jsonpath.JsonPath;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExchangeDao {
	
	Logger log = Logger.getLogger(ExchangeDao.class.getName());
	Exchange exchange;
    final DatabaseServiceExchange d2 = new DatabaseServiceExchange();
	
    public int addExchange(Currency coin1, Currency coin2) {
	    	String url = ("https://api.cryptonator.com/api/ticker/" + coin1.getName() + "-" + coin2.getName());
			String respuesta = "";
            exchange = new Exchange(coin1, coin2);
            try {
				respuesta = peticionHttpGet(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String price = JsonPath.read(respuesta, "$.ticker.price");
			double price_double = Double.parseDouble(price);
			exchange.setPrice(String.valueOf(price_double));
			exchange.setMax(String.valueOf(price_double));
			DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
			exchange.setDate_exchange(dtf5.format(LocalDateTime.now()).toString());
			exchange.setDate_max(dtf5.format(LocalDateTime.now()).toString());
            d2.insert(exchange);
               
        return 1;
    }
    
    public Exchange updateExchange(Integer identifier) {
        	Exchange exchange = d2.read(identifier);
        	Currency coin1 = exchange.getCoin1();
        	Currency coin2 = exchange.getCoin2();
        	String url = ("https://api.cryptonator.com/api/ticker/" + coin1.getName() + "-" + coin2.getName());
    		String respuesta = null;
			try {
				respuesta = peticionHttpGet(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		String price = JsonPath.read(respuesta, "$.ticker.price");
			double updated_price = Double.parseDouble(price);
			double previous_price = Double.parseDouble(exchange.getMax());
			DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
			exchange.setDate_exchange(dtf5.format(LocalDateTime.now()).toString());
			exchange.setPrice(String.valueOf(updated_price));
			
			if (previous_price < updated_price) {
				exchange.setMax(String.valueOf(updated_price));
				exchange.setDate_max(dtf5.format(LocalDateTime.now()).toString());
			}
			
    	return exchange;
    }
	
    public List<Exchange> getAllExchanges() {
    	try {
    		return d2.findAll();
    	} catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
    	}
    	return null;
    }
   
    public Exchange getExchange(Integer id) {
        try {
        	return d2.read(id);
    	} catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
    	}
    	return null;

    }
   
    public boolean deleteExchange(Integer id) {
    	return d2.delete(id);
    }
   
    private static String peticionHttpGet(String urlParaVisitar) throws Exception {
		StringBuilder resultado = new StringBuilder();
		URL url = new URL(urlParaVisitar);
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		String linea;

		while ((linea = rd.readLine()) != null) {
			resultado.append(linea);
		}

		rd.close();

		return resultado.toString();
	}
   
    
}
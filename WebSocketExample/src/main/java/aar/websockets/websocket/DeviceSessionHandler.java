package aar.websockets.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import aar.websockets.model.Device;
import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

@ApplicationScoped
public class DeviceSessionHandler {
    private final Set<Session> sessions = new HashSet<>();
    protected boolean UPDATING = false;
    private int timer_num = 0;
    
    public void addSession(Session session) {
        sessions.add(session);
        JsonProvider provider = JsonProvider.provider();
        String exchanges = petitionHttpGetExchanges();
        JSONArray jsonArr = new JSONArray(exchanges);
        
        for (int i = 0; i < jsonArr.length(); i++){
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            JsonObject addMessage = provider.createObjectBuilder()
                    .add("action", "add")
                    .add("id", jsonObj.getInt("id"))
                    .add("currency1", jsonObj.getJSONObject("coin1").getString("name"))
                    .add("currency2", jsonObj.getJSONObject("coin2").getString("name"))
                    .add("date", jsonObj.getString("date_exchange"))
                    .add("date_max", jsonObj.getString("date_max"))
                    .add("price", jsonObj.getString("price"))
                    .add("price_max", jsonObj.getString("max"))
                    .build();
                sendToSession(session, addMessage);
        }
        
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    public void addTracking(Device device) {
        JsonProvider provider = JsonProvider.provider();
        String response = null;
        try {
			response = petitionHttpPost(device.getCurrency1(), device.getCurrency2());
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try (JsonReader reader = Json.createReader(new StringReader(response))) {
            JsonObject jsonMessage = reader.readObject();
            device.setId(jsonMessage.getJsonNumber("id").intValue());
            device.setDate_Max(jsonMessage.getString("date_max"));
            device.setDate(jsonMessage.getString("date_exchange"));
            device.setPrice(jsonMessage.getString("price"));
            device.setPrice_Max(jsonMessage.getString("max"));           
        }    
                    
		 JsonObject addMessage = provider.createObjectBuilder()
		      .add("action", "add")
		      .add("id", device.getId())
		      .add("currency1", device.getCurrency1())
		      .add("currency2", device.getCurrency2())
		      .add("date", device.getDate())
		      .add("date_max", device.getDate_Max())
		      .add("price", device.getPrice())
		      .add("price_max", device.getPrice_Max())
		      .build();
		sendToAllConnectedSessions(addMessage);   
    }

    public void removeTracking(int id) {
    	JsonProvider provider = JsonProvider.provider();
    	petitionHttpDeleteExchange(String.valueOf(id));
    	JsonObject removeMessage = provider.createObjectBuilder()
        		.add("action", "remove")
                .add("id", id)
                .build();
        sendToAllConnectedSessions(removeMessage);      
    }


    private void sendToAllConnectedSessions(JsonObject message) {  
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(DeviceSessionHandler.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
    
    private static String petitionHttpGetExchanges() {
    	String url = ("http://localhost:8080/RestWSExample/rest/exchange-rates/");
    	String respuesta = null;
    	try {
			respuesta = responseHttpGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	    	
		return respuesta;
    }
    
    private static void petitionHttpDeleteExchange(String id) {
    	String url = ("http://localhost:8080/RestWSExample/rest/exchange-rates/" + id);
    	try {
    		responseHttpDelete(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void petitionHttpUpdateExchange(boolean activar) {
    	UPDATING = activar;
    	if(UPDATING) {
    		if(timer_num == 0) {
    		timer_num++;
    		Timer timer = new Timer(7000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
		    	 JsonProvider provider = JsonProvider.provider();
		    	 String exchanges = petitionHttpGetExchanges();
		         JSONArray jsonArr = new JSONArray(exchanges);
		         
		         for (int i = 0; i < jsonArr.length(); i++){
		             JSONObject jsonObj = jsonArr.getJSONObject(i);
		             int id = jsonObj.getNumber("id").intValue();
		             String url = ("http://localhost:8080/RestWSExample/rest/exchange-rates/current/" + String.valueOf(id));
		         	 String respuesta = null;
		         	 try {
		     			 respuesta = responseHttpGet(url);
		     		 } catch (Exception e1) {
		     			 e1.printStackTrace();
		     		 }
		         	 
		         	try (JsonReader reader = Json.createReader(new StringReader(respuesta))) {
		                JsonObject jsonMessage = reader.readObject();
		                JsonObject addMessage = provider.createObjectBuilder()
		          		      .add("action", "update")
		          		      .add("id", id)
		          		      .add("date", jsonMessage.getString("date_exchange"))
		          		      .add("date_max", jsonMessage.getString("date_max"))
		          		      .add("price", jsonMessage.getString("price"))
		          		      .add("price_max", jsonMessage.getString("max"))
		          		      .build();
		          		sendToAllConnectedSessions(addMessage);
		         	 }    
		        } 
				
			}			
		});
		
		timer.start();
    		}
    	}
    }
    
    private static String petitionHttpPost(String coin1, String coin2) throws IOException {
        URL url = new URL("http://localhost:8080/RestWSExample/rest/exchange-rates");
        Map<String, Object> params = new LinkedHashMap<>();
 
        params.put("coin1", coin1);
        params.put("coin2", coin2);
 
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0)
                postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
                    "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
 
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length",
                String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
 
        Reader in = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), "UTF-8"));
        
        String response[] = new String[200];
        int i = 0;
        for (int c = in.read(); c != -1; c = in.read()) {
        	response[i] = String.valueOf((char) c);
        	i++;
        }
        String answer[] = new String[i];
        System.arraycopy(response, 0, answer, 0, i);
        return Arrays.stream(answer).collect(Collectors.joining());
    }
    
    private static String responseHttpGet(String urlParaVisitar) throws Exception {
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
    
    private static String responseHttpDelete(String urlParaVisitar) throws Exception {
		StringBuilder resultado = new StringBuilder();
		URL url = new URL(urlParaVisitar);
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod("DELETE");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		String linea;

		while ((linea = rd.readLine()) != null) {
			resultado.append(linea);
		}

		rd.close();
		return resultado.toString();
	}

}
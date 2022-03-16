package aar;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Exchange implements Serializable {
   
   //serialVersionUID obligatorio en objetos serializables
   private static final long serialVersionUID = 1L;
   
   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(nullable = false)
   private Currency coin1;
   
   @Column(nullable = false)
   private Currency coin2;
   
   @Column(nullable = true)
   private String price;
   
   @Column(nullable = true)
   private String price_max;
   
   @Column(nullable = true)
   private String date_exchange;
   
   @Column(nullable = true)
   private String date_max;

   public Exchange() {}
   
   public Exchange(Currency coin1, Currency coin2) {
      this.coin1 = coin1;
      this.coin2 = coin2;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }
   
   public Currency getCoin1() {
      return coin1;
   }
  
   public void setCoin1(Currency name) {
      this.coin1 = name;
   }
	
   public Currency getCoin2() {
	  return coin2;
   }
   
   public void setCoin2(Currency name) {
      this.coin1 = name;
   }

	public String getPrice() {
		return price;
	}
	
	public void setPrice(String exchange) {
		this.price = exchange;
	}
	
	public String getMax() {
		return price_max;
	}

	public void setMax(String max) {
		this.price_max = max;
	}

	public String getDate_exchange() {
		return date_exchange;
	}

	public void setDate_exchange(String date_exchange) {
		this.date_exchange = date_exchange;
	}

	public String getDate_max() {
		return date_max;
	}

	public void setDate_max(String date_max) {
		this.date_max = date_max;
	}
   
	
   
}

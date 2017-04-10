package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class CartDetails extends BaseEntity{
	
	@ManyToOne
	public Customer customer;
	
	@ManyToOne
	public Products products;
	
	public double amount;
	
	public Long quantity;
	
	public String status;
	
	public static Model.Finder<Long, CartDetails> find=new Model.Finder<Long, CartDetails>(CartDetails.class);
	
}

package models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import com.avaje.ebean.Model;

@Entity
public class Products extends BaseEntity{
	
	public String brand;
	
	public String color;
	
	public double price;
	
	public String waranty;
	
	public String description;
	
	@Lob
	public byte[] image;
	
	public static Model.Finder<Long, Products> find=new Model.Finder<Long, Products>(Products.class);
}

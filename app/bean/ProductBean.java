package bean;

import javax.persistence.Lob;

public class ProductBean {
	
	public Long category;
	
	public Long id;
	
	public String brand;
	
	public String color;
	
	public double price;
	
	public String waranty;
	
	public String description;
	
	@Lob
	public byte[] image;
}

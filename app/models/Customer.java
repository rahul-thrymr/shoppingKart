package models;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Model;

@Entity
public class Customer extends BaseEntity {
	 
	public String customer_name;
	public String email;
	public String password;
	public String gender;
	public String address;
	public Long mobile;
	
	@ManyToMany
	@JoinTable(name = "Customer_Role")
	public List<Role> role = new ArrayList<Role>();
	
	public static Model.Finder<Long, Customer> find=new Model.Finder<Long, Customer>(Customer.class);
	
}

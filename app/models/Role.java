package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Model;

@Entity
public class Role extends BaseEntity{

	@Column(unique=true)
	public String role;
	
	@ManyToMany
	@JoinTable(name="Customer_Role")
	public List<Customer> cust = new ArrayList<Customer>();
	
	public static Finder<Long,Role> find=new Model.Finder<Long,Role>(Role.class);
}

package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

@Entity
public class Category extends BaseEntity{
	
	public String categoryName;
	
	@OneToMany(targetEntity=Products.class,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	public List<Products> products=new ArrayList<Products>();
	
	public static Model.Finder<Long, Category> find=new Model.Finder<Long, Category>(Category.class);
	
}

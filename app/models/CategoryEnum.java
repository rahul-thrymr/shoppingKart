package models;

import com.avaje.ebean.annotation.EnumValue;

public enum CategoryEnum {
	
	@EnumValue("Mobiles")
	Mobiles,
	
	@EnumValue("Watches")
	Watches,
	
	@EnumValue("Camera")
	Camera,
	
	@EnumValue("Shoes")
	Shoes,
	
	@EnumValue("Sports")
	Sports,
	
	@EnumValue("Toys")
	Toys,
	
	@EnumValue("Books")
	Books;
}

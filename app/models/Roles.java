package models;

import com.avaje.ebean.annotation.EnumValue;

public enum Roles {
	
	@EnumValue("Admin")
	Admin,
		
	@EnumValue("Customer")
	Customer
}

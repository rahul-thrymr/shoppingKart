package controllers;

import models.Customer;
import play.mvc.*;

public class FirstController extends Controller{
	
	public Result callRegister(){
		return ok(views.html.register.render());
	}
	
	public Result callHeader(){
		return ok(views.html.header.render("Home Page"));
	}
	
	public Result callLogin(){
		return ok(views.html.login.render("Login Page"));
	}
	
	public Result callAdmin(){
		return ok(views.html.adminLogin.render());
	}
	
	public Result callAdminHomePage(){
		return ok(views.html.adminHeader.render("Admin Homepage"));
	}
	
	public Result callCategory(){
		if(session("admin")!=null){
			return ok(views.html.admin.category.render());
		}else{
			return ok(views.html.adminLogin.render());
		}
	}
	
	public Result callProducts(){
		if(session("admin")!=null){
			return ok(views.html.admin.products.render());
		}else{
			return ok(views.html.adminLogin.render());
		}		
	}
	
	public Result editProfile(){
		Long cid=Long.parseLong(session("detailId"));
		Customer customer=Customer.find.byId(cid);
		return ok(views.html.customer.editProfile.render(customer));
	}
	
	/*
	public Result callToys(){
		return ok(views.html.addProducts.toys.render());
	}
	
	public Result callMobiles(){
		return ok(views.html.addProducts.mobiles.render());
	}
	
	public Result viewToys(){
		
		return ok(views.html.addProducts.viewToys.render());
	}
	
	public Result adminViewToys(){
		return ok(views.html.addProducts.adminViewToys.render());
	}*/
}

package controllers;


import models.Customer;
import models.Role;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Util;
import bean.CustomerBean;

public class CustomerController extends Controller{
	
	public static CustomerBean bean;
	
	public Result store(){
		
		bean=Form.form(CustomerBean.class).bindFromRequest().get();
		Customer c=new Customer();
		Logger.info("==== ===================================");
		c.email=bean.email;
		c.gender=bean.gender;
		c.mobile=bean.mobile;
		c.password=Util.encode(bean.password);
		c.customer_name=bean.name;
		c.address=bean.address;
		
		c.role.add(Role.find.where().in("role", "Customer").findUnique());

		c.save();
		
		flash("success","Successfully registed in BADA Shopping");
		return redirect("/login");
	}
	
	public Result login(){
		
		bean=Form.form(CustomerBean.class).bindFromRequest().get();
		Customer details=Customer.find.where().eq("email", bean.email).findUnique();
		
		if(bean.select.equals("Customer")){
			if(details != null){	
				String email=details.email;
				String pwd=Util.decode(details.password);
				Logger.info("Customer name ----> "+bean.email+" pass ---> "+bean.password);
				if(email.equals(bean.email)&&pwd.equals(bean.password)){
					session("detailId", ""+details.id);
					Logger.info("Customer id is----------->: "+details.id);
					session("email", ""+details.email);
					flash("success","Login successfully. ");
					return redirect("/viewProducts");
				}
				else{
					flash("error","Please enter valid Email id and Password");
					return redirect("/login");
				}
			}
			else{
				flash("error","Invalid your Email id and Password");
				return redirect("/login");
			}
		}else{
			String name="rajesh@gmail.com";
			String password="admin";
			Logger.info("name ----> "+bean.email+" pass ---> "+bean.password);
			
			if(name.equalsIgnoreCase(bean.email)&&password.contentEquals(bean.password)){
				String admin=name;
				session("admin", admin);
				return ok(views.html.adminHeader.render("Admin Homepage"));
			}
			else
				return ok(views.html.failure.render("Invalid your Email id and Password"));
		}
		
	}
	
	public Result isLoggedInUser(){
		
		if(session("detailId") == null){
			
			return ok("");
			
		}else{
			return null;
		}
	}
	
	public static Boolean isLoggedIn() {
        return session("detailId") == null ? false : true;
    }
	
	public Result logout(){
		
		session().clear();
		Logger.info("=====================>>>> "+session("detailId"));
		flash("success","Successfully Log out to BADA Shopping");
		
		return redirect("/login");
	}
	
	public Result updateProfile(){
		
		bean=Form.form(CustomerBean.class).bindFromRequest().get();
		Customer customer=Customer.find.byId(Long.parseLong(session("detailId")));
		if(customer!=null){
			customer.address=bean.address;
			customer.customer_name=bean.name;
			customer.mobile=bean.mobile;
			customer.email=bean.email;
			customer.password=bean.password;
			customer.update();
			flash("success","Successfully updated your profile");
			return redirect("/viewProducts");
		}else{
			flash("success","Updated failed");
			return redirect("/viewProducts");
		}
		
	}
}

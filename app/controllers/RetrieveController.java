package controllers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;

import models.CartDetails;
import models.Customer;
import models.Products;
import play.libs.Json;
import play.mvc.*;

public class RetrieveController extends Controller {

	public Result getImage(Long id) {

		RawSql rawSql = RawSqlBuilder.parse("select id,image from products where id =" + id).create();
		Query<Products> query = Ebean.find(Products.class);
		query.setRawSql(rawSql);
		Products products = query.findUnique();
		ByteArrayInputStream input = null;
		try {
			if (products != null && products.image != null) {
				//Logger.info("image is coming");
				input = new ByteArrayInputStream(products.image);
				return ok(products.image).as("image/jpg");
			} else {
				//Logger.info("image is not coming");
				return Results.ok("failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok(products.image).as("image/jpg");
	}

	public Result viewProductCust() {
		
		if(session("detailId")!=null){
			Long cid=Long.parseLong(session("detailId"));
			List<CartDetails> cartDetails=null;
			cartDetails=CartDetails.find.where().eq("customer_id", cid).eq("status", "unordered").findList();
			Long quantity=(long)0;
			
			if(cartDetails!=null){
				for (CartDetails s : cartDetails){		
					quantity=quantity+s.quantity;
				}				
				return ok(views.html.customer.viewProductsCust.render(quantity.toString()));
			}
			else{
				return ok(views.html.customer.viewProductsCust.render("0"));
			}			
			
		}else
			return ok(views.html.customer.viewProductsCust.render("0"));
	}

	public Result viewProductsToAdmin(Long id) {
		
		return ok(views.html.admin.adminViewProducts.render(id));
	}
	
	public Result allProductDetails(){
		
		List<Products> products=Products.find.all();
		return ok(views.html.admin.allProductDetails.render(products));
	}
	

	public Result getProductId(Long id) {

		Products pro=Products.find.byId(id);
		return ok(views.html.admin.editProducts.render(pro));
	}
	
	public Result viewCartDetails(){
		
		String sessionCid=session("detailId");		
		if(sessionCid!=null){
			return ok("wait");			
		}else{
			flash("error","Please Login to BADA Shopping then view your Cart details");
			return redirect("/login");
		}
	}
	
	public Result viewCart(){
		
		if(session("detailId")!=null){
			Long cid=Long.parseLong(session("detailId"));	
			List<CartDetails> list=CartDetails.find.where().eq("status", "unordered").eq("customer_id",cid).findList();
			String str="";
			if(list.size()!=0){
				str="notempty";				
				return ok(views.html.customer.viewCart.render(list,str));
			}else{
				str="empty";
				return ok(views.html.customer.viewCart.render(list,str));				
			}
		}
		else{
			flash("error","You don't have Cart details, please ADD to CART.....!");
			return redirect("/login");
		}
	}
	
	public Result cartHistory(){
		
		if(session("detailId")!=null){
			Long cid=Long.parseLong(session("detailId"));	
			List<CartDetails> list=CartDetails.find.where().eq("status", "ordered").eq("customer_id",cid).findList();
			String str="";
			if(list.size()!=0){
				str="notempty";				
				return ok(views.html.customer.viewCart.render(list,str));
			}else{
				str="empty";
				return ok(views.html.customer.viewCart.render(list,str));				
			}
		}
		else{
			flash("error","You don't have Cart details, please ADD to CART.....!");
			return redirect("/login");
		}
	}
	
	public Result orderProducts(){
		
		List<CartDetails> list=CartDetails.find.all();
		return ok(views.html.admin.orderProducts.render(list));
	}
	
	public Result getUser(){
		Long cid=Long.parseLong(session("detailId"));
		Customer customer=Customer.find.byId(cid);
		Logger.info("customer === " + customer.id);
		return ok(Json.toJson(customer).toString());
	}
	
	public Result getEmail(String email){
		Customer existMail=Customer.find.where().eq("email", email).findUnique();
		if(existMail!=null){
			Logger.info(email);
			return ok("This email id is already exist");
		}
		return ok("");
	}
}

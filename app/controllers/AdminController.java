package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.google.common.io.Files;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import play.Logger;
import models.CartDetails;
import models.Category;
import models.Customer;
import models.Products;
import bean.CustomerBean;
import bean.ProductBean;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import  java.util.Hashtable;
import  javax.naming.*;
import  javax.naming.directory.*;

public class AdminController extends Controller{
	
	public static CustomerBean bean;
	public static ProductBean pbean;
	
	public Result login(){
		
		bean=Form.form(CustomerBean.class).bindFromRequest().get();
		String name="rajesh";
		String password="admin";
		Logger.info("name ----> "+bean.name+" pass ---> "+bean.password);
		
		if(name.equalsIgnoreCase(bean.name)&&password.contentEquals(bean.password)){
			String admin=name;
			session("admin", admin);
			return ok(views.html.adminHeader.render("Admin Homepage"));
		}
		else
			return ok(views.html.failure.render("Invalid your Email id and Password"));
		
	}
	
	public Result logout(){
		session().clear();
		Logger.info("=====================>>>> "+session("admin"));		
		return redirect("/login");
	}
	
	public static Boolean isAdminLoggedIn() {
        return session("admin") == null ? false : true;
    }
	
	public Result addCategory(){
		bean=Form.form(CustomerBean.class).bindFromRequest().get();
		Category exist=Category.find.where().eq("category_name", bean.categoryName.trim()).findUnique();
		Category c=null;
		if(exist!=null){
			c=exist;
		}else{
			c=new Category();
		}
		c.categoryName=bean.categoryName;
		c.save();
		flash("success","successfully added category in database");
		return redirect("/callCategory");	
		
	}
	
	public Result addProduct(){
		ProductBean bean=Form.form(ProductBean.class).bindFromRequest().get();
		Category exist= Category.find.where().eq("id", bean.category).findUnique();
		Category c=null;
		if(exist!=null){
			c=exist;
		}else
			c=new Category();
		Products pro=new Products();
		
		try {
			MultipartFormData body = request().body().asMultipartFormData();
			FilePart picture = body.getFile("image");
			if (picture != null) {
				String fileName = picture.getFilename();
				String contentType = picture.getContentType();
				File file = (File) picture.getFile();
				if(file != null){
					Logger.info("---------->image is not null");
					bean.image=Files.toByteArray(file);
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			//flash().put("alert",new Alert("alert-success", "Please upload Image ").toString());			
			return ok(views.html.failure.render("Please upload Image"));
		}
		pro.image=bean.image;
		pro.brand=bean.brand;
		pro.color=bean.color;
		pro.description=bean.description;
		pro.price=bean.price;
		pro.waranty=bean.waranty;
		
		List<Products> list=new ArrayList<Products>();
		list.add(pro);
		c.products=list;
		
		c.save();
		pro.save();
		flash("success","successfully saved product details");
		return redirect("/callProducts");
	}
	
	public Result updateProducts(Long id){
		pbean=Form.form(ProductBean.class).bindFromRequest().get();
		Products pro=Products.find.byId(id);
		
		if(pbean.image!=null){
		try {
			MultipartFormData body = request().body().asMultipartFormData();
			FilePart picture = body.getFile("image");
			if (picture != null) {
				String fileName = picture.getFilename();
				String contentType = picture.getContentType();
				File file = (File) picture.getFile();
				if(file != null){
					Logger.info("---------->image is not null");
					pro.image=Files.toByteArray(file);
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			flash().put("alert", "Please upload Image");			//return ok(views.html.admin.adduser.render(users));
		}
		}else
		{
			pro.image=pro.image;
			Logger.info("image is already there no need to update------------------>>>>>>");
		}
		pro.brand=pbean.brand;
		pro.color=pbean.color;
		pro.description=pbean.description;
		pro.price=pbean.price;
		pro.waranty=pbean.waranty;
				
		pro.update();
		
		flash("success","successfully updated");
		return redirect("/callProducts");
		
	}
	
	public Result deleteProducts(Long id){
		Products products=Products.find.byId(id);
		if(products!=null){
			List<CartDetails> list=CartDetails.find.where().eq("products_id", products.id).findList();
			for(CartDetails cart:list){
				cart.delete();
			}
			
			products.delete();
			flash("success","successfully deleted");
			return redirect("/callProducts");
		}else{
			flash("success","deleted failed");
			return redirect("/callProducts");
		}
	}
	 
	public Result addToCart(Long pid){
		
		Products product=Products.find.byId(pid);					
		
		if(session("detailId")!=null){
			Long cid=Long.parseLong(session("detailId"));
			
			CartDetails cartDetails= CartDetails.find.where().eq("products_id", pid).eq("customer_id", cid).eq("status", "unordered").findUnique();
			
			Logger.info("Customer id is---------> "+cid+" and Products id is---------> "+pid);
			if(cartDetails != null){
				Logger.info("Customer id is---------> "+cid+" and Products id is---------> "+pid);
				Double amount = cartDetails.amount;
				cartDetails.amount=amount+product.price;
				Long q = cartDetails.quantity;
				cartDetails.quantity=q+1;				
				cartDetails.update();
				flash("success","Successfully added to Cart");			
				return redirect("/viewProducts");
			} else {				
				Logger.info("Customer id is---------> "+cid+" and Products id is---------> "+pid);
				
				cartDetails = new CartDetails();
				cartDetails.amount=product.price;
				cartDetails.customer=Customer.find.byId(cid);
				cartDetails.products=Products.find.byId(pid);
				cartDetails.quantity=1l;
				cartDetails.status="unordered";
				cartDetails.save();
				
				flash("success","Successfully added to Cart");			
				return redirect("/viewProducts");		
			}
		}
		else{
			flash("error","First Login to Website then proceed");			
			return redirect("/login");
		}	
	}

	public Result removeCart(Long id){
		
		Logger.info("product is is ==========================>>>>>>>>>>>> "+id);
		
		CartDetails cart=CartDetails.find.byId(id);
		
		if(cart != null && cart.quantity != null && cart.quantity>1){
			
			cart.quantity=cart.quantity-1;
			cart.amount=cart.quantity*cart.products.price;
			cart.update();
			List<CartDetails> allCartDetails = CartDetails.find.where().eq("status", "unordered").eq("customer_id",Long.parseLong(session("detailId"))).findList();
			flash("success","Successfully removed to cart");
			return ok(views.html.customer.viewCart.render(allCartDetails, "notempty"));			
		}
		else{
			cart.delete();	
			List<CartDetails> allCartDetails = CartDetails.find.where().eq("status", "unordered").eq("customer_id",Long.parseLong(session("detailId"))).findList();
			flash("success","Successfully removed to cart");
			return ok(views.html.customer.viewCart.render(allCartDetails, "notempty"));	
		}	
	}
	
	public Result sendEmail(){
		
		if(session("detailId")!=null){
			
			Long cid=Long.parseLong(session("detailId"));
			Customer customer=Customer.find.byId(cid); 
			
			// compose message
			try {
				MimeMessage message = new MimeMessage(mailMethod());
				message.setFrom(new InternetAddress("rajesh.annaboena@thrymr.net"));
				//Logger.info(to);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(customer.email));
				message.setSubject("Hello");
				/*message.setText("Testing.......");				
				List<CartDetails> list=CartDetails.find.where().eq("customer_id", cid).findList();
				String str="<table><tr><th>Product Name</th><th>Quanitty</th><th>Amount</th></tr>";
				double total=0d;
				for(CartDetails cart:list){
					str=str+"<tr><td>"+cart.products.brand+"</td><td>"+cart.quantity+"</td> <td>"+cart.amount+"</td></tr>";
					total=total+cart.amount;
				}
				str = str+"</table>";				
				message.setContent(str+"<br><p>Total amount is:"+total+"</p>","text/html");	*/							
				// send message
				
				AdminController.generatePdf();
				   
				MimeBodyPart messageBodyPart2 = new MimeBodyPart(); 
				   
				   
			    String filename = "o1.pdf";//change accordingly  
			    FileDataSource source = new FileDataSource("/home/demo1/Documents/file1.pdf");  
			    messageBodyPart2.setDataHandler(new DataHandler(source));  
			    messageBodyPart2.setFileName(filename);  
			    Multipart multipart = new MimeMultipart(); 
			    multipart.addBodyPart(messageBodyPart2);  
			  
			    //6) set the multiplart object to the message object  
			    message.setContent(multipart);
			    
			    Transport.send(message);
			    
			    List<CartDetails> lcust=CartDetails.find.where().eq("customer_id", cid).findList();
			    List<CartDetails> lcart=CartDetails.find.where().eq("status", "unordered").findList();
			    if(lcust!=null){
			    	for(CartDetails cart:lcart){
			    		cart.status="ordered";
			    		cart.update();
			    	}
			    }
			    
				Logger.info("message sent successfully===========>>>");
				flash("success","Successfully products are placed now your cart is empty");
				return redirect("/viewCart");
	
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
		else{
			flash("success","unsuccessfully send to your email");
			return redirect("/viewCart");
		}	
	}
	
	public Session mailMethod(){
		
		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("rajesh.annaboena@thrymr.net", "rajeshgoud");
			}
		});
		return session;
	}
	
	public static Result generatePdf(){	
		if(session("detailId")!=null){
	       try {
    	   		Double total=0d;
        		FileOutputStream file = new FileOutputStream(new File("/home/demo1/Documents/file1.pdf"));
        		Document document = new Document();
        		PdfWriter.getInstance(document,file);
        		document.open();
        		Logger.info("id------------------------- "+session("detailId"));
        		Long cid=Long.parseLong(session("detailId"));
        		Customer customer=Customer.find.where().eq("id",cid).findUnique();
        		
    			
    			document.add(new Paragraph("Mr/Miss Name:    " +customer.customer_name));
        		document.add(new Paragraph("Email:   " +customer.email));
        		document.add(new Paragraph("--------------------------------------------------"));
        		document.add(new Paragraph("YOUR ORDER DETAILS"));
        		document.add(new Paragraph(" "));
        		document.add(new Paragraph(" "));
        		
        		//setting column names
        		PdfPCell cell1 = new PdfPCell(new Paragraph("Product_Name:")); 
        		PdfPCell cell2 = new PdfPCell(new Paragraph("Product_Color:")); 
        		PdfPCell cell3 = new PdfPCell(new Paragraph("Product_Quantity")); 
        		PdfPCell cell4 = new PdfPCell(new Paragraph("Product_Amount")); 
        	
                //adding created columns to table
        		PdfPTable table=new PdfPTable(4);
        		table.addCell(cell1); 
        		table.addCell(cell2);
        		table.addCell(cell3);
        		table.addCell(cell4);
        	
        		//getting object of Coach
        		List<CartDetails> cartList=CartDetails.find.where().eq("customer_id", cid).findList();
        	
                 // In the paragraph                                
        		if(cartList!=null) { 
        			for(CartDetails cart:cartList) { 
        				
        				String quantity=cart.quantity.toString();
        				Double amount=cart.amount;
        				
        				table.addCell(new PdfPCell(new Paragraph(cart.products.brand)));
        				table.addCell(new PdfPCell(new Paragraph(cart.products.color)));
        				table.addCell(new PdfPCell(new Paragraph(quantity)));
        				table.addCell(new PdfPCell(new Paragraph(amount.toString())));
        				
        				total=total+cart.amount; 
        			} 
        			document.add(table); 
        			document.add(new Paragraph("Total Amount is:  "+total));
        				
        		}
        			document.close();
                  file.close();
        	} catch (Exception e) {
        			e.printStackTrace(); 
        			return ok("pdf generated failed");
          }
          return ok("pdf generated successfully really");
		}else
			return redirect("/login");
	}
	
	public Result refer(){
		
		CustomerBean bean=Form.form(CustomerBean.class).bindFromRequest().get();
		if(session("detailId")!=null&&isValidEmailAddress(bean.email)){
			
			// compose message
			try {
				MimeMessage message = new MimeMessage(mailMethod());
				message.setFrom(new InternetAddress("rajesh.annaboena@thrymr.net"));
				
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(bean.email));
				message.setSubject("Refered Your Friend");
				message.setText("Please visit this website you can buy number of products in Bada Shopping --> http://localhost:9000");				
				
			    Transport.send(message);
				Logger.info("message sent successfully===========>>>");
				flash("success","Successfully refered your friend ");
				return redirect("/viewProducts");
	
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
		else{
			flash("success","unsuccessfully refered your friend invalid email id");
			return redirect("/viewProducts");
		}	
	
	}
	
	public boolean isValidEmailAddress(String email) {
	   boolean result = true;
	   try {
	      InternetAddress emailAddr = new InternetAddress(email);
	      emailAddr.validate();
	   } catch (AddressException ex) {
	      result = false;
	   }
	   return result;
	}
}

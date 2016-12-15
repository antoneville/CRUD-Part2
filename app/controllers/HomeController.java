package controllers;

import play.api.Environment;
import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import views.html.*;

// Import models
import models.*;


public class HomeController extends Controller {

    private FormFactory formFactory;

    @Inject
    private HomeController(FormFactory f){
    	this.formFactory = f;
    }

    public Result index(String name) {
        return ok(index.render("Welcome to the Home page", name));
    }

    public Result about() {
        return ok(about.render());
    }

    public Result products() {

        // Get list of all categories in ascending order
        List<Product> productsList = Product.findAll();
        return ok(products.render(productsList));
    }

    public Result addProduct() {
	Form<Product> addProductForm = formFactory.form(Product.class);
	return ok(addProduct.render(addProductForm));
    }

    public Result addProductSubmit() {

        // Create a product form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Product> newProductForm = formFactory.form(Product.class).bindFromRequest();

        // Check for errors (based on Product class annotations)
        if(newProductForm.hasErrors()) {
            // Display the form again
            return badRequest(addProduct.render(newProductForm));
        }

        // Extract the product from the form object
        Product p = newProductForm.get();

        p.save();
        

        // Set a success message in temporary flash
        // for display in return view
        flash("success", "Product " + p.getName() + " has been created/ updated");

        // Redirect to the admin home
        return redirect(controllers.routes.HomeController.products());
    }

       // @Transactional
        public Result deleteProduct(Long id) {

        // find product by id and call delete method
        Product.find.ref(id).delete();
        // Add message to flash session
        flash("success", "Product has been deleted");

        // Redirect to products page
        return redirect(routes.HomeController.products());
    }

}



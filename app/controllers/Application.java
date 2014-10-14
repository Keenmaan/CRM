package controllers;

import models.Contact;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render("index"));
    }

    @Security.Authenticated(Secured.class)
    public static Result usersList(){
        return Users.userList();
    }

    public static Result registerForm(){
        return ok(register.render(Form.form(User.class)));
    }

    public static Result register(){
        Form<User> form = Form.form(User.class).bindFromRequest();
        List<ValidationError> errors = new ArrayList<>();
        if (form.hasErrors()){
            return badRequest(register.render(form));
        }
        Logger.info(""+form.get().name+", password:"+form.get().password);
        //Safe validation:
        //Make sure there is no such username.
        Logger.info("Users.findName(form.get().name)  "+Users.findName(form.get().name));
        if (Users.findName(form.get().name)){
            errors.add(new ValidationError(
                    "name",
                    "User already exists.",
                    new ArrayList<>()
            ));
            form.errors().put("name",errors);
        }
        //Check if proper password.
        Logger.info("form.get().password.length() "+form.get().password.length());
        if (form.get().password.length()<3){
            errors.add(new ValidationError(
                    "password",
                    "Password too short.",
                    new ArrayList<>()
            ));
            form.errors().put("password",errors);
        }
        if (form.hasErrors()){
            Logger.info("Errors:"+errors);
            return badRequest(register.render(form));
        }
        else
            Users.createUser(
                form.get().name,
                form.get().password,
                false);
        return redirect(routes.Application.loginForm());
    }

    public static Result logout() {
        session().clear();
        return ok(logout.render());
    }

    public static Result loginForm() {
        Form<User> form = Form.form(User.class);
        return ok(login.render(form));
    }

    public static Result login(){
        Logger.info("***********************");
        Logger.info("  1. Application.authenticate()");
        Form<User> form = Form.form(User.class).bindFromRequest();
        Logger.info("Authenticate2.");
        if (form.hasErrors()) {
            Logger.info("Authentication has failed.");
            return badRequest(login.render(form));
        } else {
            String name=form.get().name;
            String password=form.get().password;
            Logger.info("Login form has no errors.");
            Logger.info("User:"+ form.get().name+"/" + form.get().password);
            if (User.authenticate(name, password) == null) {
                Logger.info("  name:"+name+", password:"+password);
                Logger.info("  * end *");
                return badRequest(login.render(form));
            }
            Logger.info("Authentication successful.");
            Logger.info("User:"+ form.get().name+"/" + form.get().password);
            session().clear();
            session("name", form.get().name);
            return redirect(
                    routes.Application.index()
            );
        }
    }



    @Security.Authenticated(Secured.class)
    public static Result contactsList() {

        List<Contact> contactList;

        User user = User.find.where().eq("name", session("name")).findUnique();
        contactList = user.getContacts();

        return ok(contacts.render(contactList, user));
    }

    @Security.Authenticated(Secured.class)
    public static Result addContactForm(){
        return ok(addContact.render(Form.form(Contact.class)));
    }

    @Security.Authenticated(Secured.class)
    public static Result addContact() {
        Form<Contact> contactForm = Form.form(Contact.class).bindFromRequest();

        if (contactForm.hasErrors()){
            return badRequest(addContact.render(contactForm));
        }
        else{
            User user = User.find.where().eq("name", session("name")).findUnique();
            Contact contact;
            contact = Form.form(Contact.class).bindFromRequest().get();
            contact.setUser(user);
            contact.save();
            user.addContact(contact);
            user.save();
        }
        return contactsList();
    }

    public static Result editContact(Long Id) {
        Form<Contact> contactForm = Form.form(Contact.class).bindFromRequest();
        Contact contact = Contact.find.byId(Id);
        contactForm.fill(contact);

        return ok(editContact.render(contactForm, contact));
    }

    public static Result saveContact(Long Id) {
        Contact contact;
        contact = Form.form(Contact.class).bindFromRequest().get();
        contact.update();

        return contactsList();
    }

    public static Result deleteContact(Long Id) {
        Contact contact = Contact.find.byId(Id);
        contact.delete();

        return contactsList();
    }
}
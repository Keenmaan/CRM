package controllers;

import models.Contact;
import models.User;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.addContact;
import views.html.contacts;
import views.html.editContact;

import java.util.List;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

/**
 * Created by keen on 10/15/14.
 */
@Security.Authenticated(Secured.class)
public class Contacts {

    public static Result contactsList() {

        List<Contact> contactList;

        User user = Users.getCurrentUser();
        contactList = user.getContacts();

        return ok(contacts.render(contactList, user));
    }


    public static Result addContactForm(){
        return ok(addContact.render(Form.form(Contact.class)));
    }


    public static Result addContact() {
        Form<Contact> form = Form.form(Contact.class).bindFromRequest();
        if (form.hasErrors()){
            return badRequest(addContact.render(form));
        }
        else{
            User user = Users.getCurrentUser();
            Contact contact;
            contact = form.bindFromRequest().get();
            contact.user=user;
            contact.save();
            user.addContact(contact);
            user.save();
        }
        return contactsList();
    }


    public static Result editContact(Long Id) {
        Form<Contact> form = Form.form(Contact.class).bindFromRequest();
        if (form.hasErrors()){
            return contactsList();//add a bad result
        }
        else {
            Contact contact = validateRequest(Id);
            if (contact!=null){
                form.fill(contact);
                return ok(editContact.render(form, contact));
            }
            return contactsList();
        }
    }


    public static Result saveContact(Long Id) {
        Form<Contact> form = Form.form(Contact.class).bindFromRequest();
        Contact contact;
        if (form.hasErrors()){
            return contactsList();//Add a bad result
        }
        else {
            contact = form.bindFromRequest().get();
            contact.update();
            return contactsList();
        }
    }


    public static Result deleteContact(Long Id) {
        Contact contact = validateRequest(Id);
        String s="Contact doesn't exist or cannot be deleted.";
        if(contact!=null){
            contact.delete();
        }
        return contactsList();
    }

    public static Contact validateFormRequest(){
        return new Contact();
    }

    public static Contact validateRequest(Long Id){
        //Contact<List> = new ArrayList
        User usr = Users.getCurrentUser();
        if(usr!=null){
            Contact contact = Contact.find.byId(Id);
            if (contact!=null)
                if(contact.user.id==usr.id)
                    return contact;
        }
        return null;
    }
}

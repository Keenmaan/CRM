package models;

import controllers.Hasher;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by keen on 10/7/14.
 */
@Entity
public class User extends Model {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String name;

    public String password;

    @OneToMany(targetEntity=Contact.class, mappedBy="user")
    public Collection contacts;

    public Long getId() {
        return id;
    }

    public Collection getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setUser(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Hasher.getHash(password, "md5");
    }

    public void setContacts(Collection contacts) {
        this.contacts = contacts;
    }

    public User() {
      //  this.name = userName;
      //  this.password = Hasher.getHash(password, "md5");
        this.contacts=new ArrayList<Contact>();
      //  this.save();
    }

    public static User authenticate(String name, String password) {
        User user = User.find.where().eq("name", name).findUnique();
        if (user != null && (user.password.equals(Hasher.getHash(password, "md5")))) {
            return user;
        } else {
            return null;
        }
    }

    public static Finder<String,User> find = new Finder<String,User>(
            String.class, User.class
    );

}

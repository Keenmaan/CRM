package models;

import controllers.Hasher;
import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by keen on 10/7/14.
 */
@Entity
public class User extends Model {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    public Long getId() {
        return id;
    }

    @Constraints.Required
    @Column(unique=true)
    public String name;

    //Password
    public static final int passwordLength=3;
    @Constraints.Required
    @Constraints.MinLength(passwordLength)
    public String password;

    @NotNull
    private boolean isAdmin=false;

    @Constraints.Pattern(value=".*\\.jpg|.*\\.jpeg|.*\\.gif",
            message="Only images of type JPEG or GIF are supported.")
    public String image=null;

    @OneToMany(targetEntity=Contact.class, mappedBy="user", cascade = CascadeType.ALL)
    public List<Contact> contacts;

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setUser(this);
    }

    public static User authenticate(String name, String password) {
        User user = User.find.where().eq("name", name).findUnique();
        if (user != null && (user.password.equals(Hasher.getHash(password, "md5")))) {
            Logger.info("Authentication successful.");
            return user;
        } else {
            return null;
        }
    }

    public static Finder<String,User> find = new Finder<>(
            String.class, User.class
    );

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin() {
        this.isAdmin=true;
    }

}

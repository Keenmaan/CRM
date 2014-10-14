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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Password
    public static final int passwordLength=3;
    @Constraints.Required
    @Constraints.MinLength(passwordLength)
    public String password;

    public String getPassword() {
        return password;
    }

    //Validating password in setter.
    public void setPassword(String password) {
        if(validatePassword(password))
            this.password = Hasher.getHash(password, "md5");
        else
            this.password=null;
    }

    private boolean validatePassword(String password){
        if (password.length()<3)
            return false;
        else
            return true;
    }

    @NotNull
    private boolean isAdmin=false;
    public String photo=null;

    @OneToMany(targetEntity=Contact.class, mappedBy="user", cascade = CascadeType.ALL)
    public List<Contact> contacts;

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setUser(this);
    }





    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static User authenticate(String name, String password) {
        Logger.info("  User.authenticate()");
        Logger.info("User.authenticate(), name:"+name+", password:"+password);
        User user = User.find.where().eq("name", name).findUnique();
        if (user != null && (user.password.equals(password))) {
            Logger.info("user.authenticate() success.");
            return user;
        } else {
            Logger.info("failed user.authenticate().");
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

    public static User findByName(String name){
        return User.find.where().eq("name",name).findUnique();
    }

    public String validate() {
        Logger.info("  2. validate(), name:"+this.name+", password:"+this.password);
//        if (User.byName(name) != null) {
//            return "This e-mail is already registered.";
//        }
        return null;
    }
}

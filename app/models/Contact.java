package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Contact extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;
    public String surname;
    public String email;
    public String photoId;
   // public Long userId;

    @ManyToOne
    public User user;

    @OneToMany(targetEntity=Company.class, mappedBy="contact")
    public Collection companies;

    public Contact(String name, String surname, String email, Long userId) {
        this.name = name;
        this.surname=surname;
        this.email=email;
        this.companies=new ArrayList<Company>();
       // this.userId=userId;

        this.save();
    }
}
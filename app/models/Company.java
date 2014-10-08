package models;

import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class Company extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String name;
    private String email;
    private String phone;
    //private Long contactId;

    @ManyToOne
    public Contact contact;

    public Company(String name, String email, String phone, Long contactId) {
        this.name = name;
        this.email=email;
        this.phone=phone;
        //this.contactId=contactId;
        this.save();
    }
}
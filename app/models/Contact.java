package models;

import play.db.ebean.Model;

import javax.persistence.*;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
//@OneToMany(targetEntity=Company.class, mappedBy="contact")
    //public Collection companies;

    public Contact() {
       // this.companies=new ArrayList<Company>();
       // this.userId=userId;
       // this.save();
    }
}
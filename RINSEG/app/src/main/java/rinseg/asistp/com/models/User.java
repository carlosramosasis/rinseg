package rinseg.asistp.com.models;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Carlos Ramos on 21/10/2016.
 */
public class User extends RealmObject{
    private int id;
    private String name;
    private String lastname;
    private String username;
    private String dni;
    private String email;
    private String photo;
    private String api_token;
    private int company_id;
    private int management_id;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public User(){}


    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getLastname() {return lastname;}

    public void setLastname(String lastname) {this.lastname = lastname;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getDni() {return dni;}

    public void setDni(String dni) {this.dni = dni;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPhoto() {return photo;}

    public void setPhoto(String photo) {this.photo = photo;}

    public String getApi_token() {return api_token;}

    public void setApi_token(String api_token) {this.api_token = api_token;}

    public int getCompany_id() {return company_id;}

    public void setCompany_id(int company_id) {this.company_id = company_id;}

    public int getManagement_id() {return management_id;}

    public void setManagement_id(int management_id) {this.management_id = management_id;}

   public String getCreated_at() {return created_at;}

    public void setCreated_at(String created_at) {this.created_at = created_at;}

    public String getUpdated_at() {return updated_at;}

    public void setUpdated_at(String updated_at) {this.updated_at = updated_at;}

    public String getDeleted_at() {return deleted_at;}

    public void setDeleted_at(String  deleted_at) {this.deleted_at = deleted_at;}

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", dni='" + dni + '\'' +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                ", api_token='" + api_token + '\'' +
                ", company_id=" + company_id  +
                ", management_id=" + management_id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                '}';
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.entities;

/**
 *
 * @author moham
 */
public class User {

    int id;
    String Email;
    String Password;

    public User(int id, String Email) {
        this.id = id;
        this.Email = Email;
    }

    public User(String Email, String Firstname, String Lastname, String Role ,String Address, int Telephone) {
        this.Email = Email;
        this.Role = Role;
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Address = Address;
        this.Telephone = Telephone;
    }

   

    public void setId(int id) {
        this.id = id;
    }


  

    public User() {
    }


    public int getBan() {
        return ban;
    }

    public void setBan(int ban) {
        this.ban = ban;
    }
    String animal;
    String Question;

    int ban;

    public User(int id, String Email, String Password, String Role, String Firstname, String Lastname, String Address, int Telephone, String animal) {
        this.id = id;
        this.Email = Email;
        this.Password = Password;
        this.animal = animal;
        this.Role = Role;
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Address = Address;
        this.Telephone = Telephone;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public User(int id, String Email, String Role, String Firstname, String Lastname, String Address, int Telephone) {
        this.id = id;
        this.Email = Email;
        this.Role = Role;
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Address = Address;
        this.Telephone = Telephone;
    }
    String Role;
    String Firstname;
    String Lastname;
    String Address;
    int Telephone;

    public User(String Email, String Password, String Role) {
        this.Email = Email;
        this.Password = Password;
        this.Role = Role;
    }

    public User(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return Email ;
    }
 

    public User(String Email, String Password, String Role, String Firstname, String Lastname, String Address, int Telephone) {
        this.Email = Email;
        this.Password = Password;
        this.Role = Role;
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Address = Address;
        this.Telephone = Telephone;
    }

    public User(String Email, String Password, String Role, String Firstname, String Lastname, String Address, int Telephone, String animal) {
        this.Email = Email;
        this.Password = Password;
        this.Role = Role;
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Address = Address;
        this.Telephone = Telephone;
        this.animal = animal;

    }

    public User(String Email, String Password, String Role, String Firstname, String Lastname, String Address, int Telephone, String animal, String question) {
        this.Email = Email;
        this.Password = Password;
        this.Role = Role;
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Address = Address;
        this.Telephone = Telephone;
        this.animal = animal;
        this.Question= question;

    }

    public User(String Email, String Password) {
        this.Email = Email;
        this.Password = Password;
    }

    public User(int id, String Email, String Password, String Role, String Firstname, String Lastname, String Address, int Telephone) {
        this.id = id;
        this.Email = Email;
        this.Password = Password;
        this.Role = Role;
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Address = Address;
        this.Telephone = Telephone;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getRole() {
        return Role;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String Firstname) {
        this.Firstname = Firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String Lastname) {
        this.Lastname = Lastname;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public int getTelephone() {
        return Telephone;
    }

    public void setTelephone(int Telephone) {
        this.Telephone = Telephone;
    }
    
   

}

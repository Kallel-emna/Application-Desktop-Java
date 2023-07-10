/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.entities;

import Clinimate.utils.DataSource;
import java.util.Date;
import java.util.List;

/**
 *
 * @author medam
 */
public class Rendez_vous {
    java.sql.Connection cnx = DataSource.getInstance().getCnx();
    int id ;
   String time ; 
User u ; 
    public Rendez_vous(int id, String time, Date date_rendezvous, int user_id, User u) {
        this.id = id;
        this.time = time;
        this.date_rendezvous = date_rendezvous;
        this.user_id = user_id;
        this.u = u;
    }
    Date date_rendezvous ; 
    int user_id ; 

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Rendez_vous(int id, String time, Date date_rendezvous, int user_id) {
        this.id = id;
        this.time = time;
        this.date_rendezvous = date_rendezvous;
        this.user_id = user_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Rendez_vous(int id, String time, Date date_rendezvous) {
        this.id = id;
        this.time = time;
        this.date_rendezvous = date_rendezvous;
    }

    @Override
    public String toString() {
        return "" + "" +date_rendezvous ;
    }

    public Rendez_vous() {
    }

   
  
 

    public Rendez_vous(int id, Date date_rendezvous) {
        this.id = id;
        this.date_rendezvous = date_rendezvous;
    }

  

    public Rendez_vous(Date date_rendezvous) {
        this.date_rendezvous = date_rendezvous;
    }

    public Rendez_vous(String time, Date date_rendezvous) {
        this.time = time;
        this.date_rendezvous = date_rendezvous;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate_rendezvous() {
        return date_rendezvous;
    }

    public void setDate_rendezvous(Date date_rendezvous) {
        this.date_rendezvous = date_rendezvous;
    }

  
    
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.entities;

import Clinimate.utils.DataSource;
import java.util.Date;

/**
 *
 * @author medam
 */
public class Consultation {
    java.sql.Connection cnx = DataSource.getInstance().getCnx();
    int id ;
    
    String notes ; 
    String prix ;
    int id_rendezvous_id ;
Rendez_vous r ; 

    public Consultation(int id, String notes, String prix, int id_rendezvous_id, Rendez_vous r) {
        this.id = id;
        this.notes = notes;
        this.prix = prix;
        this.id_rendezvous_id = id_rendezvous_id;
        this.r = r;
    }

    public Rendez_vous getR() {
        return r;
    }

    public void setR(Rendez_vous r) {
        this.r = r;
    }
    public Consultation() {
    }

    public Consultation(String notes, String prix, int id_rendezvous_id) {
        this.notes = notes;
        this.prix = prix;
        this.id_rendezvous_id = id_rendezvous_id;
    }

    public Consultation(int id, String notes, String prix, int id_rendezvous_id) {
        this.id = id;
        this.notes = notes;
        this.prix = prix;
        this.id_rendezvous_id = id_rendezvous_id;
    }

    @Override
    public String toString() {
        return notes ;
    }

    public Consultation(String notes, String prix) {
        this.notes = notes;
        this.prix = prix;
    }

    public Consultation(int id, String notes) {
        this.id = id;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public int getId_rendezvous_id() {
        return id_rendezvous_id;
    }

    public void setId_rendezvous_id(int id_rendezvous_id) {
        this.id_rendezvous_id = id_rendezvous_id;
    }
    
}
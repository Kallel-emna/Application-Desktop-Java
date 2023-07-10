/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.entities;

import Clinimate.utils.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author medam
 */
public class Ordonnance_1 {
    
 java.sql.Connection cnx = DataSource.getInstance().getCnx();
    int id ;
    
    String nompatient ; 
    Date date ;
    String medicament ;
    int consultation_id ;

    public int getConsultation_id() {
        return consultation_id;
    }

    public void setConsultation_id(int consultation_id) {
        this.consultation_id = consultation_id;
    }

    public Ordonnance_1() {
    }

    public Ordonnance_1(String nompatient, Date date, String medicament) {
        this.nompatient = nompatient;
        this.date = date;
        this.medicament = medicament;
    }

    public Ordonnance_1(int id, String nompatient, Date date, String medicament) {
        this.id = id;
        this.nompatient = nompatient;
        this.date = date;
        this.medicament = medicament;
    }

    public Ordonnance_1(String nompatient, Date date, String medicament, int consultation_id) {
        this.nompatient = nompatient;
        this.date = date;
        this.medicament = medicament;
        this.consultation_id = consultation_id;
    }

    public Ordonnance_1(int id, String medicament) {
        this.id = id;
        this.medicament = medicament;
    }

    public Ordonnance_1(int id, String nompatient, Date date, String medicament, int consultation_id) {
        this.id = id;
        this.nompatient = nompatient;
        this.date = date;
        this.medicament = medicament;
        this.consultation_id = consultation_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

  
   

   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Ordonnance{" + "date=" + date + '}';
    }

    public String getNompatient() {
        return nompatient;
    }

    public void setNompatient(String nompatient) {
        this.nompatient = nompatient;
    }



    public String getMedicament() {
        return medicament;
    }

    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }

    public String getPDFPath() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

  

  

 
}

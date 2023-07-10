/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.entities;

import java.util.Date;

/**
 *
 * @author LENOVO
 */
public class ReservationChambre  {
    int id;
    Date date_admission;
    Date date_sortie;
    Chambre chambre;
    User patient;

    public ReservationChambre(int id, Date date_admission, Date date_sortie, Chambre chambre, User patient) {
        this.id = id;
        this.date_admission = date_admission;
        this.date_sortie = date_sortie;
        this.chambre = chambre;
        this.patient = patient;
    }

    public ReservationChambre() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate_admission() {
        return date_admission;
    }

    public void setDate_admission(Date date_admission) {
        this.date_admission = date_admission;
    }

    public Date getDate_sortie() {
        return date_sortie;
    }

    public void setDate_sortie(Date date_sortie) {
        this.date_sortie = date_sortie;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }
            
}

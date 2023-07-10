/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.entities;

/**
 *
 * @author rania
 */
public class DossierMedical {
    int id ; 
    String certificat ; 
    String groupe_sanguin ; 
    int ordonnance_id ;
    Ordonnance ordonnance ; 

    public Ordonnance getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }

    public DossierMedical(String certificat, String groupe_sanguin, int ordonnance_id, Ordonnance ordonnance) {
        this.certificat = certificat;
        this.groupe_sanguin = groupe_sanguin;
        this.ordonnance_id = ordonnance_id;
        this.ordonnance = ordonnance;
    }

    public DossierMedical(int id, String certificat, String groupe_sanguin, int ordonnance_id, Ordonnance ordonnance) {
        this.id = id;
        this.certificat = certificat;
        this.groupe_sanguin = groupe_sanguin;
        this.ordonnance_id = ordonnance_id;
        this.ordonnance = ordonnance;
    }
    

    public DossierMedical() {
    }

    public DossierMedical(String certificat, String groupe_sanguin, int ordonnance_id) {
        this.certificat = certificat;
        this.groupe_sanguin = groupe_sanguin;
        this.ordonnance_id = ordonnance_id;
    }

    public DossierMedical(int id, String certificat, String groupe_sanguin, int ordonnance_id) {
        this.id = id;
        this.certificat = certificat;
        this.groupe_sanguin = groupe_sanguin;
        this.ordonnance_id = ordonnance_id;
    }

    public int getOrdonnance_id() {
        return ordonnance_id;
    }

    public void setOrdonnance_id(int ordonnance_id) {
        this.ordonnance_id = ordonnance_id;
    }

    public DossierMedical(int id, String certificat) {
        this.id = id;
        this.certificat = certificat;
    }

    @Override
    public String toString() {
        return  certificat ;
    }

    public DossierMedical(String certificat, String groupe_sanguin) {
        this.certificat = certificat;
        this.groupe_sanguin = groupe_sanguin;
    }

    public DossierMedical(int id, String certificat, String groupe_sanguin) {
        this.id = id;
        this.certificat = certificat;
        this.groupe_sanguin = groupe_sanguin;
    }
    

    public void setId(int id) {
        this.id = id;
    }

    public void setCertificat(String certificat) {
        this.certificat = certificat;
    }

    public void setGroupe_sanguin(String groupe_sanguin) {
        this.groupe_sanguin = groupe_sanguin;
    }

    public int getId() {
        return id;
    }

    public String getCertificat() {
        return certificat;
    }

    public String getGroupe_sanguin() {
        return groupe_sanguin;
    }
    
    
}

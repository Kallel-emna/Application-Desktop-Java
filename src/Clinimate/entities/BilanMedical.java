/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.entities;

import Clinimate.entities.User;


/**
 *
 * @author rania
 */
public class BilanMedical {
    

    int id ; 
    String antecedents ; 
    String taille ; 
    String poids ; 
    String examens_biologiques ; 
     String imagerie_medicale ;
      int user_id ; 
      int dossier_id ;
      User user ; 
     DossierMedical dossier ; 

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DossierMedical getDossier() {
        return dossier;
    }

    public void setDossier(DossierMedical dossier) {
        this.dossier = dossier;
    }

    public BilanMedical(int id, String antecedents, String taille, String poids, String examens_biologiques, String imagerie_medicale, int user_id, int dossier_id, User user, DossierMedical dossier) {
        this.id = id;
        this.antecedents = antecedents;
        this.taille = taille;
        this.poids = poids;
        this.examens_biologiques = examens_biologiques;
        this.imagerie_medicale = imagerie_medicale;
        this.user_id = user_id;
        this.dossier_id = dossier_id;
        this.user = user;
        this.dossier = dossier;
    }

    public BilanMedical(String antecedents, String taille, String poids, String examens_biologiques, String imagerie_medicale, int user_id, int dossier_id, User user, DossierMedical dossier) {
        this.antecedents = antecedents;
        this.taille = taille;
        this.poids = poids;
        this.examens_biologiques = examens_biologiques;
        this.imagerie_medicale = imagerie_medicale;
        this.user_id = user_id;
        this.dossier_id = dossier_id;
        this.user = user;
        this.dossier = dossier;
    }
      

    public BilanMedical(int id, String antecedents, String taille, String poids, String examens_biologiques, String imagerie_medicale, int user_id, int dossier_id) {
        this.id = id;
        this.antecedents = antecedents;
        this.taille = taille;
        this.poids = poids;
        this.examens_biologiques = examens_biologiques;
        this.imagerie_medicale = imagerie_medicale;
        this.user_id = user_id;
        this.dossier_id = dossier_id;
    }

    public BilanMedical(int id) {
        this.id = id;
    }

    public BilanMedical(String antecedents, String taille) {
        this.antecedents = antecedents;
        this.taille = taille;
    }

    public int getDossier_id() {
        return dossier_id;
    }

    public void setDossier_id(int dossier_id) {
        this.dossier_id = dossier_id;
    }

    public BilanMedical() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public BilanMedical(int id, String antecedents, String taille, String poids, String examens_biologiques, String imagerie_medicale, int user_id) {
        this.id = id;
        this.antecedents = antecedents;
        this.taille = taille;
        this.poids = poids;
        this.examens_biologiques = examens_biologiques;
        this.imagerie_medicale = imagerie_medicale;
        this.user_id = user_id;
    }


   
  
    public BilanMedical(int id, String antecedents, String taille, String poids, String examens_biologiques, String imagerie_medicale) {
        this.id = id;
        this.antecedents = antecedents;
        this.taille = taille;
        this.poids = poids;
        this.examens_biologiques = examens_biologiques;
        this.imagerie_medicale = imagerie_medicale;
    }

    public BilanMedical(String antecedents, String taille, String poids, String examens_biologiques, String imagerie_medicale) {
        this.antecedents = antecedents;
        this.taille = taille;
        this.poids = poids;
        this.examens_biologiques = examens_biologiques;
        this.imagerie_medicale = imagerie_medicale;
    }

 
 

    public int getId() {
        return id;
    }

    public String getAntecedents() {
        return antecedents;
    }

    public String getTaille() {
        return taille;
    }

    public String getPoids() {
        return poids;
    }

    public String getExamens_biologiques() {
        return examens_biologiques;
    }

    public String getImagerie_medicale() {
        return imagerie_medicale;
    }
    

    public void setId(int id) {
        this.id = id;
    }

    public void setAntecedents(String antecedents) {
        this.antecedents = antecedents;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }

    public void setExamens_biologiques(String examens_biologiques) {
        this.examens_biologiques = examens_biologiques;
    }

    public void setImagerie_medicale(String imagerie_medicale) {
        this.imagerie_medicale = imagerie_medicale;
    }

    public BilanMedical(int id, String antecedents) {
        this.id = id;
        this.antecedents = antecedents;
    }
    
    
}
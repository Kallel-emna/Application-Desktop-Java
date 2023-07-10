/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.entities;

/**
 *
 * @author LENOVO
 */
public class Chambre extends Service {
    int id;
    String libelle_chambre;
    int etage;
    int capacite;
    double prix;
    Service service;

    public Chambre(String libelle_chambre, int etage, int capacite, double prix, Service service) {
        this.libelle_chambre = libelle_chambre;
        this.etage = etage;
        this.capacite = capacite;
        this.prix = prix;
        this.service = service;
    }

    public String getLibelle_chambre() {
        return libelle_chambre;
    }

    public void setLibelle_chambre(String libelle_chambre) {
        this.libelle_chambre = libelle_chambre;
    }

   public Chambre(int id, Service service, int etage, int capacite, double prix) {
        this.id = id;
        this.service = service;
        this.etage = etage;
        this.capacite = capacite;
        this.prix = prix;
    }

    public Chambre(int id, String libelle_chambre) {
        this.id = id;
        this.libelle_chambre = libelle_chambre;
    }

  
  @Override
    public String toString() {
        return "" + libelle_chambre;
    }
    public Chambre() {
        
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getEtage() {
        return etage;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
    public void setChambre(int id) {
        this.id = id;
    }
}

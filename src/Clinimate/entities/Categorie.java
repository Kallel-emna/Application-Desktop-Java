/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.entities;

/**
 *
 * @author kalle
 */
public class Categorie {
    private Integer id;
    private String nom_cat;

    public Categorie(Integer id, String nom_cat) {
        this.id = id;
        this.nom_cat = nom_cat;
    }

    public Categorie(String nom_cat) {
        this.nom_cat = nom_cat;
    }

    public Categorie() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom_cat() {
        return nom_cat;
    }

    public void setNom_cat(String nom_cat) {
        this.nom_cat = nom_cat;
    }
    
    @Override
    public String toString() {
        return nom_cat ;
    }
    
    
}

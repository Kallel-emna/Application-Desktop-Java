/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.entities;

/**
 *
 * @author kalle
 */
public class Produit {
    private Integer id;
    private String nom_produit;
    private Integer quantite;
    private Integer prix;
    private Integer categorie_id;
    Categorie cat ; 

    public Produit(String nom_produit, Integer quantite, Integer prix, Integer categorie_id, Categorie cat) {
        this.nom_produit = nom_produit;
        this.quantite = quantite;
        this.prix = prix;
        this.categorie_id = categorie_id;
        this.cat = cat;
    }

    public Produit(Integer id, String nom_produit, Integer quantite, Integer prix, Integer categorie_id, Categorie cat) {
        this.id = id;
        this.nom_produit = nom_produit;
        this.quantite = quantite;
        this.prix = prix;
        this.categorie_id = categorie_id;
        this.cat = cat;
    }

    public Categorie getCat() {
        return cat;
    }

    public void setCat(Categorie cat) {
        this.cat = cat;
    }
    
    
    public Produit() {
    }

    public Produit(Integer id, String nom_produit, Integer quantite, Integer prix, Integer categorie_id) {
        this.id = id;
        this.nom_produit = nom_produit;
        this.quantite = quantite;
        this.prix = prix;
        this.categorie_id = categorie_id;
    }

    public Produit(String nom_produit, Integer quantite, Integer prix, Integer categorie_id) {
        this.nom_produit = nom_produit;
        this.quantite = quantite;
        this.prix = prix;
        this.categorie_id = categorie_id;
    }

    public Produit(Integer id, String nom_produit, Integer quantite, Integer prix) {
        this.id = id;
        this.nom_produit = nom_produit;
        this.quantite = quantite;
        this.prix = prix;
    }
    
    public Produit(Integer id, String nom_produit, Integer prix) {
        this.id = id;
        this.nom_produit = nom_produit;
        this.prix = prix;
    }

    public Produit(Integer id) {
        this.id = id;
    }


    public String getNom_produit() {
        return nom_produit;
    }

    public void setNom_produit(String nom_produit) {
        this.nom_produit = nom_produit;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getPrix() {
        return prix;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public Integer getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(Integer categorie_id) {
        this.categorie_id = categorie_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    

    @Override
    public String toString() {
        return "Produit{" + "id=" + id + ", nom_produit=" + nom_produit + ", quantite=" + quantite + ", prix=" + prix + '}';
    }
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Clinimate.service;

import Clinimate.entities.Produit;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kalle
 * @param <P>
 */
public interface IServiceProduit<P> {
    
    public void ajouterProduit(Produit p);
    public List<Produit> afficherProduit();
    public void modifierProduit (int id, String np, int q, int p, int idcat);
    public void supprimerProduit(Produit p);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Clinimate.service;

import Clinimate.entities.Categorie;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kalle
 * @param <C>
 */
public interface IServiceCategorie<C> {
    
    public void ajouterCategorie(Categorie c);
    public List<Categorie> afficherCategorie();
    public void modifierCategorie (int id, String nc);
    public void supprimerCategorie(Categorie c);
}

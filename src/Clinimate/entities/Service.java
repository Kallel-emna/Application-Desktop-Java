package Clinimate.entities;

import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author LENOVO
 */
public class Service {
    int id;
    String nom_service;

    public Service(String nom_service) {
        this.nom_service = nom_service;
    }

    public Service(int id, String nom_service) {
        this.id = id;
        this.nom_service = nom_service;
    }
@Override
    public String toString() {
        return nom_service;
    }
   
    public Service(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_service() {
        return nom_service;
    }

    public int getId_service() {
        return id;
    }

    public void setNom_service(String nom_service) {
        this.nom_service = nom_service;
    }

    public Service() {
    }
 

}

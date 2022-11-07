/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.projets5encheres;

/**
 *
 * @author i5e330
 */
public class Utilisateur {
    
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String pass;
    private String codepostal;
    
    public Utilisateur (int id, String nom, String prenom, String email, String pass, String codepostal) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pass = pass;
        this.codepostal = codepostal;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getCodepostal() {
        return codepostal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }
    
    
    
}

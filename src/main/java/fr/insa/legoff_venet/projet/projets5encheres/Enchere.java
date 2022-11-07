/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.projets5encheres;

import java.sql.Timestamp;

/**
 *
 * @author i5e330
 */
public class Enchere {
    
    private int id;
    private int de;
    private int sur;
    private Timestamp quand;
    private int montant;
    
    public Enchere (int id, int de, int sur, Timestamp quand, int montant) {
        this.id = id;
        this.de = de;
        this.sur = sur;
        this.quand = quand;
        this.montant = montant;
    }

    public int getId() {
        return id;
    }

    public int getDe() {
        return de;
    }

    public int getSur() {
        return sur;
    }

    public Timestamp getQuand() {
        return quand;
    }

    public int getMontant() {
        return montant;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDe(int de) {
        this.de = de;
    }

    public void setSur(int sur) {
        this.sur = sur;
    }

    public void setQuand(Timestamp quand) {
        this.quand = quand;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }
    
}

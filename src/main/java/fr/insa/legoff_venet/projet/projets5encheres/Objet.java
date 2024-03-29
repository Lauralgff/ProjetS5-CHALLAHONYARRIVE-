/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.projets5encheres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author i5e330
 */
public class Objet {

    private int id;
    private String titre;
    private String description;
    private Timestamp debut;
    private Timestamp fin;
    private int prixbase;
    private int categorie;
    private int proposepar;
    
    private int mMax;
    private String nomCat;
    private String close;
    private int idDe;
    private String nomDe;

    public Objet(int id, String titre, String description, Timestamp debut,
            Timestamp fin, int prixbase, int categorie, int proposepar) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.debut = debut;
        this.fin = fin;
        this.prixbase = prixbase;
        this.categorie = categorie;
        this.proposepar = proposepar;
    }
    
    public Objet(int id, String titre, String description, Timestamp debut,
            Timestamp fin, int prixbase, int categorie, int proposepar, 
            int mMax, String nomCat, String close, int idDe, String nomDe) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.debut = debut;
        this.fin = fin;
        this.prixbase = prixbase;
        this.categorie = categorie;
        this.proposepar = proposepar;
        this.mMax = mMax;
        this.nomCat = nomCat;
        this.close = close;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getDebut() {
        return debut;
    }

    public Timestamp getFin() {
        return fin;
    }

    public int getPrixbase() {
        return prixbase;
    }

    public int getCategorie() {
        return categorie;
    }

    public int getProposepar() {
        return proposepar;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDebut(Timestamp debut) {
        this.debut = debut;
    }

    public void setFin(Timestamp fin) {
        this.fin = fin;
    }

    public void setPrixbase(int prixbase) {
        this.prixbase = prixbase;
    }

    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }

    public void setProposepar(int proposepar) {
        this.proposepar = proposepar;
    }

    public int getmMax() {
        return mMax;
    }

    public String getNomCat() {
        return nomCat;
    }

    public void setmMax(int mMax) {
        this.mMax = mMax;
    }

    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }

    public static int getIdObjetFromTitre(Connection con, String titre)
            throws SQLException {
        int id = 0;
        try ( PreparedStatement pst = con.prepareStatement(
                """
            select objet1.id from objet1 where objet1.titre = ?
            """)) {
            pst.setString(1, titre);
            try ( ResultSet rs = pst.executeQuery()) {
                rs.next();
                id = rs.getInt("id");
            }
        } catch (SQLException ex) {
            throw ex;
        }
        System.out.println(id);
        return id;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public void setIdDe(int idDe) {
        this.idDe = idDe;
    }

    public void setNomDe(String nomDe) {
        this.nomDe = nomDe;
    }

    public String getClose() {
        return close;
    }

    public int getIdDe() {
        return idDe;
    }

    public String getNomDe() {
        return nomDe;
    }

}

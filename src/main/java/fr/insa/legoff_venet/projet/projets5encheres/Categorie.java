/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.projets5encheres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author i5e330
 */
public class Categorie {
    
    private int id;
    private String nom;
    
    public Categorie (int id, String nom){
        this.id = id;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public static int getIdCatFromNom(Connection con, String nom) throws SQLException {
        int id = 0;
        try ( PreparedStatement pst = con.prepareStatement(
                """
            select categorie1.id from categorie1 where categorie1.nom = ?
            """)) {
            pst.setString(1, nom);
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
    
}

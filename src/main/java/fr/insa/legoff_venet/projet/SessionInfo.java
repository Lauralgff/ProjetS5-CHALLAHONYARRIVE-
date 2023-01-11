/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet;

import fr.insa.legoff_venet.projet.projets5encheres.Utilisateur;
import java.sql.Connection;
import java.util.Optional;

/**
 *
 * @author laura
 */
public class SessionInfo {

    private Optional<Utilisateur> currentUser;
    private Connection con;

    public SessionInfo() {
        this.currentUser = Optional.empty();
        this.con = null;
    }

    public boolean userConnected() {
        return this.currentUser.isPresent();
    }

    public Optional<Utilisateur> getCurrentUser() {
        return currentUser;
    }

    public Connection getCon() {
        return con;
    }

    public void setCurrentUser(Optional<Utilisateur> currentUser) {
        this.currentUser = currentUser;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public int getUserId() {
        return this.currentUser.orElseThrow().getId();
    }

    public String getUserNom() {
        return this.currentUser.orElseThrow().getNom();
    }
    
    public String getUserPrenom() {
        return this.currentUser.orElseThrow().getPrenom();
    }
    
    public String getUserEmail() {
        return this.currentUser.orElseThrow().getEmail();
    }
    
    public String getUserCodePostal() {
        return this.currentUser.orElseThrow().getCodepostal();
    }

}

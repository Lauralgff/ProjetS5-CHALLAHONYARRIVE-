/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import fr.insa.legoff_venet.projet.VuePrincipale;
import fr.insa.legoff_venet.projet.projets5encheres.Objet;
import fr.insa.legoff_venet.projet.projets5encheres.ProjetS5Encheres;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author laura
 */
public class MesEncheres extends MyVerticalLayout {
    private VuePrincipale main;
        
    public MesEncheres (VuePrincipale main){
    this.main = main;
    this.add(new H1("Mes Enchères"));
    
    //Button de retour à la page profil
    Button home = new Button(new Icon(VaadinIcon.USER));
    home.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
    home.addClickListener((event)-> {
        this.main.setMainContent(new AfficheProfil(this.main));
    });
        
    add (home);
    
    //Affichage de mes objets à vendre
    Connection con = this.main.getSessionInfo().getCon();
    int id = this.main.getSessionInfo().getUserId();
        Grid<Objet> TabObjet = new Grid<>(Objet.class, false);
        TabObjet.addColumn(Objet::getTitre).setHeader("Titre de l'objet");
        TabObjet.addColumn(Objet::getDescription).setHeader("Description");
        TabObjet.addColumn(Objet::getPrixbase).setHeader("Prix de départ €");
        TabObjet.addColumn(Objet::getmMax).setHeader("Dernière enchère");
        TabObjet.addColumn(Objet::getFin).setHeader("Fin de l'enchère");
        TabObjet.addColumn(Objet::getNomCat).setHeader("Catégorie");

        try {

            List<Objet> ListObjet = ProjetS5Encheres.mesEncheres(con,id);
            TabObjet.setItems(ListObjet);
            this.add(TabObjet);
        } catch (SQLException ex) {
            Notification.show("Problème interne : " + ex.getLocalizedMessage());
        }
    }
}

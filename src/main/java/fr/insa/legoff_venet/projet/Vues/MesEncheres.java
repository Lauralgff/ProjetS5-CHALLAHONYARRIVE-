/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
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
    public Button Deconnexion = new Button(new Icon(VaadinIcon.POWER_OFF));
    Grid<Objet> TabObjet = new Grid<>(Objet.class, false);
    
    public MesEncheres (VuePrincipale main){
    this.main = main;
    this.add(new H1("Mes Enchères"));
    
    //Button de retour à la page profil
    Button home = new Button(new Icon(VaadinIcon.USER));
    home.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
    home.addClickListener((event)-> {
        this.main.setMainContent(new AfficheProfil(this.main));
    });
    
    //Bouton de deconnexion, retour à la page d'accueil
    this.Deconnexion.addThemeVariants(ButtonVariant.LUMO_ERROR);
    this.Deconnexion.addClickListener((event) -> {
        this.main.setMainContent(new PageAccueil(this.main));
        this.main.entete.removeAll(); 
    });
    
    add (home, Deconnexion);
    
    //Affichage de mes objets à vendre
    Connection con = this.main.getSessionInfo().getCon();
    int id = this.main.getSessionInfo().getUserId();
        
        TabObjet.addColumn(Objet::getId).setHeader("Identifiant");
        TabObjet.addColumn(Objet::getTitre).setHeader("Titre de l'objet");
        TabObjet.addColumn(Objet::getDescription).setHeader("Description");
        TabObjet.addColumn(Objet::getPrixbase).setHeader("Prix de départ €");
        TabObjet.addColumn(Objet::getmMax).setHeader("Dernière enchère");
        TabObjet.addColumn(Objet::getFin).setHeader("Fin de l'enchère");
        TabObjet.addColumn(Objet::getNomCat).setHeader("Catégorie");
        TabObjet.addColumn(Objet::getClose).setHeader("Statut");

        try {

            List<Objet> ListObjet = ProjetS5Encheres.mesEncheres(con,id);
            TabObjet.setItems(ListObjet);
            this.add(TabObjet);
        } catch (SQLException ex) {
            Notification.show("Problème interne : " + ex.getLocalizedMessage());
        }
   
// -----------------------------POP UP ENCHERIR--------------------------------------

   Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Encherir");

        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
        this.TabObjet.addItemClickListener(e ->
            dialog.open()
        );
        add(dialog);   
    }

    private static VerticalLayout createDialogLayout() {

        TextField IdObj = new TextField("Inserer l'id de l'objet");
        TextField encherir = new TextField("Votre enchère");

        VerticalLayout dialogLayout = new VerticalLayout(IdObj,
                encherir);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    private static Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Enchérir", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);

        return saveButton;
    }
 }

    
    
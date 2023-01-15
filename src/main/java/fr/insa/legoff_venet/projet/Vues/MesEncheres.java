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

    private Dialog dialog;
    private Button saveButton;
    private Button cancelButton;
    private VerticalLayout dialogLayout;
    private TextField IdObj;
    private TextField encherir;

    public MesEncheres(VuePrincipale main) {
        this.main = main;
        this.add(new H1("Mes Enchères"));

        //Button de retour à la page profil
        Button home = new Button(new Icon(VaadinIcon.USER));
        home.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        home.addClickListener((event) -> {
            this.main.setMainContent(new AfficheProfil(this.main));
        });

        //Bouton de deconnexion, retour à la page d'accueil
        this.Deconnexion.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.Deconnexion.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueil(this.main));
            this.main.entete.removeAll();
        });

        add(home, Deconnexion);

        //Affichage de mes objets à vendre
        Connection con = this.main.getSessionInfo().getCon();
        int id = this.main.getSessionInfo().getUserId();

        TabObjet.addColumn(Objet::getId).setHeader("Id");
        TabObjet.addColumn(Objet::getTitre).setHeader("Titre de l'objet");
        TabObjet.addColumn(Objet::getDescription).setHeader("Description");
        TabObjet.addColumn(Objet::getPrixbase).setHeader("Prix de départ €");
        TabObjet.addColumn(Objet::getmMax).setHeader("Dernière enchère");
        TabObjet.addColumn(Objet::getNomDe).setHeader("Dernier enchérisseur");
        TabObjet.addColumn(Objet::getFin).setHeader("Fin de l'enchère");
        TabObjet.addColumn(Objet::getNomCat).setHeader("Catégorie");
        TabObjet.addColumn(Objet::getClose).setHeader("Statut");

        try {

            List<Objet> ListObjet = ProjetS5Encheres.mesEncheres(con, id);
            TabObjet.setItems(ListObjet);
            this.add(TabObjet);
        } catch (SQLException ex) {
            Notification.show("Problème interne : " + ex.getLocalizedMessage());
        }

// -----------------------------POP UP ENCHERIR--------------------------------------
        dialog = new Dialog();

        dialog.setHeaderTitle("Encherir");

        this.IdObj = new TextField("Inserer l'id de l'objet");
        this.encherir = new TextField("Votre enchère");
        this.dialogLayout = new VerticalLayout(IdObj, encherir);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
        dialog.add(dialogLayout);

        this.saveButton = createSaveButton(dialog);
        saveButton.addClickListener((event) -> {
            doEncherir();
        });
        
        //Défintion du pop up
        this.cancelButton = new Button("Cancel", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
        this.TabObjet.addItemClickListener(e
                -> dialog.open()
        );
        add(dialog);
    }

    private static Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Enchérir");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        return saveButton;
    }

    
//--------------------- Méthode d'insertion d'une enchère ---------------------
    public void doEncherir() {
        String idObjetS = this.IdObj.getValue();
        String montantS = this.encherir.getValue();
        int res = 0;
        try {
            Connection con = this.main.getSessionInfo().getCon();
            if (idObjetS.isBlank() || montantS.isBlank()) {
                Notification.show("Veuillez remplir tous les champs.");
            } else if (!ProjetS5Encheres.idObjetExiste(con,
                    Integer.parseInt(this.IdObj.getValue()))) {
                Notification.show("L'objet choisi n'existe pas.");
            } else if (!ProjetS5Encheres.finiOuPas(con,
                    Integer.parseInt(this.IdObj.getValue()))) {
                Notification.show("La vente est terminée, vous ne pouvez "
                        + "plus placer d'enchère.");
            } else {
                int idObjet = Integer.parseInt(this.IdObj.getValue());
                int montant = Integer.parseInt(this.encherir.getValue());
                res = ProjetS5Encheres.createEnchere(con, this.main.getSessionInfo().getUserId(),
                        idObjet, montant);
                if (res == -1) {
                    Notification.show("Le montant proposé doit être supérieur "
                            + "à celui de la dernière enchère ou au prix de départ.");
                } else {
                    dialog.close();
                    Notification.show("Enchère placée!");
                }
            }
        } catch (SQLException ex) {
            Notification.show("Problème interne : " + ex.getLocalizedMessage());
        }
    }

}

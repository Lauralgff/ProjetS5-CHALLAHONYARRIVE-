/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import fr.insa.legoff_venet.projet.VuePrincipale;
import fr.insa.legoff_venet.projet.projets5encheres.Objet;
import fr.insa.legoff_venet.projet.projets5encheres.ProjetS5Encheres;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author laura
 */
public class PageAccueilSite extends MyVerticalLayout {

    public VuePrincipale main;
//    private List<String> items = new ArrayList<>(
//            Arrays.asList("Vêtements", "Meubles", "Animaux", "Sport", 
//                    "Jeux/Jouets", "Automobile", "Bricolage", "Décoration"));
    private List<String> items = new ArrayList<>();
    public TextField ResearchBar = new TextField();
    public ComboBox<String> RechercheCat = new ComboBox<>();
    public Button Deconnexion = new Button(new Icon(VaadinIcon.POWER_OFF));
    public Button Profil = new Button("Mon profil", new Icon(VaadinIcon.USER));
    public Button AVendre = new Button("Vendre", new Icon(VaadinIcon.WALLET));
    public Button ActRechercheText = new Button ("Rechercher", new Icon(VaadinIcon.SEARCH));
    public Button ActRechercheCat = new Button ("Rechercher par catégorie", new Icon(VaadinIcon.SEARCH));
    private Grid<Objet> tableau;
    

    public PageAccueilSite(VuePrincipale main) {

        
        this.main = main;
        
//------------------------ AFFICHAGE ENTETE ---------------------------------------        
        
        //Bouton de deconnexion, retour à la page d'accueil      
        this.Deconnexion.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.Deconnexion.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueil(this.main));
            this.main.entete.removeAll();
        });

        //Bouton profil, entete
        this.Profil.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        this.Profil.addClickListener((event) -> {
            this.main.setMainContent(new AfficheProfil(this.main));
            /*this.main.entete.remove(ResearchBar, RechercheCat,
                    ActRechercheCat, ActRechercheText);*/
            this.main.entete.removeAll();
        });

        //Bouton Vendre, entete
        this.AVendre.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        this.AVendre.addClickListener((event) -> {
            this.main.setMainContent(new CreerEncheres(this.main));
            /*this.main.entete.remove(ResearchBar, RechercheCat,
                ActRechercheCat, ActRechercheText);*/  
            this.main.entete.removeAll();
        });

        this.main.entete.add(Profil, AVendre);
        
        Connection con = this.main.getSessionInfo().getCon();
        
        //Barre de recherche textuelle
        this.ResearchBar.setPlaceholder("Search");
        this.ResearchBar.setPrefixComponent(VaadinIcon.SEARCH.create());
        
        this.ActRechercheText.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        this.ActRechercheText.addClickListener((e) -> {
            this.main.mainContent.removeAll();
            String motcle = ResearchBar.getValue(); 
            this.tableau = new Grid<>(Objet.class, false);
            this.tableau.addColumn(Objet::getTitre).setHeader("Titre de l'objet");
            this.tableau.addColumn(Objet::getDescription).setHeader("Description");
            this.tableau.addColumn(Objet::getPrixbase).setHeader("Prix de départ €");
            this.tableau.addColumn(Objet::getmMax).setHeader("Dernière enchère");
            this.tableau.addColumn(Objet::getFin).setHeader("Fin de l'enchère");
            this.tableau.addColumn(Objet::getNomCat).setHeader("Catégorie");
            List<Objet> listeobjet;
            try {
                listeobjet = ProjetS5Encheres.objetsRecherche(con, motcle);
                this.tableau.setItems(listeobjet);
            } catch (SQLException ex) {
                Logger.getLogger(AfficheProfil.class.getName()).log(Level.SEVERE, null, ex);
            }       
            this.main.mainContent.add(this.tableau);
        });
        
        //Barre de recherche par catégories
        this.RechercheCat.setAllowCustomValue(true);
        this.RechercheCat.setPlaceholder("Rechercher par catégorie");
        try {
        items = ProjetS5Encheres.listeNomCat(con);
        } catch (SQLException ex) {
            
        }
        this.RechercheCat.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            items.add(customValue);
            RechercheCat.setItems(items);
            RechercheCat.setValue(customValue);
        });
        RechercheCat.setItems(items);
        
        this.ActRechercheCat.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        this.ActRechercheCat.addClickListener((e) -> {
            this.main.mainContent.removeAll();
            
            String categorie = RechercheCat.getValue();
            this.tableau = new Grid<>(Objet.class, false);
            this.tableau.addColumn(Objet::getTitre).setHeader("Titre de l'objet");
            this.tableau.addColumn(Objet::getDescription).setHeader("Description");
            this.tableau.addColumn(Objet::getPrixbase).setHeader("Prix de départ €");
            this.tableau.addColumn(Objet::getmMax).setHeader("Dernière enchère");
            this.tableau.addColumn(Objet::getFin).setHeader("Fin de l'enchère");
            this.tableau.addColumn(Objet::getNomCat).setHeader("Catégorie");
            List<Objet> listeobjet;
            try {
                listeobjet = ProjetS5Encheres.objetsRechercheCat (con, categorie);
                this.tableau.setItems(listeobjet);
            } catch (SQLException ex) {
                Logger.getLogger(AfficheProfil.class.getName()).log(Level.SEVERE, null, ex);
            }       
            this.main.mainContent.add(this.tableau);
        });
        
        this.main.entete.add(ResearchBar, ActRechercheText,
                RechercheCat,ActRechercheCat, Deconnexion);
        
//------------------------ AFFICHAGE MAIN CONTENT --------------------------------     
        
//      Affichage tableau objets en ventes

        Grid<Objet> TabObjet = new Grid<>(Objet.class, false);
        TabObjet.addColumn(Objet::getTitre).setHeader("Titre de l'objet");
        TabObjet.addColumn(Objet::getDescription).setHeader("Description");
        TabObjet.addColumn(Objet::getPrixbase).setHeader("Prix de départ €");
        TabObjet.addColumn(Objet::getmMax).setHeader("Dernière enchère");
        TabObjet.addColumn(Objet::getFin).setHeader("Fin de l'enchère");
        TabObjet.addColumn(Objet::getNomCat).setHeader("Catégorie");

        try {

            List<Objet> ListObjet = ProjetS5Encheres.ventesEnCours(con);
            TabObjet.setItems(ListObjet);
            this.add(TabObjet);
        } catch (SQLException ex) {
            Notification.show("Problème interne : " + ex.getLocalizedMessage());
        }
    }
}

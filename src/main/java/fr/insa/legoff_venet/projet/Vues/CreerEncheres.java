/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.nimbusds.jose.shaded.asm.ConvertDate;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import fr.insa.legoff_venet.projet.VuePrincipale;
import fr.insa.legoff_venet.projet.projets5encheres.Categorie;
import fr.insa.legoff_venet.projet.projets5encheres.Objet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import fr.insa.legoff_venet.projet.projets5encheres.ProjetS5Encheres;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;

/**
 *
 * @author laura
 */
public class CreerEncheres extends MyVerticalLayout {

    private VuePrincipale main;
    private List<String> items = new ArrayList<>();
    public TextField textfield = new TextField();
    public ComboBox<String> RechercheCat = new ComboBox<>();
    Button Deconnexion = new Button(new Icon(VaadinIcon.POWER_OFF));
    public Button Profil = new Button("Mon profil", new Icon(VaadinIcon.USER));
    public Button AVendre = new Button("Vendre", new Icon(VaadinIcon.WALLET));
    public Button ActRechercheText = new Button("Rechercher", new Icon(VaadinIcon.SEARCH));
    public Button ActRechercheCat = new Button("Rechercher par catégorie", new Icon(VaadinIcon.SEARCH));

    private Button valider;

    private TextField title;
    private TextField prix;
    private TextArea description;
    private DatePicker date;
    private TimePicker to;
    private ComboBox<String> ChoixCat;

    public CreerEncheres(VuePrincipale main) throws SQLException {

        this.main = main;

        Connection con = this.main.getSessionInfo().getCon();
        
        this.add(new H1("Créez votre enchère !"));
        title = new TextField("Titre *");
        prix = new TextField("Prix de départ *");
        prix.setSuffixComponent(new Span("€"));
        description = new TextArea();
        description.setWidthFull();
        description.setLabel("Description");
        date = new DatePicker("Date de fin *");
        ChoixCat = new ComboBox<>("Catégories *");
        ChoixCat.setAllowCustomValue(true);
        items = ProjetS5Encheres.listeNomCat(con);
        ChoixCat.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            items.add(customValue);
            ChoixCat.setItems(items);
            ChoixCat.setValue(customValue);
        });
        ChoixCat.setItems(items);

        FormLayout formLayout = new FormLayout();
        formLayout.add(title, prix, ChoixCat, description, date);
        formLayout.setColspan(description, 3);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));
        add(formLayout);

        /* //Barre de recherche textuelle
        this.textfield.setPlaceholder("Search");
        this.textfield.setPrefixComponent(VaadinIcon.SEARCH.create());*/

        //Bouton de deconnexion, retour à la page d'accueil
        this.Deconnexion.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.Deconnexion.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueil(this.main));
            this.main.entete.removeAll();
        });

        //Bouton profil, entete
        /*this.Profil.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        this.Profil.addClickListener((event) -> {
            this.main.setMainContent(new AfficheProfil(this.main));
        });

        //Bouton Vendre, entete
        this.AVendre.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        this.AVendre.addClickListener((event) -> {
            this.main.setMainContent(new CreerEncheres(this.main));
        });

        //Barre de recherche par catégories
        this.RechercheCat.setAllowCustomValue(true);
        this.RechercheCat.setPlaceholder("Rechercher par catégorie");
        this.RechercheCat.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            items.add(customValue);
            this.RechercheCat.setItems(items);
            this.RechercheCat.setValue(customValue);
        });
        this.RechercheCat.setItems(items);*/

        Button home = new Button(new Icon(VaadinIcon.HOME));
        home.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        home.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueilSite(this.main));
            //this.main.entete.removeAll();
            /*this.main.entete.add(Profil, AVendre,
                    textfield, ActRechercheText,
                    this.RechercheCat, ActRechercheCat, Deconnexion);*/
        });

        this.valider = new Button("Valider");
        valider.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        valider.addClickListener(clickEvent -> {
            doCreerEnchere();
            valider.setEnabled(false);

//            Notification notification = Notification
//                    .show("Enchère ajoutée :)");
//            notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
//            notification.setPosition(Notification.Position.MIDDLE);
//
//            notification
//                    .addDetachListener(detachEvent -> valider.setEnabled(true));
        });

        add(valider, home,Deconnexion);
        
    }

    public void doCreerEnchere() {
        Connection con = this.main.getSessionInfo().getCon();
        String titre = this.title.getValue();
//        int prixbase = Integer.parseInt(this.prix.getValue());
        String prixbaseS = this.prix.getValue();
        String descript = this.description.getValue();
//        Date dateFin = ConvertDate.convertToDate(this.date.getValue());
        Date dateFin = ProjetS5Encheres.convertToDateUsingInstant(this.date.getValue());
        long dateMillis = dateFin.getTime();
//        long dateMillis = dateFin.get
        Timestamp fin = new Timestamp(dateMillis);
        int proposepar = this.main.getSessionInfo().getUserId();
        int categorie = 0;
        if (dateMillis <= System.currentTimeMillis()) {
            Notification.show("La date de fin de l'enchère doit être "
                    + "ultérieure à la date actuelle.");
        } else if (titre.isBlank() || prixbaseS.isBlank() || dateMillis == 0) {
            Notification.show("Les champs * doivent tous être remplis.");
        } else {
            try {
                int prixbase = Integer.parseInt(prixbaseS);
                categorie = Categorie.getIdCatFromNom(con, this.ChoixCat.getValue());
                if (categorie == 0) {
                    Notification.show("Les champs * doivent tous être remplis.");
                } else {
                    ProjetS5Encheres.createObjet(con, titre, descript,
                            new Timestamp(System.currentTimeMillis()), fin,
                            prixbase, categorie, proposepar);
                    this.main.setMainContent(new PageAccueilSite(this.main));
                    /*this.main.entete.removeAll();
                    this.main.entete.add(Profil, AVendre, textfield,
                            RechercheCat, Deconnexion);*/
                    Notification.show("Vente créée! id de l'objet : "
                            + Objet.getIdObjetFromTitre(con, titre));
                }
            } catch (SQLException ex) {
                Notification.show("Problème interne : " + ex.getLocalizedMessage());
            }
        }
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.legoff_venet.projet.VuePrincipale;
import fr.insa.legoff_venet.projet.projets5encheres.ProjetS5Encheres;
import fr.insa.legoff_venet.projet.projets5encheres.Utilisateur;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


/**
 *
 * @author laura
 */
public class Inscription extends MyVerticalLayout {
    
    private VuePrincipale main;
    
    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextField postCode;
    private PasswordField password;
    private PasswordField confirmPassword;
    private Button valider;
    private Button retour;
    
    public Inscription (VuePrincipale main){
  
    this.main = main;
    
        this.add(new H1("Inscription"));
        
        this.firstName = new TextField("Prénom");
        this.lastName = new TextField("Nom");
        this.email = new TextField("E-mail");
        this.postCode = new TextField("Code postal");
        this.password = new PasswordField("Mot de passe");
        this.confirmPassword = new PasswordField("Confirmez votre mot de passe");
        

//        Button valider = new Button("Valider");
        this.valider = new Button("Valider");
        valider.setEnabled(false);
        valider.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        valider.addClickListener((event) -> {
            doSignUp();   
         });
        
        //activer le bouton valider
        confirmPassword.addInputListener(event ->  {
            valider.setEnabled(true);
        });
        
        //Bouton de retour à la page d'accueil
        this.retour = new Button ("Retour", new Icon(VaadinIcon.ARROW_LEFT));
        retour.addThemeVariants(ButtonVariant.LUMO_ERROR);
        retour.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueil(this.main));   
         });
        
        this.add(firstName,lastName, email, postCode, password,confirmPassword,valider, retour);
        
    
    }
    
//------------------------- Méthode d'inscription -----------------------------------   
    public void doSignUp() {
        String nom = this.lastName.getValue();
        String prenom = this.firstName.getValue();
        String email = this.email.getValue();
        String codepostal = this.postCode.getValue();
        String pass = this.password.getValue();
        String confPass = this.confirmPassword.getValue();
        try {
            Connection con = this.main.getSessionInfo().getCon();
            if (nom.isBlank() || prenom.isBlank() || email.isBlank() 
                    || codepostal.isBlank()) {
                Notification.show("Veuillez remplir tous les champs.");
            } else if (!pass.equals(confPass)) {
                Notification.show("Le mot de passe et celui de confirmation "
                        + "doivent être identiques.");
            } else {
                ProjetS5Encheres.createUtilisateur(con,nom,prenom,email,pass,codepostal);
                Optional<Utilisateur> user = ProjetS5Encheres.login2(con, email, pass);
                this.main.getSessionInfo().setCurrentUser(user);
                this.main.setMainContent(new PageAccueilSite(this.main));
                Notification.show("Inscription terminée");
                Notification.show("Bienvenue " 
                        + this.main.getSessionInfo().getUserPrenom()
                        + " (" + this.main.getSessionInfo().getUserId() +")");
            }
        } catch (SQLException | ProjetS5Encheres.EmailExisteDejaException ex) {
            Notification.show("Problème interne : " + ex.getLocalizedMessage());
        }
    }
    
}

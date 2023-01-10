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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.legoff_venet.projet.VuePrincipale;
import java.sql.Connection;
import fr.insa.legoff_venet.projet.projets5encheres.ProjetS5Encheres;
import fr.insa.legoff_venet.projet.projets5encheres.Utilisateur;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author laura
 */
public class Connexion extends MyVerticalLayout{    
    
    private VuePrincipale main;
    
    private TextField vemail;
    private PasswordField vpass;
    private Button vbLogin;
    private Button vbRetour;
              
    public Connexion(VuePrincipale main) {
        this.main = main;
//        TextField lastName = new TextField("Email");
        this.vemail = new TextField("Email");
//        PasswordField password = new PasswordField("Password"); 
        this.vpass = new PasswordField("Password");
//        Button login = new Button ("Connexion");
        this.vbLogin = new Button ("Connexion");
//        Button Retour = new Button ("Retour",new Icon(VaadinIcon.ARROW_LEFT));
        this.vbRetour = new Button ("Retour",new Icon(VaadinIcon.ARROW_LEFT));
//        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        this.vbLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
//        this.add(lastName, password, login, Retour);
        this.add(this.vemail, this.vpass, this.vbLogin, this.vbRetour);
//        login.addClickListener((event) -> {
//            this.main.setMainContent(new PageAccueilSite(this.main));   
//         });
        this.vbLogin.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueilSite(this.main));
            this.doLogin();
        });
//        Retour.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.vbRetour.addThemeVariants(ButtonVariant.LUMO_ERROR);
//        Retour.addClickListener((event) -> {
//            this.main.setMainContent(new PageAccueil(this.main));   
//         });
        this.vbRetour.addClickListener((event) -> {
            this.main.setMainContent(new PageAccueil(this.main));
        });
    }
    
    public void doLogin() {
        String email = this.vemail.getValue();
        String pass = this.vpass.getValue();
        try {
            Connection con = this.main.getSessionInfo().getCon();
            Optional<Utilisateur> user = ProjetS5Encheres.login2(con, email, pass);
            if (user.isEmpty()) {
                Notification.show("Email ou mot de passe invalide");
            } else {
                this.main.getSessionInfo().setCurrentUser(user);
            }
        } catch (SQLException ex) {
            Notification.show("Problème interne : " + ex.getLocalizedMessage());
        }
    }
}

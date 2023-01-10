/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.legoff_venet.projet.Vues;

import com.vaadin.flow.component.login.LoginForm;

/**
 *
 * @author i5e330
 */
public class LoginMain extends LoginForm {

    public LoginMain() {
        this.addLoginListener((event) -> {
            String email = event.getUsername();
            String pass = event.getPassword();
        });
    }

}

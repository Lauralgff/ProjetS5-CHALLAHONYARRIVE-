/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package fr.insa.legoff_venet.projet.projets5encheres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import fr.insa.legoff_venet.projet.utils.Lire;
import java.util.Date;
import java.util.Optional;
import fr.insa.legoff_venet.Session.Session;
import fr.insa.legoff_venet.projet.utils.Console;

/**
 *
 * @author i5e330
 */
public class ProjetS5Encheres {

    public static Connection connectGeneralPostGres(String host,
            int port, String database,
            String user, String pass)
            throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port
                + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }

    public static Connection defautConnect()
            throws ClassNotFoundException, SQLException {
        return connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
    }

    public static void creeSchema(Connection con)
            throws SQLException {
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {
            st.executeUpdate(
                    """
                    create table utilisateur1 (
                        id integer not null primary key
                          generated always as identity,
                        nom varchar(100) not null unique,
                        prenom varchar(50) not null,
                        email varchar(100) not null unique,
                        pass varchar(50) not null,
                        codepostal varchar(30) not null
                    )
                    """);
            st.executeUpdate(
                    """
                    create table categorie1 (
                        id integer not null primary key
                          generated always as identity,
                        nom varchar(50) not null unique
                    )
                    """);
            st.executeUpdate(
                    """
                    create table enchere1 (
                        id integer not null primary key
                          generated always as identity,
                        quand timestamp,
                        montant integer,
                        de integer,
                        sur integer
                    )
                    """);

            st.executeUpdate(
                    """
                    create table objet1 (
                        id integer not null primary key
                          generated always as identity,
                        titre varchar(200) not null unique,
                        description text,
                        debut timestamp,
                        fin timestamp,                        
                        prixbase integer,
                        proposepar integer,
                        categorie integer
                    )
                    """);
            st.executeUpdate(
                    """
                    alter table objet1
                        add constraint fk_objet1_proposepar
                        foreign key (proposepar) references utilisateur1(id)
                    """);
            st.executeUpdate(
                    """
                    alter table objet1
                        add constraint fk_objet1_categorie
                        foreign key (categorie) references categorie1(id)
                    """);
            st.executeUpdate(
                    """
                    alter table enchere1
                        add constraint fk_enchere1_de
                        foreign key (de) references utilisateur1(id)
                    """);
            st.executeUpdate(
                    """
                    alter table enchere1
                        add constraint fk_enchere1_sur
                        foreign key (sur) references objet1(id)
                    """);
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static void deleteSchema(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try {
                st.executeUpdate(
                        """
                        alter table objet1
                            drop constraint fk_objet1_proposepar
                        """);
                System.out.println("Constraint fk_objet1_proposepar dropped");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                alter table objet1
                    drop constraint fk_objet1_categorie
                """);
                System.out.println("Constraint fk_objet1_categorie dropped");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                alter table enchere1
                    drop constraint fk_enchere1_de
                """);
                System.out.println("Constraint fk_enchere1_de dropped");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                alter table enchere1
                    drop constraint fk_enchere1_sur
                """);
                System.out.println("Constraint fk_enchere1_sur dropped");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                drop table enchere1
                """);
                System.out.println("Table enchere1 dropped");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                drop table objet1
                """);
                System.out.println("Table objet1 dropped");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                drop table categorie1
                """);
                System.out.println("Table categorie1 dropped");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                drop table utilisateur1
                """);
                System.out.println("Table utilisateur1 dropped");
            } catch (SQLException ex) {
            }
        }
    }

    public static class NomExisteDejaException extends Exception {
    }

    public static class EmailExisteDejaException extends Exception {
    }

    public static int createUtilisateur(Connection con, String nom, String prenom,
            String email, String pass, String codepostal)
            throws SQLException, EmailExisteDejaException {
// M??thode pour cr??er un utilisateur d??fini dans le sch??ma de base ou lors de 
//l'inscription d'un nouvel utilisateur
        con.setAutoCommit(false);
        try ( PreparedStatement chercheEmail = con.prepareStatement(
                "select id from utilisateur1 where email = ?")) {
            chercheEmail.setString(1, nom);
            ResultSet testEmail = chercheEmail.executeQuery();
            if (testEmail.next()) {
                throw new EmailExisteDejaException();
            }
            try ( PreparedStatement pst = con.prepareStatement(
                    """
                    insert into utilisateur1 (nom,prenom,email,pass,codepostal) values (?,?,?,?,?) 
                    """, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, nom);
                pst.setString(2, prenom);
                pst.setString(3, email);
                pst.setString(4, pass);
                pst.setString(5, codepostal);
                pst.executeUpdate();
                con.commit();

                try ( ResultSet rid = pst.getGeneratedKeys()) {
                    rid.next();
                    int id = rid.getInt(1);
                    return id;
                }
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

//    public static int createUtilisateur2(Connection con)
//            throws SQLException {
//        con.setAutoCommit(false);
//        try ( PreparedStatement chercheNom = con.prepareStatement(
//                "select id from utilisateur1 where nom = ?")) {
//            try ( PreparedStatement pst = con.prepareStatement(
//                    """
//                    insert into utilisateur1 (nom,prenom,email,pass,codepostal) values (?,?,?,?,?) 
//                    """, PreparedStatement.RETURN_GENERATED_KEYS)) {
//                System.out.println("Nom : ");
//                pst.setString(1, Lire.S());
//                System.out.println("Pr??nom : ");
//                pst.setString(2, Lire.S());
//                System.out.println("e-mail : ");
//                pst.setString(3, Lire.S());
//                System.out.println("Mot de passe : ");
//                pst.setString(4, Lire.S());
//                System.out.println("Code postal : ");
//                pst.setString(5, Lire.S());
//                pst.executeUpdate();
//                con.commit();
//
//                try ( ResultSet rid = pst.getGeneratedKeys()) {
//                    rid.next();
//                    int id = rid.getInt(1);
//                    return id;
//                }
//            }
//        } catch (Exception ex) {
//            con.rollback();
//            throw ex;
//        } finally {
//            con.setAutoCommit(true);
//        }
//    }
    public static void demandeUtilisateur(Connection con) throws SQLException {
        boolean existe = true;
        while (existe) {
            System.out.println("Nouvel utilisateur");
            String nom = Console.entreeString("nom");
            String prenom = Console.entreeString("prenom");
            String email = Console.entreeString("email");
            String pass = Console.entreeString("pass");
            String codepostal = Console.entreeString("codepostal");
            try {
                createUtilisateur(con, nom, prenom, email, pass, codepostal);
                existe = false;
            } catch (EmailExisteDejaException ex) {
                System.out.println("Ce nom d'utilisateur existe d??j??.");
            }
        }
    }

// Retourne une date en fonction du nombre de jours s??parant la date d'aujourd'hui 
// de la date souhait??e
    public static Date GetDate(long nbjr) {
        long milliseconds = System.currentTimeMillis();
        Date date = new Date(milliseconds + nbjr * 86400000);
        // Il y a 86400000 ms dans une journ??e
        return date;
    }

// Convertit une date en Timestamp
    public static java.sql.Timestamp convert(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    public static int createCategorie(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                """
                insert into categorie1 (nom) values (?)
                """, Statement.RETURN_GENERATED_KEYS)) {
            System.out.println("Nom de la cat??gorie : ");
            pst.setString(1, Lire.S());
            pst.executeUpdate();
            con.commit();

            try ( ResultSet rid = pst.getGeneratedKeys()) {
                rid.next();
                int id = rid.getInt(1);
                return id;
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int createCategorie2(Connection con, String nom)
            throws SQLException {
// M??thode pour cr??er une cat??gorie d??finie dans le sch??ma de base
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                """
                insert into categorie1 (nom) values (?)
                """, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, nom);
            pst.executeUpdate();
            con.commit();

            try ( ResultSet rid = pst.getGeneratedKeys()) {
                rid.next();
                int id = rid.getInt(1);
                return id;
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int createObjet(Connection con, String titre, String description,
            Timestamp debut, Timestamp fin, int prixbase, int categorie, int proposepar)
            throws SQLException {
// M??thode pour cr??er un objet d??fini dans le sch??ma de base
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                """
            insert into objet1 (titre,description,debut,fin,prixbase,categorie,
                proposepar) values (?,?,?,?,?,?,?)
            """, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, titre);
            pst.setString(2, description);
            pst.setTimestamp(3, debut);
            pst.setTimestamp(4, fin);
            pst.setInt(5, prixbase);
            pst.setInt(6, categorie);
            pst.setInt(7, proposepar);
            pst.executeUpdate();
            con.commit();

            try ( ResultSet rid = pst.getGeneratedKeys()) {
                rid.next();
                int id = rid.getInt(1);
                return id;
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int createObjet2(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                """
            insert into objet1 (titre,description,debut,fin,prixbase,categorie,
                proposepar) values (?,?,?,?,?,?,?)
            """, Statement.RETURN_GENERATED_KEYS)) {
            System.out.println("Titre : ");
            pst.setString(1, Lire.S());
            System.out.println("Description : ");
            pst.setString(2, Lire.S());
            pst.setTimestamp(3, convert(GetDate(0)));
            System.out.println("Combien de jours durera l'ench??re ?");
            pst.setTimestamp(4, convert(GetDate(Lire.i())));
            System.out.println("Prix de base de l'objet (en centimes) : ");
            pst.setInt(5, Lire.i());
            System.out.println("Cat??gorie de l'objet : ");
            pst.setInt(6, Lire.i());
//TODO : faire en sorte que le nom d'utilisateur soit d??fini 
//      en fonction de l'utilisateur connect??
            System.out.println("Votre id d'utilisateur : ");
            pst.setInt(7, Lire.i());
            pst.executeUpdate();
            con.commit();

            try ( ResultSet rid = pst.getGeneratedKeys()) {
                rid.next();
                int id = rid.getInt(1);
                return id;
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static void demandeEnchere(Connection con) throws SQLException {
//        int idE = Session.getUserId();
        int idEncherisseur = Console.entreeEntier("Id de l'ench??risseur : ");
        int idObj = Console.entreeEntier("Id de l'objet : ");
        int montant = Console.entreeEntier("Montant propos??");
        int res = 0;
        if (finiOuPas(con, idObj) == false) {
            res = -2;
        } else {
            res = createEnchere(con, idEncherisseur, idObj, montant);
        }
        if (res == -1) {
            System.out.println("Le montant de votre ench??re est trop faible");
        } else if (res == -2) {
            System.out.println("La vente est termin??e, vous ne pouvez plus proposer d'ench??re.");
        } else {
            System.out.println("Votre ench??re a bien ??t?? enregistr??e. (id :" + res + ")");
        }

    }

    public static boolean finiOuPas(Connection con, int idObj) throws SQLException {
        boolean enCours = true;
//        con.setAutoCommit(false);
        try {
            String sqlChercheFin
                    = "select fin from objet1 where id = ?";
            PreparedStatement chercheF = con.prepareStatement(sqlChercheFin);
            chercheF.setInt(1, idObj);
            ResultSet rs = chercheF.executeQuery();
            rs.next();
            Timestamp fin = rs.getTimestamp(1);
            if (fin.getTime() <= System.currentTimeMillis()) {
                enCours = false;
            }
        } catch (SQLException ex) {
            System.out.println("Probl??me finiOuPas");
        }
        return enCours;
    }

//TODO : finaliser m??thode, param??trage de "de" et "sur" en fonction de 
    //   l'utilisateur connect?? et de l'objet choisi
    public static int createEnchere(Connection con, int de, int sur,
            int montant) throws SQLException {
        con.setAutoCommit(false);
        try {
            //d??but de transaction si false
            String sqlChercheEnchere
                    = "select max(montant)"
                    + " from enchere1"
                    + " where sur = ?";
            PreparedStatement chercheE = con.prepareStatement(sqlChercheEnchere);
            chercheE.setInt(1, sur);
            // statement = ordre sql
            ResultSet rs1 = chercheE.executeQuery();
            //resultSet = table
            rs1.next();
            int val = rs1.getInt(1);
            if (rs1.wasNull()) {
                String sqlChercheValBase
                        = "select prixbase"
                        + " from objet1"
                        + " where id = ?";
                PreparedStatement chercheVB = con.prepareStatement(sqlChercheValBase);
                chercheVB.setInt(1, sur);
                ResultSet rs2 = chercheVB.executeQuery();
                rs2.next();
                val = rs2.getInt("prixbase");
            }
            if (montant < val) {
                return -1;
            } else {
//                String sqlDateFin
//                        = "select fin from objet1 "
//                        + "where id = ?";
//                PreparedStatement chercheDateFin = con.prepareStatement(sqlDateFin);
//                chercheDateFin.setInt(1, sur);
//                ResultSet rs3 = chercheDateFin.executeQuery();
//                Timestamp fin = rs3.getTimestamp("fin");
//                if (fin.getTime() <= System.currentTimeMillis()){
//                    return -2;
//                } else {

                chercheE.setInt(1, sur);
                PreparedStatement pst = con.prepareStatement(
                        """
                insert into enchere1 (de,sur,quand,montant) values (?,?,?,?)
                """, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, de);
                pst.setInt(2, sur);
                pst.setTimestamp(3, convert(GetDate(0)));
                pst.setInt(4, montant);
                pst.executeUpdate();
                con.commit(); //je valide la transaction

                try ( ResultSet rid = pst.getGeneratedKeys()) {
                    rid.next();
                    int id = rid.getInt(1);
                    return id;
                }
//            }
            }
        } catch (Exception ex) {
            con.rollback(); //j'annule toute la transaction
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int createEnchere2(Connection con, int de, int sur,
            Timestamp quand, int montant) throws SQLException {
// M??thode pour cr??er une ench??re d??finie dans le sch??ma de base
        con.setAutoCommit(false);
        try {
            //d??but de transaction si false
            String sqlChercheEnchere
                    = "select max(montant)"
                    + " from enchere1"
                    + " where sur = ?";
            PreparedStatement chercheE = con.prepareStatement(sqlChercheEnchere);
            chercheE.setInt(1, sur);
            // statement = ordre sql
            ResultSet rs1 = chercheE.executeQuery();
            //resultSet = table
            rs1.next();
            int val = rs1.getInt(1);
            if (rs1.wasNull()) {
                String sqlChercheValBase
                        = "select prixbase"
                        + " from objet1"
                        + " where id = ?";
                PreparedStatement chercheVB = con.prepareStatement(sqlChercheValBase);
                chercheVB.setInt(1, sur);
                ResultSet rs2 = chercheVB.executeQuery();
                rs2.next();
                val = rs2.getInt("prixbase");
            }
            if (montant <= val) {
                return -1;
            } else {
//                String sqlDateFin
//                        = "select fin from objet1 "
//                        + "where id = ?";
//                PreparedStatement chercheDateFin = con.prepareStatement(sqlDateFin);
//                chercheDateFin.setInt(1, sur);
//                ResultSet rs3 = chercheDateFin.executeQuery();
//                Timestamp fin = rs3.getTimestamp("fin");
//                if (fin.getTime() <= System.currentTimeMillis()){
//                    return -2;
//                } else {

                chercheE.setInt(1, sur);
                PreparedStatement pst = con.prepareStatement(
                        """
                insert into enchere1 (de,sur,quand,montant) values (?,?,?,?)
                """, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, de);
                pst.setInt(2, sur);
                pst.setTimestamp(3, quand);
                pst.setInt(4, montant);
                pst.executeUpdate();
                con.commit(); //je valide la transaction

                try ( ResultSet rid = pst.getGeneratedKeys()) {
                    rid.next();
                    int id = rid.getInt(1);
                    return id;
                }
//            }
            }
        } catch (Exception ex) {
            con.rollback(); //j'annule toute la transaction
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static void afficheTousLesUtilisateurs(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select id,nom,prenom,email,"
                    + "pass,codepostal from utilisateur1")) {
                System.out.println("Liste des utilisateurs");
                System.out.println("----------------------");
                while (tlu.next()) {
                    int id = tlu.getInt(1);
                    String nom = tlu.getString(2);
                    String prenom = tlu.getString(3);
                    String email = tlu.getString(4);
//                    String pass = tlu.getString(5);
// Mieux vaut ne pas avoir acc??s au mot de passe des autres!
                    String codepostal = tlu.getString(6);
                    String mess = id + " : " + nom + ", " + prenom + " / "
                            + email + " / " + codepostal;

                    System.out.println(mess);
                }
            }
        }
        System.out.println("\n");
    }

    public static void afficheToutesLesCategories(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select * from categorie1")) {
                System.out.println("Liste des cat??gories");
                System.out.println("--------------------");
                while (tlu.next()) {
                    int id = tlu.getInt("id");
                    String nom = tlu.getString("nom");
                    String mess = id + " : " + nom;
                    System.out.println(mess);
                }
            }
        }
    }

    public static void afficheTousLesObjets(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select * from objet1")) {
                System.out.println("Liste des objets");
                System.out.println("----------------");
                while (tlu.next()) {
                    int id = tlu.getInt("id");
                    String titre = tlu.getString("titre");
                    String description = tlu.getString("description");
                    Timestamp debut = tlu.getTimestamp("debut");
                    Timestamp fin = tlu.getTimestamp("fin");
                    int prixbase = tlu.getInt("prixbase");
                    int categorie = tlu.getInt("categorie");
                    int proposepar = tlu.getInt("proposepar");
                    String mess = id + " : " + titre + "\n Description : " + description
                            + "\n D??but de l'ench??re : " + debut + "\n Fin de l'ench??re : "
                            + fin + "\n Prix initial : " + prixbase + "\n Cat??gorie : "
                            + categorie + "\n Propos?? par : " + proposepar;
                    System.out.println(mess);
                }
            }
        }
    }

    public static void afficheVentesEnCours(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select * from objet1 "
                    + "order by debut desc")) {
                while (tlu.next()) {
                    int id = tlu.getInt("id");
                    String titre = tlu.getString("titre");
                    String description = tlu.getString("description");
                    Timestamp debut = tlu.getTimestamp("debut");
                    Timestamp fin = tlu.getTimestamp("fin");
                    int prixbase = tlu.getInt("prixbase");
                    int categorie = tlu.getInt("categorie");
                    int proposepar = tlu.getInt("proposepar");
                    String mess = id + " : " + titre + "\n Description : " + description
                            + "\n D??but de l'ench??re : " + debut + "\n Fin de l'ench??re : "
                            + fin + "\n Prix initial : " + prixbase + "\n Cat??gorie : "
                            + categorie + "\n Propos?? par : " + proposepar;
                    if (fin.getTime() >= System.currentTimeMillis()) {
                        System.out.println(mess);
                    }
                }
            }
        }
    }

    public static List<Objet> listeObjets(Connection con) throws SQLException {
        List<Objet> res = new ArrayList<>();
        try ( PreparedStatement pst = con.prepareStatement(
                "select objet1.id as objid,titre,description,debut,fin,prixbase,categorie,proposepar"
                + "from objet1"
                + "join utilisateur1 on objet1.proposepar = utilisateur1.id"
                + "order by titre asc")) {
            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Objet(rs.getInt("objid"), rs.getString("titre"),
                            rs.getString("description"), rs.getTimestamp("debut"),
                            rs.getTimestamp("fin"), rs.getInt("prixbase"),
                            rs.getInt("categorie"), rs.getInt("proposepar")));
                }
            }
        }
        return res;
    }

    public static List<Enchere> listeEncheres(Connection con) throws SQLException {
        List<Enchere> res = new ArrayList<>();
        try ( PreparedStatement pst = con.prepareStatement(
                "select enchere1.id as enchereid,de,sur,quand,montant from enchere1"
                + "join utilisateur1 on enchere1.de = utilisateur1.id"
                + "join objet1 on enchere1.sur = objet1.id"
                + "order by quand desc")) {
            try ( ResultSet rs = pst.executeQuery()) {
                res.add(new Enchere(rs.getInt("enchereid"), rs.getInt("de"),
                        rs.getInt("sur"), rs.getTimestamp("quand"),
                        rs.getInt("montant")));
            }
        }
        return res;
    }

    public static boolean idUtilisateurExiste(Connection con, int id) throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement("select id from utilisateur1 where id = ?")) {
            pst.setInt(1, id);
            ResultSet res = pst.executeQuery();
            return res.next();
        }
    }

    public static boolean idCategorieExiste(Connection con, int id) throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement("select id from categorie1 where id = ?")) {
            pst.setInt(1, id);
            ResultSet res = pst.executeQuery();
            return res.next();
        }
    }

    public static boolean idObjetExiste(Connection con, int id) throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement("select id from objet1 where id = ?")) {
            pst.setInt(1, id);
            ResultSet res = pst.executeQuery();
            return res.next();
        }
    }

    public static int choisiUtilisateur(Connection con) throws SQLException {
        boolean ok = false;
        int id = -1;
        while (!ok) {
            System.out.println("------- Choix d'un utilisateur");
            afficheTousLesUtilisateurs(con);
            id = Console.entreeEntier("donnez l'identificateur de l'utilisateur : ");
            ok = idUtilisateurExiste(con, id);
            if (!ok) {
                System.out.println("id invalide");
            }
        }
        return id;
    }

    public static int choisiCategorie(Connection con) throws SQLException {
        boolean ok = false;
        int id = -1;
        while (!ok) {
            System.out.println("------- Choix d'une cat??gorie");
            afficheToutesLesCategories(con);
            id = Console.entreeEntier("Donnez l'identificateur de la cat??gorie");
            ok = idCategorieExiste(con, id);
            if (!ok) {
                System.out.println("id invalide");
            }
        }
        return id;
    }

    public static int choisiObjet(Connection con) throws SQLException {
        boolean ok = false;
        int id = -1;
        while (!ok) {
            System.out.println("------- Choix d'un objet");
            afficheTousLesObjets(con);
            id = Console.entreeEntier("Donnez l'identificateur de l'objet");
            ok = idObjetExiste(con, id);
            if (!ok) {
                System.out.println("id invalide");
            }
        }
        return id;
    }

//    select objet1.id,objet1.titre, (
//    select max(montant) from enchere1 where sur = objet1.id
//) ,
//(
//    select de from enchere1 where sur = objet1.id and montant = (
//        select max(montant) from enchere1 where sur = objet1.id
//    )
//),
//(
//    select utilisateur1.nom from enchere1
//        join utilisateur1 on utilisateur1.id = enchere1.de where sur = objet1.id and montant = (
//        select max(montant) from enchere1 where sur = objet1.id
//    )
//)
//from objet1 
//    public static void bilan(Connection con) throws SQLException {
//        int idU = Console.entreeEntier("Id de l'utilisateur concern?? : ");
//        try ( Statement st = con.createStatement()) {
//            try ( ResultSet tlu = st.executeQuery("select objet1.id,titre,"
//                    + "(select max(montant) from enchere1 where sur = objet1.id) as mMax,"
//                    + "(select de from enchere1 where sur = objet1.id and montant = ("
//                    + "select max(montant) from enchere1 where sur = objet1.id)) as idDe,"
//                    + "(select utilisateur1.nom from enchere1 "
//                    + "join utilisateur1 on utilisateur1.id = enchere1.de "
//                    + "where sur = objet1.id and montant = ("
//                    + "select max(montant) from enchere1 where sur = objet1.id)) as nomDe,"
//                    //                    + "(select utilisateur1.id as idUtil from enchere1 "
//                    //                    + "join utilisateur1 on enchere1.de = utilisateur1.id) as idU,"
//                    + "objet1.categorie,categorie1.nom as nomCat,prixbase,debut,fin from objet1 "
//                    + "join categorie1 on categorie1.id = objet1.categorie "
//                    //                    + "where exists (select de from enchere1 where enchere1.de = utilisateur1.id) "
//                    + "where exists(select enchere1.sur from enchere1 "
//                    + "where enchere1.sur = objet1.id and enchere1.de = 6)"
//            )) {
//                System.out.println("Votre bilan");
//                System.out.println("-----------");
//                while (tlu.next()) {
//                    int id = tlu.getInt("id");
//                    String titre = tlu.getString("titre");
//                    int prixbase = tlu.getInt("prixbase");
//                    Timestamp debut = tlu.getTimestamp("debut");
//                    Timestamp fin = tlu.getTimestamp("fin");
//                    int categorie = tlu.getInt("categorie");
//                    String nomCat = tlu.getString("nomCat");
//                    int mMax = tlu.getInt("mMax");
//                    int idDe = tlu.getInt("idDe");
//                    String nomDe = tlu.getString("nomDe");
//                    long finMillis = fin.getTime();
//                    long millis = System.currentTimeMillis();
//                    String close = null;
//                    if (finMillis < System.currentTimeMillis()) {
//                        close = "Ench??re close";
//                    }
//                    String mess = id + " : " + titre
//                            + "\n Cat??gorie : " + categorie + " / " + nomCat
//                            + "\n Prix initial : " + prixbase
//                            + "\n Montant actuel de l'ench??re : " + mMax
//                            + "\n Dernier ench??risseur : " + idDe + " / " + nomDe
//                            + "\n D??but de l'ench??re : " + debut
//                            + "\n Fin de l'ench??re : " + fin
//                            + "\n " + close;
//                    System.out.println(mess);
//                }
//            }
//        }
//    }
    public static void bilanEncheres(Connection con) throws SQLException {
        /*
    BilanEncheres affiche un message du type :
    Votre bilan : 
    -------------
    4 : Bureau
    Cat??gorie : 1 / Meuble
    Prix initial : 10000
    Montant actuel de l'ench??re : 12000
    Dernier ench??risseur : 7 / Venet
    D??but de l'ench??re : 2022-12-08 13:38:38.996
    Fin de l'ench??re : 2022-12-18 13:38:44.405
         */
        int idU = Console.entreeEntier("Id de l'utilisateur concern?? : ");
        try ( PreparedStatement pst = con.prepareStatement("select objet1.id,titre,"
                + "(select max(montant) from enchere1 where sur = objet1.id) as mMax,"
                + "(select de from enchere1 where sur = objet1.id and montant = ("
                + "select max(montant) from enchere1 where sur = objet1.id)) as idDe,"
                + "(select utilisateur1.nom from enchere1 "
                + "join utilisateur1 on utilisateur1.id = enchere1.de "
                + "where sur = objet1.id and montant = ("
                + "select max(montant) from enchere1 where sur = objet1.id)) as nomDe,"
                //                    + "(select utilisateur1.id as idUtil from enchere1 "
                //                    + "join utilisateur1 on enchere1.de = utilisateur1.id) as idU,"
                + "objet1.categorie,categorie1.nom as nomCat,prixbase,debut,fin from objet1 "
                + "join categorie1 on categorie1.id = objet1.categorie "
                //                    + "where exists (select de from enchere1 where enchere1.de = utilisateur1.id) "
                + "where exists(select enchere1.sur from enchere1 "
                + "where enchere1.sur = objet1.id and enchere1.de = ?)")) {
            pst.setInt(1, idU);
            try ( ResultSet tlu = pst.executeQuery()) {
                System.out.println("Votre bilan");
                System.out.println("-----------");
                while (tlu.next()) {
                    int id = tlu.getInt("id");
                    String titre = tlu.getString("titre");
                    int prixbase = tlu.getInt("prixbase");
                    Timestamp debut = tlu.getTimestamp("debut");
                    Timestamp fin = tlu.getTimestamp("fin");
                    int categorie = tlu.getInt("categorie");
                    String nomCat = tlu.getString("nomCat");
                    int mMax = tlu.getInt("mMax");
                    int idDe = tlu.getInt("idDe");
                    String nomDe = tlu.getString("nomDe");
                    long finMillis = fin.getTime();
                    String close = null;
                    if (finMillis < System.currentTimeMillis()) {
                        close = "ENCHERE CLOSE\n";
                    } else {
                        close = " ";
                    }
                    String mess = id + " : " + titre
                            + "\n Cat??gorie : " + categorie + " / " + nomCat
                            + "\n Prix initial : " + prixbase
                            + "\n Montant actuel de l'ench??re : " + mMax
                            + "\n Dernier ench??risseur : " + idDe + " / " + nomDe
                            + "\n D??but de l'ench??re : " + debut
                            + "\n Fin de l'ench??re : " + fin
                            + "\n " + close;
                    System.out.println(mess);
                }
            }
        }
    }

    public static void bilanVentes(Connection con) throws SQLException {
        int idU = Console.entreeEntier("Id de l'utilisateur concern?? : ");
        try ( PreparedStatement pst = con.prepareStatement("select objet1.id,titre,"
                + "(select max(montant) from enchere1 where sur = objet1.id) as mMax,"
                + "(select de from enchere1 where sur = objet1.id and montant = ("
                + "select max(montant) from enchere1 where sur = objet1.id)) as idDe,"
                + "(select utilisateur1.nom from enchere1 "
                + "join utilisateur1 on utilisateur1.id = enchere1.de "
                + "where sur = objet1.id and montant = ("
                + "select max(montant) from enchere1 where sur = objet1.id)) as nomDe,"
                + "objet1.categorie,categorie1.nom as nomCat,prixbase,debut,fin from objet1 "
                + "join categorie1 on categorie1.id = objet1.categorie "
                + "where objet1.proposepar = ?")) {
            pst.setInt(1, idU);
            try ( ResultSet tlu = pst.executeQuery()) {
                System.out.println("Votre bilan");
                System.out.println("-----------");
                while (tlu.next()) {
                    int id = tlu.getInt("id");
                    String titre = tlu.getString("titre");
                    int prixbase = tlu.getInt("prixbase");
                    Timestamp debut = tlu.getTimestamp("debut");
                    Timestamp fin = tlu.getTimestamp("fin");
                    int categorie = tlu.getInt("categorie");
                    String nomCat = tlu.getString("nomCat");
                    int mMax = tlu.getInt("mMax");
                    int idDe = tlu.getInt("idDe");
                    String nomDe = tlu.getString("nomDe");
                    long finMillis = fin.getTime();
                    String close = null;
                    if (finMillis < System.currentTimeMillis()) {
                        close = "ENCHERE CLOSE\n";
                    } else {
                        close = " ";
                    }
                    String mess = id + " : " + titre
                            + "\n Cat??gorie : " + categorie + " / " + nomCat
                            + "\n Prix initial : " + prixbase
                            + "\n Montant actuel de l'ench??re : " + mMax
                            + "\n Dernier ench??risseur : " + idDe + " / " + nomDe
                            + "\n D??but de l'ench??re : " + debut
                            + "\n Fin de l'ench??re : " + fin
                            + "\n " + close;
                    System.out.println(mess);
                }
            }
        }
    }

    public static void afficheProfil(Connection con) throws SQLException {
        int idu = Console.entreeEntier("Id de l'utilisateur : ");
        try ( PreparedStatement pst = con.prepareStatement("select id,nom,"
                + "prenom,email,codepostal from utilisateur1 "
                + "where utilisateur1.id = ?")) {
            pst.setInt(1, idu);
            try ( ResultSet rs = pst.executeQuery()) {
                System.out.println("Votre profil");
                System.out.println("------------");
                rs.next();
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String codepostal = rs.getString("codepostal");
                String mess = "id : " + id + "\nNom : " + nom + "\nPr??nom : "
                        + prenom + "\nE-mail : " + email + "\nCode postal : "
                        + codepostal;
                System.out.println(mess);
            }
        }
    }

    // TODO : trouver comment faire une recherche par cat??gorie via un choix de 
    // noms de cat??gories pr??d??fini
    public static void recherche(Connection con) throws SQLException {
        /* Permet de trouver un objet avec un extrait de son titre ou de sa description
et affiche en premier les r??sultats retourn??s gr??ce au titre, puis ceux 
retourn??s gr??ce ?? leur description */
        String search = Console.entreeString("Veuillez entrer votre recherche.");
        String finalSearch = "%" + search + "%";
        try ( PreparedStatement pst = con.prepareStatement("select *,"
                + "(select max(montant) from enchere1 where sur = objet1.id) as mMax,"
                + "categorie1.nom as nomCat,"
                + "(select utilisateur1.nom from enchere1 "
                + "join utilisateur1 on utilisateur1.id = enchere1.de "
                + "where sur = objet1.id and montant = ("
                + "select max(montant) from enchere1 where sur = objet1.id)) as nomDe "
                + "from objet1 "
                + "join categorie1 on categorie1.id = objet1.categorie "
                + "where titre like ? ")) {
            pst.setString(1, finalSearch);
            try ( ResultSet tlu = pst.executeQuery()) {
//                System.out.println("R??sultats obtenus : \n"
//                        + "--------------------");
                while (tlu.next()) {
                    int id = tlu.getInt("id");
                    String titre = tlu.getString("titre");
                    String description = tlu.getString("description");
                    Timestamp debut = tlu.getTimestamp("debut");
                    Timestamp fin = tlu.getTimestamp("fin");
                    int prixbase = tlu.getInt("prixbase");
                    int mMax = tlu.getInt("mMax");
                    if (mMax == 0) {
                        mMax = prixbase;
                    }
                    int proposepar = tlu.getInt("proposepar");
                    int categorie = tlu.getInt("categorie");
                    String nomCat = tlu.getString("nomCat");
                    String nomDe = tlu.getString("nomDe");
                    String mess = id + " : " + titre + " \n" + description
                            + "\nEnch??re : du " + debut + " au " + fin + "\n"
                            + "Prix de base : " + prixbase + " / Prix actuel : "
                            + mMax + "\n"
                            + "Cat??gorie : " + categorie + " / " + nomCat
                            + "\nVendeur : " + proposepar + " / " + nomDe
                            + "\n--------------------";

                    System.out.println(mess);
                }
            }
        }
        try ( PreparedStatement pst = con.prepareStatement("select *,"
                + "(select max(montant) from enchere1 where sur = objet1.id) as mMax,"
                + "categorie1.nom as nomCat,"
                + "(select utilisateur1.nom from enchere1 "
                + "join utilisateur1 on utilisateur1.id = enchere1.de "
                + "where sur = objet1.id and montant = ("
                + "select max(montant) from enchere1 where sur = objet1.id)) as nomDe "
                + "from objet1 "
                + "join categorie1 on categorie1.id = objet1.categorie "
                + "where description like ?")) {
            pst.setString(1, finalSearch);
            try ( ResultSet tlu = pst.executeQuery()) {
//                System.out.println("R??sultats obtenus : \n"
//                        + "--------------------");
                while (tlu.next()) {
                    int id = tlu.getInt("id");
                    String titre = tlu.getString("titre");
                    String description = tlu.getString("description");
                    Timestamp debut = tlu.getTimestamp("debut");
                    Timestamp fin = tlu.getTimestamp("fin");
                    int prixbase = tlu.getInt("prixbase");
                    int mMax = tlu.getInt("mMax");
                    if (mMax == 0) {
                        mMax = prixbase;
                    }
                    int proposepar = tlu.getInt("proposepar");
                    int categorie = tlu.getInt("categorie");
                    String nomCat = tlu.getString("nomCat");
                    String nomDe = tlu.getString("nomDe");
                    String mess = id + " : " + titre + " \n" + description
                            + "\nEnch??re : du " + debut + " au " + fin + "\n"
                            + "Prix de base : " + prixbase + " / Prix actuel : "
                            + mMax + "\n"
                            + "Cat??gorie : " + categorie + " / " + nomCat
                            + "\nVendeur : " + proposepar + " / " + nomDe
                            + "\n--------------------";

                    System.out.println(mess);
                }
            }
        }
    }

    public static Optional<Utilisateur> login(Connection con)
            throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement(
                "select utilisateur1.id as uid,nom,prenom,codepostal from utilisateur1 "
                + "where utilisateur1.email = ? and pass = ?")) {
            String email = Console.entreeString("Identifiant");
            String pass = Console.entreeString("Mot de passe");
            pst.setString(1, email);
            pst.setString(2, pass);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                return Optional.of(new Utilisateur(res.getInt("uid"),
                        res.getString("nom"), res.getString("prenom"),
                        email, pass, res.getString("codepostal")));
            } else {
                System.out.println("Mauvais identifiant ou mot de passe");
                return Optional.empty();
            }
        }
    }

    public static Optional<Utilisateur> login2(Connection con, String email, String pass)
            throws SQLException {
// Utilis?? pour permettre ?? un nouvel utilisateur de se connecter directement 
// apr??s son inscription
        try ( PreparedStatement pst = con.prepareStatement(
                "select * from utilisateur1 "
                + "where utilisateur1.email = ? and pass = ?")) {
            pst.setString(1, email);
            pst.setString(2, pass);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                int id = res.getInt("id");
                System.out.println("id : " + id);
                return Optional.of(new Utilisateur(id,
                        res.getString("nom"), res.getString("prenom"),
                        email, pass, res.getString("codepostal")));
            } else {
                System.out.println("Mauvais identifiant ou mot de passe");
                return Optional.empty();
            }
        }
    }

    public static void inscription(Connection con)
            throws SQLException, EmailExisteDejaException {
        String nom = Console.entreeString("Nom : ");
        String prenom = Console.entreeString("Pr??nom : ");
        String email = Console.entreeString("email : ");
        String pass = Console.entreeString("Mot de passe : ");
        String codepostal = Console.entreeString("Code postal : ");
        try {
            createUtilisateur(con, nom, prenom, email, pass, codepostal);
        login2(con, email, pass);
        System.out.println("Nouvel utilisateur inscrit");
        } catch (SQLException | EmailExisteDejaException ex) {
            throw ex;
        }
        
    }

    public static void toutRecreer(Connection con) throws SQLException {
        try {
            deleteSchema(con);
        } catch (SQLException ex) {
            System.out.println("Pas de suppression n??cessaire");
        }
        creeSchema(con);
        List<Integer> idu = new ArrayList<>();
        try {
            idu.add(createUtilisateur(con, "Cabane", "La",
                    "lacabane@email.com", "lctcqtv", "67000"));
            idu.add(createUtilisateur(con, "Leblanc", "Juste",
                    "justelb@email.com", "ilapasdeprenom", "75000"));
            idu.add(createUtilisateur(con, "Feur", "Quoi",
                    "mdr@email.com", "12345", "01240"));
            idu.add(createUtilisateur(con, "Talent", "Billy",
                    "billy@email.com", "leaves", "45000"));
        } catch (EmailExisteDejaException ex) {
            throw new Error(ex);
        }
        createCategorie2(con, "V??tement");
        createCategorie2(con, "Meuble");
        createCategorie2(con, "Animaux");
        createCategorie2(con, "Sport");
        createCategorie2(con, "Jeux/Jouets");
        createCategorie2(con, "Automobile");
        createCategorie2(con, "Bricolage");

        Timestamp ts = new Timestamp(0, 0, 0, 0, 0, 0, 0);

        createObjet(con, "Pull", "En laine",
                convert(GetDate(-10)), convert(GetDate(-2)),
                2000, 2, 1);
        createObjet(con, "Bureau", "En bois avec quatre pieds",
                convert(GetDate(-30)), convert(GetDate(5)),
                10000, 1, 1);
        createObjet(con, "Cage ?? oiseaux", "En m??tal avec un perchoir",
                convert(GetDate(-5)), convert(GetDate(15)),
                5000, 3, 3);
        createObjet(con, "Un truc", "sans ench??re pour faire des tests",
                convert(GetDate(-30)), convert(GetDate(30)),
                100, 1, 2);
        createObjet(con, "Pneus", "4 pneus neige peu utilis??s",
                convert(GetDate(-30)), convert(GetDate(-2)),
                20000, 6, 4);
        createObjet(con, "test ts", "test",
                new Timestamp(2022, 12, 18, 12, 0, 0, 0),
                new Timestamp(2023, 01, 18, 12, 0, 0, 0),
                100, 5, 4);

        createEnchere2(con, 2, 2, convert(GetDate(-15)), 11000);
        createEnchere2(con, 4, 2, convert(GetDate(-5)), 12000);
        createEnchere2(con, 2, 2, convert(GetDate(-2)), 12500);
        createEnchere2(con, 3, 1, convert(GetDate(-5)), 2200);
        createEnchere2(con, 2, 3, convert(GetDate(-4)), 6000);
        createEnchere2(con, 1, 3, convert(GetDate(-3)), 6500);
        createEnchere2(con, 4, 3, convert(GetDate(-2)), 7000);
        createEnchere2(con, 2, 3, convert(GetDate(-1)), 7100);
        createEnchere2(con, 4, 5, convert(GetDate(-10)), 20500);
    }

    public static void menuPrincipal(Connection con) {
        int rep = -1;
//        Session curSec = new Session();
        while (rep != 0) {
            System.out.println("""
                               1) Liste des utilisateurs 
                               2) Proposer un objet 
                               3) Proposer une ench??re 
                               4) Liste des objets 
                               5) Bilan des ench??res
                               6) Bilan des ventes
                               0) Quitter""");
            rep = Console.entreeEntier("Votre choix : ");
            try {
                if (rep == 1) {
                    afficheTousLesUtilisateurs(con);
                } else if (rep == 2) {
                    createObjet2(con);
                } else if (rep == 3) {
                    demandeEnchere(con);
                } else if (rep == 4) {
                    afficheVentesEnCours(con);
                } else if (rep == 5) {
//                    Utilisateur curUser = curSec.getCurrentUser().orElseThrow();
                    bilanEncheres(con);
                } else if (rep == 6) {
                    bilanVentes(con);
                }
            } catch (SQLException ex) {
                System.out.println("Probl??me : ...");
            }
        }
    }

    public static void menuLogin() {
        Connection con = null;
        boolean ok = false;
        int rep = -1;
        while (rep != 0 && !ok) {
            System.out.println("""
                               Accueil du site
                               ===============
                               1) Connexion
                               2) Inscription
                               0) Quitter
                               """);
            rep = Console.entreeEntier("Que voulez-vous faire?");

            try {
                if (rep == 1) {
                    con = defautConnect();
                    String email = Console.entreeString("Email : ");
                    String pass = Console.entreeString("pass : ");
                    login2(con, email, pass);
                    ok = true;
                }
                else if (rep == 2) {
                    con = defautConnect();
                    inscription(con);
                    ok = true;
                }
                if (ok) {
                    System.out.println("Connect??");
                } else {
                    System.out.println("Probl??me");
                }
            } catch (SQLException | ClassNotFoundException | 
                    EmailExisteDejaException ex) {
                System.out.println("Problem : " + ex.getLocalizedMessage());
            }

        }
        if (ok) {
            menuPrincipal(con);
        }
    }

    public static void menuTest() {

    }

    public static void main(String[] args) throws NomExisteDejaException {
        System.out.println("Hello World!");
        try {
            Connection con = defautConnect();
            //System.out.println("connection OK");
//            creeSchema(con);
//            System.out.println("sch??ma cr????");
//            deleteSchema(con);
//            System.out.println("Sch??ma supprim??");
//            createUtilisateur(con, "Toto", "truc", "toto@email.fr", "pass1", "67084");
//            createUtilisateur2(con);
//            System.out.println("Utilisateur cr????");
//            createObjet(con, "Pull", "En laine", null, null, 2000, 2, 7);
//            System.out.println("Objet cr????");
//            createObjet2(con);
//            afficheTousLesUtilisateurs(con);
//            createCategorie(con);
//            System.out.println("Cat??gorie cr????e");
//            demandeEnchere(con);
//            bilanEncheres(con);
//            bilanVentes(con);
//            recherche(con);
//            String email = Console.entreeString("Email : ");
//            String pass = Console.entreeString("pass : ");
//            login2(con, email, pass);
//            System.out.println("Utilisateur connect??");
//            demandeUtilisateur(con);
//            menuPrincipal(con);
//            toutRecreer(con);
//            afficheProfil(con);
//            afficheVentesEnCours(con);
        menuLogin();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjetS5Encheres.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(ProjetS5Encheres.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package fr.insa.legoff_venet.projet.projets5encheres;

import fr.insa.legoff_venet.projet.utils.Console;
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
            // si j'arrive jusqu'ici, c'est que tout s'est bien passÃ©
            // je confirme (commit) la transaction
            con.commit();
            // je retourne dans le mode par dÃ©faut de gestion des transaction :
            // chaque ordre au SGBD sera considÃ©rÃ© comme une transaction indÃ©pendante
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            // quelque chose s'est mal passÃ©
            // j'annule la transaction
            con.rollback();
            // puis je renvoie l'exeption pour qu'elle puisse Ã©ventuellement
            // Ãªtre gÃ©rÃ©e (message Ã  l'utilisateur...)
            throw ex;
        } finally {
            // je reviens Ã  la gestion par dÃ©faut : une transaction pour
            // chaque ordre SQL
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

    public static int createUtilisateur(Connection con, String nom, String prenom,
            String email, String pass, String codepostal)
            throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement chercheNom = con.prepareStatement(
                "select id from utilisateur1 where nom = ?")) {
            chercheNom.setString(1, nom);
            ResultSet testNom = chercheNom.executeQuery();
//            if (testNom.next()) {
//                throw new NomExisteDejaException();
//            }
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

    public static int createUtilisateur2(Connection con)
            throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement chercheNom = con.prepareStatement(
                "select id from utilisateur1 where nom = ?")) {
//            chercheNom.setString(1, Lire.S());
//            ResultSet testNom = chercheNom.executeQuery();
//            if (testNom.next()) {
//                throw new NomExisteDejaException();
//            }
            try ( PreparedStatement pst = con.prepareStatement(
                    """
                    insert into utilisateur1 (nom,prenom,email,pass,codepostal) values (?,?,?,?,?) 
                    """, PreparedStatement.RETURN_GENERATED_KEYS)) {
                System.out.println("Nom : ");
                pst.setString(1, Lire.S());
                System.out.println("Prénom : ");
                pst.setString(2, Lire.S());
                System.out.println("e-mail : ");
                pst.setString(3, Lire.S());
                System.out.println("Mot de passe : ");
                pst.setString(4, Lire.S());
                System.out.println("Code postal : ");
                pst.setString(5, Lire.S());
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

    public static Date GetDate(int nbjr) {
        long milliseconds = System.currentTimeMillis();
        Date date = new Date(milliseconds + nbjr * 86400000);
        // Il y a 86400000 ms dans une journée
        return date;
    }

    public static java.sql.Timestamp convert(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    public static int createCategorie(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try ( PreparedStatement pst = con.prepareStatement(
                """
                insert into categorie1 (nom) values (?)
                """, Statement.RETURN_GENERATED_KEYS)) {
            System.out.println("Nom de la catégorie : ");
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
//TODO : régler le pb des Timestamp
//      permettre de créer un objet "spontanément" 

    public static int createObjet(Connection con, String titre, String description,
            Timestamp debut, Timestamp fin, int prixbase, int categorie, int proposepar)
            throws SQLException {
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
            System.out.println("Combien de jours durera l'enchère ?");
            pst.setTimestamp(4, convert(GetDate(Lire.i())));
            System.out.println("Prix de base de l'objet (en centimes) : ");
            pst.setInt(5, Lire.i());
            System.out.println("Catégorie de l'objet : ");
            pst.setInt(6, Lire.i());
//TODO : faire en sorte que le nom d'utilisateur soit défini 
//      en fonction de l'utilisateur connecté
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
        System.out.println("Id de l'enchérisseur : ");
        int idE = Lire.i();
        System.out.println("Id de l'objet : ");
        int idO = Lire.i();
        System.out.println("Montant proposé : ");
        int montant = Lire.i();
        int res = createEnchere(con, idE, idO, montant);
        if (res == -1) {
            System.out.println("montant trop faible");
        } else {
            System.out.println("Votre enchère a bien été enregistrée. (id :" + res + ")");
        }

    }
//TODO : finaliser méthode, paramétrage de "de" et "sur" en fonction de 
    //   l'utilisateur connecté et de l'objet choisi

    public static int createEnchere(Connection con, int de, int sur,
            int montant) throws SQLException {
        con.setAutoCommit(false);
        try {
            //début de transaction si false
            String sqlChercheEnchere
                    = "select max(montant)"
                    + " from enchere1"
                    + " where sur = ?";
            PreparedStatement chercheE = con.prepareStatement(sqlChercheEnchere);
            chercheE.setInt(1, sur);
            // statement = ordre sql
            ResultSet rs1 = chercheE.executeQuery(); //resultSet = table
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
            try ( ResultSet tlu = st.executeQuery("select id,nom,prenom,email,pass,codepostal from utilisateur1")) {
                System.out.println("Liste des utilisateurs");
                System.out.println("----------------------");
                while (tlu.next()) {
                    int id = tlu.getInt(1);
                    String nom = tlu.getString(2);
                    String prenom = tlu.getString(3);
                    String email = tlu.getString(4);
                    String pass = tlu.getString(5);
                    String codepostal = tlu.getString(6);
                    String mess = id + " : " + nom + ", " + prenom + " / "
                            + email + " / " + pass + " / " + codepostal;

                    System.out.println(mess);
                }
            }
        }
    }

    public static void afficheToutesLesCategories(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select id,nom")) {
                System.out.println("Liste des catégories");
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
            try ( ResultSet tlu = st.executeQuery("select id,titre,description,"
                    + "debut,fin,prixbase,categorie,proposepar")) {
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
                            + "\n Début de l'enchère : " + debut + "\n Fin de l'enchère : "
                            + fin + "\n Prix initial : " + prixbase + "\n Catégorie : "
                            + categorie + "\n Proposé par : " + proposepar;
                    System.out.println(mess);
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
            System.out.println("------- Choix d'une catégorie");
            afficheToutesLesCategories(con);
            id = Console.entreeEntier("Donnez l'identificateur de la catégorie");
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
    public static void bilan(Connection con, int idU) throws SQLException {
//Affiche un message du type : 
//4 : Bureau
// Catégorie : 1 / Meuble
// Prix initial : 10000
// Montant actuel de l'enchère : 12000
// Dernier enchérisseur : 7 / Venet
// Début de l'enchère : 2022-12-08 13:38:38.996
// Fin de l'enchère : 2022-12-18 13:38:44.405
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select objet1.id,titre,"
                    + "(select max(montant) from enchere1 where sur = objet1.id) as mMax,"
                    + "(select de from enchere1 where sur = objet1.id and montant = ("
                    + "select max(montant) from enchere1 where sur = objet1.id)) as idDe,"
                    + "(select utilisateur1.nom from enchere1 "
                    + "join utilisateur1 on utilisateur1.id = enchere1.de "
                    + "where sur = objet1.id and montant = ("
                    + "select max(montant) from enchere1 where sur = objet1.id)) as nomDe,"
                    + "objet1.categorie,categorie1.nom as nomCat,prixbase,debut,fin from objet1 "
                    + "join categorie1 on categorie1.id = objet1.categorie")) {
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
                    String mess = id + " : " + titre
                            + "\n Catégorie : " + categorie + " / " + nomCat
                            + "\n Prix initial : " + prixbase
                            + "\n Montant actuel de l'enchère : " + mMax
                            + "\n Dernier enchérisseur : " + idDe + " / " + nomDe
                            + "\n Début de l'enchère : " + debut
                            + "\n Fin de l'enchère : " + fin + "\n";
                    System.out.println(mess);
                }
            }
        }
    }

//    public static Optional<Utilisateur> login(Connection con, String nom, String pass)
//            throws SQLException {
//        try ( PreparedStatement pst = con.prepareStatement(
//                "select utilisateur1.id as uid from utilisateur1"
//                + "where utilisateur1.nom = ? and pass = ?")) {
//            pst.setString(1, nom);
//            pst.setString(2, pass);
//            ResultSet res = pst.executeQuery();
//            if (res.next()) {
//                return Optional.of(new Utilisateur(res.getInt("uid"), nom, pass));
//            } else {
//                return Optional.empty();
//            }
//        }
//    }
    
    public static void main(String[] args) throws NomExisteDejaException {
        System.out.println("Hello World!");
        try {
            Connection con = defautConnect();
            //System.out.println("connection OK");
//            creeSchema(con);
//            System.out.println("schéma créé");
//            deleteSchema(con);
//            System.out.println("Schéma supprimé");
//            createUtilisateur(con, "Toto", "truc", "toto@email.fr", "pass1", "67084");
//            createUtilisateur2(con);
//            System.out.println("Utilisateur créé");
//            createObjet(con, "Pull", "En laine", null, null, 2000, 2, 7);
//            System.out.println("Objet créé");
//            createObjet2(con);
//            afficheTousLesUtilisateurs(con);
//            createCategorie(con);
//            System.out.println("Catégorie créée");
//            demandeEnchere(con);
            bilan(con, 7);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjetS5Encheres.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(ProjetS5Encheres.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}

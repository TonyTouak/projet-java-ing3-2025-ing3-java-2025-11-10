package dao;

import java.sql.*;


public class DaoFactory {
    /**
     * Attributs privés pour la connexion JDBC
     */
    private static String url;
    private static String username;
    private static String password;

    public DaoFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Méthode qui retourne une instance de DaoFactory
     * @param database : nom de la base de données
     * @param username : utilisateur de la base de données
     * @param password : mot de passe de la base de données
     * @return : objet de la classe DaoFactory
     */
    public static DaoFactory getInstance(String database, String username, String password) {
        try {
            // Chargement du driver JDBC pour MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Erreur de connexion à la base de données");
        }

        url = "jdbc:mysql://localhost:3306/" + database;

        DaoFactory instance = new DaoFactory(url, username, password);

        return instance;
    }

    /**
     * Méthode qui retourne une connexion à la base de données
     * @return : une connexion à la base de données
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        // Retourner la connexion du driver à la base de données
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Récupération du DAO pour la gestion des administrateurs
     * @return : objet de la classe administrateurDaoImpl
     */
    public administrateurDao getadministrateurDao() {
        // Retourne un objet de administrateurDaoImpl qui implémente administrateurDao
        return new administrateurDaoImpl(this);
    }

    /**
     * Récupération du DAO pour la gestion des articles
     * @return : objet de la classe articleDaoImpl
     */
    public articleDao getarticleDao() {
        // Retourne un objet de articleDaoImpl qui implémente articleDao
        return new articleDaoImpl(this);
    }

    /**
     * Récupération du DAO pour la gestion des clients
     * @return : objet de la classe ClientDAOImpl
     */
    public clientDao getClientDao() {
        // Retourne un objet de ClientDAOImpl qui implémente ClientDAO
        return new clientDaoImpl(this);
    }

    /**
     * Récupération du DAO pour la gestion des commandes
     * @return : objet de la classe CommanderDAOImpl
     */
    public commandeDao getcommandeDao() {
        // Retourne un objet de CommanderDAOImpl qui implémente CommanderDAO
        return new commandeDaoImpl(this);
    }

    /**
     * Récupération du DAO pour la gestion des utilisateurs
     * @return : objet de la classe utilisateurDaoImpl
     */
    public utilisateurDao getutilisateurDao() {
        // Retourne un objet de utilisateurDaoImpl qui implémente utilisateurDao
        return new utilisateurDaoImpl(this);
    }

    /**
     * Fermer la connexion à la base de données
     */
    public void disconnect() {
        Connection connexion = null;

        try {
            connexion = this.getConnection();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la déconnexion à la base de données");
        }
    }
}

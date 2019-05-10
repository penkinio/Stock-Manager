/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.enumeration.RoleType;
import com.stockmanager.data.User;
import com.stockmanager.data.enumeration.EStatusUtilisateur;
import com.stockmanager.service.IServiceUser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 *
 * classe implementant l'interface IServiceUser et permettant d'effectuer les
 * traitements sur utilisateurs
 */
public class ServiceUserImpl implements IServiceUser {

    public ServiceUserImpl() {
    }

    /**
     * methode permettant supprimer les utilisateurs et retoune le nombre de
     * lignes supprimés
     *
     * @param code
     * @return le nombre de lignes detectés
     */
    @Override
    public int deleteUserByCode(int code) {
        int line = 0;
        PreparedStatement pst = null;
        try {
            pst = Connexion.getCon().prepareStatement("update utilisateur set status = ? where code = ?");
            pst.setInt(1, 1);
            pst.setInt(2, code);
            pst.executeUpdate();
            line = pst.executeUpdate();
            if (line > 0) {
                return line;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return line;
    }

    /**
     * methode permettant de renvoyer la liste de tous les utilisateurs prennant
     * en parametre une requete sql
     *
     * @param requete : requete sql
     * @return la liste de tous les utilisateurs
     * @throws SQLException
     */
    @Override
    public List<User> findAllUserSimple(String requete) throws SQLException {
        List<User> listUser = new ArrayList<User>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        User u;
        try {
            pst = Connexion.getCon().prepareStatement(requete);
            pst.setInt(1, 0);
            rs = pst.executeQuery();
            while (rs.next()) {
                u = new User();
                u.setCode(rs.getInt("code"));
                u.setPassWord(rs.getString("passwd"));
                u.setRole(RoleType.values()[rs.getInt("role")]);
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setUserName(rs.getString("username"));
                u.setStatus(EStatusUtilisateur.values()[rs.getInt("status")]);
                listUser.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.close();
            pst.close();
        }
        return listUser;
    }

    /**
     * methode permettant de reinitialiser le mot de passe
     *
     * @param id : identifiant utilisateur
     * @return 1 pour succes et 2 echec
     */
    @Override
    public int reinitialiserPassWord(int id) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            pst = Connexion.getCon().prepareStatement("update utilisateur set passwd = ? where code = ?");
            pst.setString(1, "admin");
            pst.setInt(2, id);
            pst.executeUpdate();
            result = 1;
        } catch (SQLException ex) {
            result = 2;
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * methode permettant d'enregistrer un utilisateur pris en parametre
     *
     * @param u parametre representant un utilisateur
     * @return 1 pour succes et 2 echec
     */
    @Override
    public int saveUtilisateur(User u) {
        PreparedStatement pst = null;
        int res = 1;
        try {
            pst = Connexion.getCon().prepareStatement("insert into utilisateur (nom,prenom,username,passwd,role,status) values(?,?,?,?,?,?)");
            pst.setString(1, u.getNom());
            pst.setString(2, u.getPrenom());
            pst.setString(3, u.getUserName().toLowerCase());
            pst.setString(4, u.getPassWord());
            pst.setInt(5, u.getRole().ordinal());
            pst.setInt(6, 0);
            pst.executeUpdate();
            res = 1;
        } catch (SQLException ex) {
            res = 2;
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return res;
    }

    /**
     * methode permettant d'authentifier un utilisateur et prennant en parametre
     * son pseudo et son mot de passe retourne 1 si tous se passe bien 2 si
     * l'utilisateur n'a pas bien entré les parametres de connexion ou
     * l'utilisateur en question n'existe pas dans le systeme et enfin 3 si il
     * y'a impossibilité de verification de coordonnées
     *
     * @param login : pseudo de l'utilisateur
     * @param password : mot de passe de l'utilisateur
     * @return 1 si tous se passe bien,2 pour mauvaise parametre et 3 pour
     * impossibilité de verification des coordonnées
     */
    @Override
    public int authentification(String login, String password) {
        PreparedStatement pst = null;
        PreparedStatement pp = null;
        ResultSet rs = null;
        ResultSet dd = null;
        try {
            pst = Connexion.getCon().prepareStatement("select * from utilisateur where username = ?");
            pst.setString(1, login.toLowerCase());
            rs = pst.executeQuery();
            if (rs.next()) {
                if (password.equals(rs.getString("passwd"))) {
                    UserInfo.setUsername(login);
                    UserInfo.setRole(RoleType.values()[rs.getInt("role")]);
                    UserInfo.setUserCode(rs.getInt("code"));
                    UserInfo.setNom(rs.getString("nom"));
                    UserInfo.setPrenom(rs.getString("prenom"));
                    UserInfo.setLogged(true);
                    if (UserInfo.getRole().equals(RoleType.Employee)) {
                        pp = Connexion.getCon().prepareStatement("select code from utilisateur where username = ?");
                        pp.setString(1, login.toLowerCase());
                        dd = pp.executeQuery();
                    }
                    return 1;
                } else {
                    return 2;
                }
            } else {
                return 2;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            return 3;
        } finally {
            try {
                if (dd != null) {
                    dd.close();
                }
                if (pp != null) {
                    pp.close();
                }
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int changePassword(String login, String newPassword, String confirmPassword) {

        PreparedStatement pst = null;
        int result = 1;

        if (!newPassword.equals(confirmPassword)) {
            return 2;
        }

        try {
            pst = Connexion.getCon().prepareStatement("update utilisateur set passwd = ? where username = ?");
            pst.setString(1, newPassword);
            pst.setString(2, login);
            pst.executeUpdate();
            result = 1;
        } catch (SQLException ex) {
            result = 2;
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public User getUserByUserName(String username) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        User u = null;
        try {
            pst = Connexion.getCon().prepareStatement("select * from utilisateur where username = ?");
            pst.setString(1, username);
            rs = pst.executeQuery();
            if (rs.next()) {
                u = new User();
                u.setCode(rs.getInt("code"));
                u.setPassWord(rs.getString("passwd"));
                u.setRole(RoleType.values()[rs.getInt("role")]);
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setUserName(rs.getString("username"));
                u.setStatus(EStatusUtilisateur.values()[rs.getInt("status")]);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return u;
        }
    }

    public boolean getIsActif(String username) {
        User user = getUserByUserName(username);

        return user.getStatus().equals(EStatusUtilisateur.Actif);
    }

    public Iterable<User> findAllUser() {
        List<User> listUser = new ArrayList<User>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        User u;
        try {
            pst = Connexion.getCon().prepareStatement("select * from utilisateur");
            rs = pst.executeQuery();
            while (rs.next()) {
                u = new User();
                u.setCode(rs.getInt("code"));
                u.setPassWord(rs.getString("passwd"));
                u.setRole(RoleType.values()[rs.getInt("role")]);
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setUserName(rs.getString("username"));
                listUser.add(u);
                u.setStatus(EStatusUtilisateur.values()[rs.getInt("status")]);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listUser;
    }

    public int modifierUser(int code, EStatusUtilisateur status) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            pst = Connexion.getCon().prepareStatement("update utilisateur set status = ? where code = ?");
            pst.setInt(1, status.ordinal());
            pst.setInt(2, code);
            pst.executeUpdate();
            result = 1;
        } catch (SQLException ex) {
            result = 2;
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}

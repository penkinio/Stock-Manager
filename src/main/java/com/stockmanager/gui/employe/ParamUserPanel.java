package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Journal;
import com.stockmanager.data.User;
import com.stockmanager.data.enumeration.EStatusUtilisateur;
import com.stockmanager.gui.admin.UtilisateurPanel;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceUser;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.stockmanager.serviceimpl.ServiceUserImpl;
import com.stockmanager.serviceimpl.UserInfo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * classe permettant d'afficher le panel d'enregistrement d'un nouveau client
 */
public class ParamUserPanel extends JPanel {

    private JComboBox<EStatusUtilisateur> statusCombo;
    private JComboBox<String> userCombo;
    private MainMenuPanel parent;
    IServiceUser model = new ServiceUserImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public ParamUserPanel() {
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public ParamUserPanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(10, 10));
        userCombo = new JComboBox<String>();
        statusCombo = new JComboBox<EStatusUtilisateur>();
        JButton btnEnregistrer = new JButton("Enrégistrer");
        
        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        btnEnregistrer.setIcon(new ImageIcon(iconSave.getPath()));
        
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("PARAMETRAGE DES UTILISATEURS");
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        userCombo = initComboUser(userCombo);
        statusCombo.addItem(null);
        statusCombo.addItem(EStatusUtilisateur.Actif);
        statusCombo.addItem(EStatusUtilisateur.Suspendu);
        builder.append("Utilisateur", userCombo);
        builder.append("Status", statusCombo);
        builder.append(btnEnregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        btnEnregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    EStatusUtilisateur status = (EStatusUtilisateur) statusCombo.getSelectedItem();
                    String userName = (String) userCombo.getSelectedItem();
                    User user = model.getUserByUserName(userName);
                    verification(status.toString(), userName);
                    int resul = model.modifierUser(user.getCode(), status);
                    if (resul == 1) {
                        Journal journal = new Journal(" Parametrage User ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Parametrage Prix d'un User par l'utilisateur " + UserInfo.getUsername() + " de nom :" + userName);
                        modelJournal.saveJournal(journal);
                        JOptionPane.showMessageDialog(null, "Le User " + userName + " a été parametre");
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de parametre ce User");
                    }
                    parent.setContenu(new UtilisateurPanel(parent));
                } catch (SQLException ex) {
                    Logger.getLogger(ParamUserPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void verification(String categorie, String libelle) {
                if ((categorie == null) || ("".equals(categorie))) {
                    JOptionPane.showMessageDialog(null, "La Categorie est obligatoire");
                }

                if ((libelle == null) || ("".equals(libelle))) {
                    JOptionPane.showMessageDialog(null, "Le User est obligatoire");
                }
            }
        });
    }

    private JComboBox<String> initComboUser(JComboBox<String> userCombo) {
        userCombo.addItem(null);

        for (User user : model.findAllUser()) {
            userCombo.addItem(user.getUserName());
        }
        return userCombo;
    }

}

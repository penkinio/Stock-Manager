package com.stockmanager.gui.admin;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Journal;
import com.stockmanager.data.enumeration.RoleType;
import com.stockmanager.data.User;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * classe permettant d'enregistrer un nouveau utilisateur
 */
public class NouveauUtilisateurPanel extends JPanel {

    private JTextField loginText;
    private JTextField nomText;
    private JTextField prenomText;
    private JPasswordField passwdText1;
    private JPasswordField passwdText2;
    private JComboBox<RoleType> role;
    private MainMenuPanel parent;
    IServiceUser model = new ServiceUserImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public NouveauUtilisateurPanel() {
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public NouveauUtilisateurPanel(MainMenuPanel parentFrame) {
        setLayout(new BorderLayout(20, 20));
        this.parent = parentFrame;
        JLabel lbl = new JLabel("AJOUT D'UN NOUVEAU UTILISATEUR DANS MA STOCK MANAGER");
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginText = new JTextField();
        nomText = new JTextField();
        prenomText = new JTextField();
        passwdText1 = new JPasswordField();
        passwdText2 = new JPasswordField();
        role = new JComboBox<RoleType>();
        JButton enregistrer = new JButton("Enregistrer");

        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        enregistrer.setIcon(new ImageIcon(iconSave.getPath()));

        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        haut.add(lbl);
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Login", loginText);
        builder.append("Mot de Passe", passwdText1);
        builder.append("Retapez mot de passe", passwdText2);
        builder.append("Nom", nomText);
        builder.append("Prenom", prenomText);
        builder.append("Role", role);
        role.addItem(null);
        role.addItem(RoleType.Administrateur);
        role.addItem(RoleType.Employee);
        role.addItem(RoleType.Controleur);
        builder.append(enregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        enregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String login = loginText.getText();
                String pwd = new String(passwdText1.getPassword());
                String pwd2 = new String(passwdText2.getPassword());
                String nom = nomText.getText();
                String prenom = prenomText.getText();
                RoleType ro = (RoleType) role.getSelectedItem();
                if ((login == null) || ("".equals(login))) {
                    JOptionPane.showMessageDialog(null, "Le login est obligatoire");
                    return;
                }
                if ("".equals(pwd)) {
                    JOptionPane.showMessageDialog(null, "Le password est obligatoire");
                    return;
                }
                if (!(pwd.equals(pwd2))) {
                    JOptionPane.showMessageDialog(null, "Les mots de passe ne sont pas identiques");
                    return;
                }
                if (ro == null) {
                    JOptionPane.showMessageDialog(null, "Le role est obligatoire");
                    return;
                }
                //creation d'un object utilisateur
                User u = new User(nom, prenom, login, pwd, ro);

                //insersion d'un object utilisateur dans la base de donnée
                int res = model.saveUtilisateur(u);
                if (res == 1) {
                    Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'un client par l'utilisateur " + UserInfo.getUsername() + " de Nom :" + nom + " " + prenom);
                    modelJournal.saveJournal(journal);
                    JOptionPane.showMessageDialog(null, "Nouveau utilisateur creer");
                } else {
                    JOptionPane.showMessageDialog(null, "Impossible de créer le compte");
                }
                try {
                    parent.setContenu(new UtilisateurPanel(parent));
                } catch (SQLException ex) {
                    Logger.getLogger(NouveauUtilisateurPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}

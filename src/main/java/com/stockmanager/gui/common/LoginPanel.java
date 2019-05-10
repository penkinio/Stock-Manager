package com.stockmanager.gui.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Journal;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceUser;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.stockmanager.serviceimpl.ServiceUserImpl;
import com.stockmanager.serviceimpl.UserInfo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 *
 * classe affichant le panel d'authentification
 */
public class LoginPanel extends JPanel {

    private JTextField loginText;
    private JPasswordField passwdText;
    private JButton btnLogin;
    private JButton btnAnnuler;
    private JPanel buttonPanel;
    IServiceUser model = new ServiceUserImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconLogin;
    File iconCancel;

    public LoginPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(80, 350, 80, 300));
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));

        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 120dlu:", ""));
        btnLogin = new JButton("Connexion");
        btnAnnuler = new JButton("Annuler");
        
        iconLogin = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Delivery.png");
        iconCancel = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Cancel.png");
        btnLogin.setIcon(new ImageIcon(iconLogin.getPath()));
        btnAnnuler.setIcon(new ImageIcon(iconCancel.getPath()));
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnAnnuler);
        builder.append("Nom Utilisateur", loginText = new JTextField());
        builder.append("Mot de passe", passwdText = new JPasswordField());
        builder.append("", buttonPanel);
        add(BorderLayout.CENTER, builder.getPanel());
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String username = loginText.getText();
                String passwd = new String(passwdText.getPassword());
                if ((username == null) || ("".equals(username))) {
                    JOptionPane.showMessageDialog(null, "Le login est obligatoire");
                    passwdText.setText("");
                    return;
                }
                if ("".equals(passwd)) {
                    JOptionPane.showMessageDialog(null, "Le mot de passe est obligatoire");
                    passwdText.setText("");
                    return;
                }
                int statutAth = model.authentification(username, passwd);
                if (statutAth == 1) {
                    if (model.getIsActif(username)) {
                        Journal journal = new Journal("Connection", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), "Connection de l'utilisateur " + username);
                        modelJournal.saveJournal(journal);
                        success();
                    } else {
                        JOptionPane.showMessageDialog(null, "Utilisateur Suspendu !!!!!!!");
                    }
                } else if (statutAth == 2) {
                    JOptionPane.showMessageDialog(null, "Login ou mot de passe incorrect");
                    passwdText.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Impossible de vérifier vos coordonnées");
                    passwdText.setText("");
                }
            }
        });

        btnAnnuler.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                loginText.setText("");
                passwdText.setText("");
            }
        });
    }

    public void success() {
    }
}

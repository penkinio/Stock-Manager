package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Client;
import com.stockmanager.data.Journal;
import com.stockmanager.service.IServiceClient;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.serviceimpl.ServiceClientImpl;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * @author Ngnawen Samuel<sngawen@yahoo.fr>
 * classe permettant d'afficher le panel d'enregistrement d'un nouveau client
 */
public class NouveauClientPanel extends JPanel {

    private JTextField nomText;
    private JTextField contactText;
    private JTextField villeText;
    private JTextField localisationText;
    private JTextField emailText;
    private JTextField paysText;
    private JTextField boitePostalText;
    private JTextField siteWebText;
    private int code = -1;
    private MainMenuPanel parent;
    IServiceClient model = new ServiceClientImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public NouveauClientPanel() {
    }

    /**
     * Constructeur avec 2 arguments
     *
     * @param parentFrame
     * @param code
     * @throws SQLException
     */
    public NouveauClientPanel(MainMenuPanel parentFrame, int code) throws SQLException {
        this(parentFrame);
        this.code = code;
        Client c = model.findClientByCode(code);
        nomText.setText(c.getNom());
        contactText.setText(c.getContact());
        villeText.setText(c.getVille());
        localisationText.setText(c.getLocalisation());
        emailText.setText(c.getEmail());
        paysText.setText(c.getPays());
        boitePostalText.setText(c.getBoitePostal());
        siteWebText.setText(c.getSiteWeb());
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public NouveauClientPanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(10, 10));
        nomText = new JTextField();
        contactText = new JTextField();
        villeText = new JTextField();
        localisationText = new JTextField();
        emailText = new JTextField();
        paysText = new JTextField();
        boitePostalText = new JTextField();
        siteWebText = new JTextField();
        JButton btnEnregistrer = new JButton("Enrégistrer");
        
        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        btnEnregistrer.setIcon(new ImageIcon(iconSave.getPath()));
        
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("AJOUT D'UN NOUVEAU CLIENT DANS MA BANQUE POPULAIRE");
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Nom", nomText);
        builder.append("Contact", contactText);
        builder.append("Ville", villeText);
        builder.append("Localisation", localisationText);
        builder.append("Email", emailText);
        builder.append("Pays", paysText);
        builder.append("Boite Postale", boitePostalText);
        builder.append("Site Web", siteWebText);
        builder.append(btnEnregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        btnEnregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (code <= 0) {
                    String nom = nomText.getText();
                    String contact = contactText.getText();
                    String ville = villeText.getText();
                    String localisation = localisationText.getText();
                    String email = emailText.getText();
                    String pays = paysText.getText();
                    String boitePostale = boitePostalText.getText();
                    String siteWeb = siteWebText.getText();

                    verification(nom, contact, ville, localisation);

                    Client c = new Client(nom, contact, ville, localisation, email, pays, boitePostale, siteWeb);

                    if (model.isExist(nom)) {
                        JOptionPane.showMessageDialog(null, "Le Client " + nom + " Existe déjà !!!");
                    } else {
                        int resul = model.saveClient(c);
                        if (resul == 1) {
                            Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'un client par l'utilisateur " + UserInfo.getUsername() + " de Nom :" + nom);
                            modelJournal.saveJournal(journal);
                            JOptionPane.showMessageDialog(null, "Le Client " + nom + " a été créé");
                        } else {
                            JOptionPane.showMessageDialog(null, "Impossible de creer ce Client");
                        }
                    }

                    try {
                        parent.setContenu(new ClientPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String nom = nomText.getText();
                    String contact = contactText.getText();
                    String ville = villeText.getText();
                    String localisation = localisationText.getText();
                    String email = emailText.getText();
                    String pays = paysText.getText();
                    String boitePostale = boitePostalText.getText();
                    String siteWeb = siteWebText.getText();

                    verification(nom, contact, ville, localisation);

                    int result = model.modifierClient(nom, contact, ville, localisation, email, pays, boitePostale, siteWeb, code);

                    if (result == 1) {
                        Journal journal = new Journal(" Modification ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'un client par l'utilisateur " + UserInfo.getUsername() + " de Nom :" + nom);
                        modelJournal.saveJournal(journal);
                        JOptionPane.showMessageDialog(null, "Operation reussit");
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de mettre a jour ce client");
                    }
                    try {
                        parent.setContenu(new ClientPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            private void verification(String nom, String contact, String ville, String localisation) {
                if ((nom == null) || ("".equals(nom))) {
                    JOptionPane.showMessageDialog(null, "Le nom est obligatoire");
                    return;
                }
                if ((contact == null) || ("".equals(contact))) {
                    JOptionPane.showMessageDialog(null, "Le Contact est obligatoire");
                    return;
                }
                if ((ville == null) || ("".equals(ville))) {
                    JOptionPane.showMessageDialog(null, "La ville est obligatoire");
                    return;
                }
                if ((localisation == null) || ("".equals(localisation))) {
                    JOptionPane.showMessageDialog(null, "La Localisation est obligatoire");
                }
            }
        });
    }
}

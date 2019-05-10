package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Fournisseur;
import com.stockmanager.data.Journal;
import com.stockmanager.service.IServiceFournisseur;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.serviceimpl.ServiceFournisseurImpl;
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
public class NouveauFournisseurPanel extends JPanel {

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
    IServiceFournisseur model = new ServiceFournisseurImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public NouveauFournisseurPanel() {
    }

    /**
     * Constructeur avec 2 arguments
     *
     * @param parentFrame
     * @param id
     * @throws SQLException
     */
    public NouveauFournisseurPanel(MainMenuPanel parentFrame, int code) throws SQLException {
        this(parentFrame);
        this.code = code;
        Fournisseur c = model.findFournisseurByCode(code);
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
    public NouveauFournisseurPanel(MainMenuPanel parentFrame) {
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
        JLabel lbl = new JLabel("AJOUT D'UN NOUVEAU FOURNISSEUR DANS STOCK-MANAGER");
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

                String nom = nomText.getText();
                String contact = contactText.getText();
                String ville = villeText.getText();
                String localisation = localisationText.getText();
                String email = emailText.getText();
                String pays = paysText.getText();
                String boitePostale = boitePostalText.getText();
                String siteWeb = siteWebText.getText();

                verification(nom, contact, ville, localisation);

                if (code <= 0) {
                    Fournisseur c = new Fournisseur(nom, contact, ville, localisation, email, pays, boitePostale, siteWeb);

                    if (model.isExist(nom)) {
                        JOptionPane.showMessageDialog(null, "Le Fournisseur " + nom + " Existe déjà !!!");
                    } else {
                        int resul = model.saveFournisseur(c);
                        if (resul == 1) {
                            Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'un Fournisseur par l'utilisateur " + UserInfo.getUsername() + " de nom :" + nom);
                            modelJournal.saveJournal(journal);
                            JOptionPane.showMessageDialog(null, "Le Fournisseur " + nom + " a été créé");
                        } else {
                            JOptionPane.showMessageDialog(null, "Impossible de creer ce Fournisseur");
                        }
                    }

                    try {
                        parent.setContenu(new FournisseurPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauFournisseurPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    int result = model.modifierFournisseur(nom, contact, ville, localisation, email, pays, boitePostale, siteWeb, code);

                    if (result == 1) {
                        Journal journal = new Journal(" Modification ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Modification d'un Fournisseur par l'utilisateur " + UserInfo.getUsername() + " de nom :" + nom);
                        modelJournal.saveJournal(journal);
                        JOptionPane.showMessageDialog(null, "Operation reussit");
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de mettre a jour Ce Fournisseur");
                    }
                    try {
                        parent.setContenu(new FournisseurPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauFournisseurPanel.class.getName()).log(Level.SEVERE, null, ex);
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

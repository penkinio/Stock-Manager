package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Journal;
import com.stockmanager.data.Produit;
import com.stockmanager.data.enumeration.EGenreProduit;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.stockmanager.serviceimpl.ServiceProduitImpl;
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
import javax.swing.JTextField;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * classe permettant d'afficher le panel d'enregistrement d'un nouveau client
 */
public class ParamProduitPanel extends JPanel {

    private JTextField prixVenteText;
    private JComboBox<String> produitCombo;
    private JComboBox<EGenreProduit> genreCombo;
    private MainMenuPanel parent;
    IServiceProduit model = new ServiceProduitImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public ParamProduitPanel() {
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public ParamProduitPanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(10, 10));
        genreCombo = new JComboBox<EGenreProduit>();
        produitCombo = new JComboBox<String>();
        prixVenteText = new JTextField();
        JButton btnEnregistrer = new JButton("Enrégistrer");

        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        btnEnregistrer.setIcon(new ImageIcon(iconSave.getPath()));

        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("PARAMETRAGE DU PRIX DES PRODUITS");
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        produitCombo = initComboTypeProduit(produitCombo);
        genreCombo.addItem(null);
        genreCombo.addItem(EGenreProduit.Commercialisable);
        genreCombo.addItem(EGenreProduit.NonCommercialisable);
        builder.append("Produit", produitCombo);
        builder.append("Genre", genreCombo);
        builder.append("Prix de Vente", prixVenteText);
        builder.append(btnEnregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        btnEnregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    Double prixVente = Double.parseDouble(prixVenteText.getText());
                    String nom = (String) produitCombo.getSelectedItem();
                    Produit produit = model.getProduitByName(nom);
                    EGenreProduit genre = (EGenreProduit) genreCombo.getSelectedItem();
                    verification(prixVente, nom, genre);

                    int resul = model.updateParamProduit(produit.getCode(), prixVente, genre);
                    if (resul == 1) {
                        Journal journal = new Journal(" Parametrage Produit ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Parametrage Prix d'un Produit par l'utilisateur " + UserInfo.getUsername() + " de nom :" + nom);
                        modelJournal.saveJournal(journal);
                        JOptionPane.showMessageDialog(null, "Le Produit " + nom + " a été parametre");
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de parametre ce Produit");
                    }

                    try {
                        parent.setContenu(new ProduitPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ParamProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void verification(Double prix, String libelle, EGenreProduit genre) {
                if (prix == null) {
                    JOptionPane.showMessageDialog(null, "Le prix est obligatoire");
                    return;
                }

                if ((libelle == null) || ("".equals(libelle))) {
                    JOptionPane.showMessageDialog(null, "Le Produit est obligatoire");
                }

                if ((genre == null)) {
                    JOptionPane.showMessageDialog(null, "Le Genre est obligatoire");
                }
            }
        });
    }

    private JComboBox<String> initComboTypeProduit(JComboBox<String> produitCombo) {
        produitCombo.addItem(null);

        try {
            for (Produit produit : model.findAllProduit()) {
                produitCombo.addItem(produit.getNom());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParamProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produitCombo;
    }
}

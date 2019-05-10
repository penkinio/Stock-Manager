package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.connection.Connexion;
import com.stockmanager.data.Fournisseur;
import com.stockmanager.data.Produit;
import com.stockmanager.data.Achat;
import com.stockmanager.data.Journal;
import com.stockmanager.service.IServiceAchat;
import com.stockmanager.service.IServiceFournisseur;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.serviceimpl.ServiceFournisseurImpl;
import com.stockmanager.serviceimpl.ServiceProduitImpl;
import com.stockmanager.serviceimpl.ServiceAchatImpl;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.stockmanager.serviceimpl.UserInfo;
import java.io.File;
import javax.swing.ImageIcon;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * classe permettant d'enregistrer un nouveau utilisateur
 */
public class NouveauAchatPanel extends JPanel {

    private JTextField prixAchatText;
    private JTextField quantiteText;
    private JComboBox<String> produitCombo;
    private JComboBox<String> fournisseurCombo;
    private MainMenuPanel parent;
    private int codeProduit;
    private int codeFournisseur;
    private String reference;
    IServiceAchat model = new ServiceAchatImpl();
    IServiceFournisseur modelFournisseur = new ServiceFournisseurImpl();
    IServiceProduit modelProduit = new ServiceProduitImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    Achat achat = new Achat();
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public NouveauAchatPanel() {
    }

    /**
     * Constructeur avec 4 arguments
     *
     * @param parentFrame
     * @param reference
     * @throws SQLException
     */
    public NouveauAchatPanel(MainMenuPanel parentFrame, String reference) throws SQLException {
        this(parentFrame);
        this.reference = reference;
        achat = model.getAchat(reference);
        fournisseurCombo.setSelectedItem(modelFournisseur.findFournisseurByCode(achat.getCodeFournisseur()).getNom());
        produitCombo.setSelectedItem(modelProduit.findProduitByCode(achat.getCodeProduit()).getNom());
        quantiteText.setText("" + achat.getQuantite());
        prixAchatText.setText("" + achat.getPrixUnitaireAchat());
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public NouveauAchatPanel(MainMenuPanel parentFrame) {
        setLayout(new BorderLayout(20, 20));
        this.parent = parentFrame;
        JLabel lbl = new JLabel("ARRIVAGE DANS STOCK-MANAGER");
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        produitCombo = new JComboBox<String>();
        fournisseurCombo = new JComboBox<String>();
        prixAchatText = new JTextField();
        quantiteText = new JTextField();
        JButton enregistrer = new JButton("Enregistrer");
        
        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        enregistrer.setIcon(new ImageIcon(iconSave.getPath()));
        
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        haut.add(lbl);
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));

        produitCombo = initComboProduit(produitCombo);
        fournisseurCombo = initComboFournisseur(fournisseurCombo);
        builder.append("Produit", produitCombo);
        builder.append("Fournisseur", fournisseurCombo);
        builder.append("Prix Unitaire", prixAchatText);
        builder.append("Quantite", quantiteText);

        builder.append(enregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        enregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    double prix = Double.parseDouble(prixAchatText.getText());
                    int qte = Integer.parseInt(quantiteText.getText());
                    String nomProduit = (String) produitCombo.getSelectedItem();
                    String nomFournisseur = (String) fournisseurCombo.getSelectedItem();
                    Produit produit = modelProduit.getProduitByName(nomProduit);
                    Fournisseur fournisseur = modelFournisseur.getFournisseurByName(nomFournisseur);
                    Date date = new Date();

                    if ((prix == 0.0) || ("".equals(prixAchatText.getText()))) {
                        JOptionPane.showMessageDialog(null, "Le Prix est obligatoire");
                        return;
                    }
                    if ((qte == 0) || ("".equals(quantiteText.getText()))) {
                        JOptionPane.showMessageDialog(null, "La quantite est obligatoire");
                        return;
                    }
                    if (produit == null) {
                        JOptionPane.showMessageDialog(null, "Le Produit est obligatoire");
                        return;
                    }
                    if (fournisseur == null) {
                        JOptionPane.showMessageDialog(null, "Le Fournisseur est obligatoire");
                        return;
                    }

                    if (achat.getReference() == null) {
                        String ref = generateRef(date);
                        System.out.println(ref);

                        //creation d'un object utilisateur
                        achat = new Achat(fournisseur.getCode(), produit.getCode(), ref, prix, qte, date);

                        //insersion d'un object utilisateur dans la base de donnée
                        int res = model.saveAchat(achat);
                        if (res == 1) {
                            modelProduit.addQuantiteProduit(produit.getCode(), qte);
                            Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'un achat par l'utilisateur " + UserInfo.getUsername() + " du produit " + produit.getNom() + " de quantité " + achat.getQuantite() + " au montant unitaire " + achat.getPrixUnitaireAchat());
                            modelJournal.saveJournal(journal);
                            JOptionPane.showMessageDialog(null, "Nouveau achat creer");
                        } else {
                            JOptionPane.showMessageDialog(null, "Impossible de créer une Achat ");
                        }
                    } else {
                        modelProduit.removeQuantiteProduit(achat.getCodeProduit(), achat.getQuantite());
                        Connexion.getCon().commit();
                        achat = new Achat(fournisseur.getCode(), produit.getCode(), achat.getReference(), prix, qte, date);

                        //insersion d'un object utilisateur dans la base de donnée
                        int res = model.updateAchat(achat);
                        if (res == 1) {
                            modelProduit.addQuantiteProduit(produit.getCode(), qte);
                            Journal journal = new Journal(" Modification ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'un achat par l'utilisateur " + UserInfo.getUsername() + " du produit " + produit.getNom() + " de quantité " + achat.getQuantite() + " au montant unitaire " + achat.getPrixUnitaireAchat());
                            modelJournal.saveJournal(journal);
                            JOptionPane.showMessageDialog(null, "Operation reussit");
                        } else {
                            JOptionPane.showMessageDialog(null, "Impossible de modifier l'achat ");
                        }
                    }

                    try {
                        parent.setContenu(new AchatPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauAchatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(NouveauAchatPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            private String generateRef(Date date) {
                return "AH" +(new SimpleDateFormat("yyMMddHHmmss").format(date));
            }
        });
    }

    private JComboBox<String> initComboProduit(JComboBox<String> produitCombo) {

        produitCombo.addItem(null);

        try {
            for (String produit : modelProduit.findAllProduitName()) {
                produitCombo.addItem(produit);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NouveauAchatPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produitCombo;
    }

    private JComboBox<String> initComboFournisseur(JComboBox<String> fournisseurCombo) {
        fournisseurCombo.addItem(null);

        try {
            for (String fournisseur : modelFournisseur.findAllFournisseurName()) {
                fournisseurCombo.addItem(fournisseur);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NouveauAchatPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fournisseurCombo;
    }
}

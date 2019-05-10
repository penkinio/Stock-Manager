package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Client;
import com.stockmanager.data.Facture;
import com.stockmanager.data.Journal;
import com.stockmanager.data.Produit;
import com.stockmanager.data.Vente;
import com.stockmanager.service.IServiceClient;
import com.stockmanager.service.IServiceFacture;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.service.IServiceVente;
import com.stockmanager.serviceimpl.ServiceClientImpl;
import com.stockmanager.serviceimpl.ServiceFactureImpl;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.stockmanager.serviceimpl.ServiceProduitImpl;
import com.stockmanager.serviceimpl.ServiceVenteImpl;
import com.stockmanager.serviceimpl.UserInfo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
 * classe permettant d'enregistrer un nouveau utilisateur
 */
public class NouveauVentePanel extends JPanel {

    private JTextField quantiteText;
    private JComboBox<String> produitCombo;
    private JComboBox<String> clientCombo;
    private MainMenuPanel parent;
    private int codeProduit;
    private int codeClient;
    private String reference;
    IServiceVente model = new ServiceVenteImpl();
    IServiceClient modelClient = new ServiceClientImpl();
    IServiceProduit modelProduit = new ServiceProduitImpl();
    IServiceFacture factureModel = new ServiceFactureImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconSave;
    Vente vente;

    /**
     * constructeur sans arguments
     */
    public NouveauVentePanel() {
    }

    /**
     *
     * @param parent
     * @param string
     */
    NouveauVentePanel(MainMenuPanel parentFrame, String reference) {
        this(parentFrame);
        try {
            this.reference = reference;
            vente = model.getVente(reference);
            clientCombo.setSelectedItem(modelClient.findClientByCode(vente.getCodeClient()).getNom());
            produitCombo.setSelectedItem(modelProduit.findProduitByCode(vente.getCodeProduit()).getNom());
            quantiteText.setText("" + vente.getQuantite());
        } catch (SQLException ex) {
            Logger.getLogger(NouveauVentePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public NouveauVentePanel(MainMenuPanel parentFrame) {
        setLayout(new BorderLayout(20, 20));
        this.parent = parentFrame;
        JLabel lbl = new JLabel("NOUVELLE VENTE DANS STOCK-MANAGER");
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        produitCombo = new JComboBox<String>();
        clientCombo = new JComboBox<String>();
        quantiteText = new JTextField();
        JButton enregistrer = new JButton("Enregistrer");

        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        enregistrer.setIcon(new ImageIcon(iconSave.getPath()));

        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        haut.add(lbl);
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));

        produitCombo = initComboProduit(produitCombo);
        clientCombo = initComboClient(clientCombo);
        builder.append("Produit", produitCombo);
        builder.append("Client", clientCombo);
        builder.append("Quantite", quantiteText);

        builder.append(enregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        enregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    int qte = Integer.parseInt(quantiteText.getText());
                    String nomProduit = (String) produitCombo.getSelectedItem();
                    String nomClient = (String) clientCombo.getSelectedItem();
                    Client client = modelClient.getClientByName(nomClient);
                    Produit produit = modelProduit.getProduitByName(nomProduit);

                    Date date = new Date();

                    if ((qte == 0) || ("".equals(quantiteText.getText()))) {
                        JOptionPane.showMessageDialog(null, "La quantite est obligatoire");
                        return;
                    }
                    if (produit == null) {
                        JOptionPane.showMessageDialog(null, "Le Produit est obligatoire");
                        return;
                    }
                    if (client == null) {
                        JOptionPane.showMessageDialog(null, "Le Client est obligatoire");
                        return;
                    }

                    String ref = generateRef(date);
                    vente = new Vente(client.getCode(), produit.getCode(), ref, produit.getPrixUnitaire(), qte, date);

                    //creation d'un object utilisateur
                    String NumFacture = generateNum(date);
                    vente.setFacture(NumFacture);
                    Facture facture = new Facture();
                    facture.setNumeroFacture(NumFacture);
                    facture.setNomClient((String) clientCombo.getSelectedItem());
                    facture.setDate(date);
                    facture.getVentes().add(vente);

                    if (vente.getReference() == null) {
                        if (qte < modelProduit.getQuantiteEnSock(produit.getCode())) {
                            int res = model.saveVente(vente);
                            if (res == 1) {
                                Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'une Vente par l'utilisateur " + UserInfo.getUsername() + " Du produit :" + produit.getNom() + " Au montant unitaire egale a " + vente.getPrixUnitaireVente());
                                modelJournal.saveJournal(journal);
                                modelProduit.removeQuantiteProduit(produit.getCode(), qte);
                                JOptionPane.showMessageDialog(null, "Nouvelle Vente creer");
                                factureModel.saveFacture(facture);
                            } else {
                                JOptionPane.showMessageDialog(null, "Impossible de créer le compte");
                            }
                            try {
                                parent.setContenu(new VentePanel(parent));
                            } catch (SQLException ex) {
                                Logger.getLogger(NouveauVentePanel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Quantite Insuffisant ");
                        }
                    } else if (qte < modelProduit.getQuantiteEnSock(produit.getCode())) {
                        int res = model.updateVente(vente);
                        if (res == 1) {
                            Journal journal = new Journal(" Modification ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'une Vente par l'utilisateur " + UserInfo.getUsername() + " Du produit :" + produit.getNom() + " Au montant unitaire egale a " + vente.getPrixUnitaireVente());
                            modelJournal.saveJournal(journal);
                            modelProduit.removeQuantiteProduit(produit.getCode(), qte);
                            JOptionPane.showMessageDialog(null, "Vente Modifié");
                            factureModel.updateFacture(facture);
                        } else {
                            JOptionPane.showMessageDialog(null, "Impossible de créer le compte");
                        }
                        try {
                            parent.setContenu(new VentePanel(parent));
                        } catch (SQLException ex) {
                            Logger.getLogger(NouveauVentePanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Quantite Insuffisant ");
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(NouveauVentePanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            private String generateRef(Date date) {
                return "VT" + (new SimpleDateFormat("yyMMddHHmmss").format(date));
            }

            private String generateNum(Date date) {
                return "FA" + (new SimpleDateFormat("yyMMddHHmmss").format(date));
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
            Logger.getLogger(NouveauVentePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produitCombo;
    }

    private JComboBox<String> initComboClient(JComboBox<String> clientCombo) {
        clientCombo.addItem(null);

        try {
            for (String client : modelClient.findAllClientName()) {
                clientCombo.addItem(client);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NouveauVentePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clientCombo;
    }
}

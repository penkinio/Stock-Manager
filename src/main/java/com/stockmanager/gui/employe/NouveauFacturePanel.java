/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.gui.employe;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Client;
import com.stockmanager.data.Facture;
import com.stockmanager.data.Journal;
import com.stockmanager.data.Produit;
import com.stockmanager.data.Vente;
import com.stockmanager.data.enumeration.EtatFacture;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.service.IServiceCategorie;
import com.stockmanager.service.IServiceClient;
import com.stockmanager.service.IServiceFacture;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.service.IServiceVente;
import com.stockmanager.serviceimpl.ServiceCategorieImpl;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Maurice
 */
class NouveauFacturePanel extends JPanel {

    private JComboBox<String> clientCombo;
    private JComboBox<String> produitCombo;
    private JComboBox<EtatFacture> etat;
    private javax.swing.JTextField txtQuantite;
    private JTextField montantFactureText;
    private JCheckBox tvaCheckBox;
    private int code = -1;
    private Vente vente;
    private JTable venteTable;
    double montant = 0;
    Facture facture;
    private DefaultTableModel tableModel;
    private MainMenuPanel parent;
    DecimalFormat df = new DecimalFormat("#,##0");
    File iconSave;
    File iconCreate;
    File iconDelete;

    IServiceClient modelClient = new ServiceClientImpl();
    IServiceProduit modelProduit = new ServiceProduitImpl();
    IServiceVente venteModels = new ServiceVenteImpl();
    IServiceFacture factureModel = new ServiceFactureImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();

    public NouveauFacturePanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(20, 20));
        JPanel contenu = new JPanel();
        JLabel lbl = new JLabel("NOUVELLE FACTURE DANS STOCK-MANAGER");
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        clientCombo = new JComboBox<String>();
        JButton addVente = new JButton("Ajouter Facture");
        JButton removeVente = new JButton("Supprimer Facture");

        iconCreate = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Create.png");
        iconDelete = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Delete.png");

        addVente.setIcon(new ImageIcon(iconCreate.getPath()));
        removeVente.setIcon(new ImageIcon(iconDelete.getPath()));

        panelButton.add(addVente);
        panelButton.add(removeVente);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        haut.add(lbl);
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));

        etat = new JComboBox<EtatFacture>();
        clientCombo = initComboClient(clientCombo);
        montantFactureText = new JTextField();
        montantFactureText.setEditable(false);
        tvaCheckBox = new JCheckBox();
        builder.append("Client", clientCombo);
        builder.append("", panelButton);
        builder.append("Montant Facture", montantFactureText);
        builder.append("Etat De Facture", etat);
        builder.append("Appliquer la TVA", tvaCheckBox);
        etat.addItem(null);
        etat.addItem(EtatFacture.NonRegler);
        etat.addItem(EtatFacture.EnCourDeReglement);
        etat.addItem(EtatFacture.Regler);
        add(BorderLayout.CENTER, builder.getPanel());

        DefaultFormBuilder venteBuilder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        produitCombo = new JComboBox<String>();
        produitCombo = initComboProduit(produitCombo);
        txtQuantite = new javax.swing.JTextField();
        venteBuilder.append("Produit", produitCombo);
        venteBuilder.append("Quantite", txtQuantite);
        venteBuilder.append("", panelButton);
        add(BorderLayout.SOUTH, venteBuilder.getPanel());

        addVente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Produit produit;
                Client client;

                String nomProduit = (String) produitCombo.getSelectedItem();
                String nom = (String) clientCombo.getSelectedItem();

                try {

                    produit = modelProduit.getProduitByName(nomProduit);
                    client = modelClient.getClientByName(nom);
                    int qte = Integer.parseInt(txtQuantite.getText());

                    Date date = new Date();

                    String ref = generateRef(date);
                    System.out.println(ref);

                    vente = new Vente(client.getCode(), produit.getCode(), ref, produit.getPrixUnitaire(), qte, date);
                } catch (SQLException ex) {
                    Logger.getLogger(NouveauFacturePanel.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    tableModel.addRow(new Object[]{
                        modelProduit.findProduitByCode(vente.getCodeProduit()).getNom(),
                        nom,
                        vente.getReference(),
                        df.format(vente.getPrixUnitaireVente()),
                        vente.getQuantite(),
                        df.format(vente.getPrixUnitaireVente() * vente.getQuantite()),
                        vente.getDate()
                    });
                    montant += vente.getPrixUnitaireVente() * vente.getQuantite();
                    montantFactureText.setText(" " + df.format(montant) + " ");
                    viderChamp();
                } catch (SQLException ex) {
                    Logger.getLogger(NouveauFacturePanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        removeVente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = venteTable.getSelectedRow();

                if (selected >= 0) {
                    tableModel.removeRow(selected);
                    montant -= (Double) tableModel.getValueAt(selected, 5);
                    montantFactureText.setText(" " + df.format(montant) + " ");
                } else {
                    JOptionPane.showMessageDialog(null, "Aucune Vente selectionnée");
                }
            }
        });

        tableModel = new DefaultTableModel(new Object[]{"Produit", "Client", "Reference", "Prix Unitaire", "Quantite", "Prix Total", "Date"}, 0);
        venteTable = new JTable(tableModel);
        contenu.add(BorderLayout.CENTER, new JScrollPane(venteTable));
        add(BorderLayout.EAST, contenu);
        JButton enregistrer = new JButton("Enregistrer");

        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        enregistrer.setIcon(new ImageIcon(iconSave.getPath()));

        builder.append(enregistrer);
        enregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    if (facture == null) {
                        facture = new Facture();
                    }

                    String nomClient = (String) clientCombo.getSelectedItem();
                    Client client = modelClient.getClientByName(nomClient);
                    boolean tva = tvaCheckBox.isSelected();

                    if (client == null) {
                        JOptionPane.showMessageDialog(null, "Le Client est obligatoire");
                        return;
                    }
                    Date date = new Date();
                    facture.setNomClient(nomClient);
                    facture.setDate(date);
                    facture.setApplicTva(tva);
                    if (etat.getSelectedItem() == null) {
                        JOptionPane.showMessageDialog(null, "Veillez selectionner le status de la Facture");
                        return;
                    } else {
                        facture.setEtat((EtatFacture) etat.getSelectedItem());
                    }

                    if (code <= 0) {
                        //creation d'un object facture

                        String NumFacture = generateNum(date);
                        facture.setNumeroFacture(NumFacture);
                        try {
                            for (int index = 0; index < tableModel.getRowCount(); index++) {
                                String nomProduit = (String) tableModel.getValueAt(index, 0);
                                Produit produit = modelProduit.getProduitByName(nomProduit);
                                String ref = (String) tableModel.getValueAt(index, 2);
                                Double prix = Double.parseDouble(((String)tableModel.getValueAt(index, 3)).replaceAll((""+(char) 160), ""));
                                int qte = (Integer) tableModel.getValueAt(index, 4);
                                Vente vente = new Vente(client.getCode(), produit.getCode(), ref, prix, qte, date, NumFacture);

                                facture.getVentes().add(vente);
                                int res = venteModels.saveVente(vente);
                                if (res == 1) {
                                    Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'une Vente par l'utilisateur " + UserInfo.getUsername() + " Du produit :" + produit.getNom() + " Au montant unitaire egale a " + vente.getPrixUnitaireVente());
                                    modelJournal.saveJournal(journal);
                                    modelProduit.removeQuantiteProduit(produit.getCode(), qte);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Impossible de créer le compte");
                                }
                            }

                            factureModel.saveFacture(facture);
                            Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'une Facture par l'utilisateur " + UserInfo.getUsername() + " Numero :" + facture.getNumeroFacture());
                            modelJournal.saveJournal(journal);

                            parent.setContenu(new FacturePanel(parent));

                        } catch (SQLException ex) {
                            Logger.getLogger(NouveauFacturePanel.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        try {
                            for (int index = 0; index < tableModel.getRowCount(); index++) {
                                String nomProduit = (String) tableModel.getValueAt(index, 0);
                                Produit produit = modelProduit.getProduitByName(nomProduit);
                                String ref = (String) tableModel.getValueAt(index, 2);
                                Double prix = Double.parseDouble(((String)tableModel.getValueAt(index, 3)).replaceAll((""+(char) 160), ""));
                                int qte = (Integer) tableModel.getValueAt(index, 4);
                                Vente vente = new Vente(client.getCode(), produit.getCode(), ref, prix, qte, date, facture.getNumeroFacture());
                                if (venteModels.isExist(ref)) {
                                    int res = venteModels.updateVente(vente);
                                    if (res == 1) {
                                        Journal journal = new Journal(" Modification ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Modification d'une Vente par l'utilisateur " + UserInfo.getUsername() + " Du produit :" + produit.getNom() + " Au montant unitaire egale a " + vente.getPrixUnitaireVente());
                                        modelJournal.saveJournal(journal);
                                        modelProduit.removeQuantiteProduit(produit.getCode(), qte);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Impossible de créer le compte");
                                        return;
                                    }
                                } else {
                                    int res = venteModels.saveVente(vente);
                                    if (res == 1) {
                                        Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'une Vente par l'utilisateur " + UserInfo.getUsername() + " Du produit :" + produit.getNom() + " Au montant unitaire egale a " + vente.getPrixUnitaireVente());
                                        modelJournal.saveJournal(journal);
                                        modelProduit.removeQuantiteProduit(produit.getCode(), qte);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Impossible de créer le compte");
                                        return;
                                    }
                                    facture.getVentes().add(vente);
                                }
                            }

                            factureModel.updateFacture(facture);
                            Journal journal = new Journal(" Modification ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Modification d'une Facture par l'utilisateur " + UserInfo.getUsername() + " Numero :" + facture.getNumeroFacture());
                            modelJournal.saveJournal(journal);

                            parent.setContenu(new FacturePanel(parent));

                        } catch (SQLException ex) {
                            Logger.getLogger(NouveauFacturePanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(NouveauFacturePanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            private String generateNum(Date date) {
                return "FA" + (new SimpleDateFormat("yyMMddHHmmss").format(date));
            }
        });
    }

    NouveauFacturePanel(MainMenuPanel parent, int code) throws SQLException {
        this(parent);
        this.code = code;
        facture = factureModel.findFactureByCode(code);
        clientCombo.setSelectedItem(facture.getNomClient());
        etat.setSelectedItem(facture.getEtat());
        System.out.println("Code Facture : " + facture.getCode() + "  Etat Facture : " + facture.getEtat().ordinal());

        for (int i = 0; i < facture.getVentes().size(); i++) {
            tableModel.addRow(new Object[]{
                modelProduit.findProduitByCode(facture.getVentes().get(i).getCodeProduit()).getNom(),
                facture.getNomClient(),
                facture.getVentes().get(i).getReference(),
                df.format(facture.getVentes().get(i).getPrixUnitaireVente()),
                facture.getVentes().get(i).getQuantite(),
                df.format(facture.getVentes().get(i).getPrixUnitaireVente() * facture.getVentes().get(i).getQuantite()),
                facture.getVentes().get(i).getDate()
            });
            montant += facture.getVentes().get(i).getPrixUnitaireVente() * facture.getVentes().get(i).getQuantite();
        }
        montantFactureText.setText(" " + df.format(montant) + " ");
        tvaCheckBox.setSelected(facture.isApplicTva());

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

    private javax.swing.JComboBox<String> initComboProduit(javax.swing.JComboBox<String> produitCombo) {

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

    private String generateRef(Date date) {
        return "VT" + (new SimpleDateFormat("yyMMddHHmmss").format(date));
    }

    private void viderChamp() {
        produitCombo.setSelectedItem(null);
        txtQuantite.setText("");
    }
}

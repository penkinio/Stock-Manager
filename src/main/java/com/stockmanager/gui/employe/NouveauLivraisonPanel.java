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
import com.stockmanager.data.Livraison;
import com.stockmanager.data.LivraisonEcheance;
import com.stockmanager.data.Vente;
import com.stockmanager.data.enumeration.EtatLivraison;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.service.IServiceClient;
import com.stockmanager.service.IServiceFacture;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.service.IServiceLivraison;
import com.stockmanager.service.IServiceVente;
import com.stockmanager.serviceimpl.ServiceClientImpl;
import com.stockmanager.serviceimpl.ServiceFactureImpl;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.stockmanager.serviceimpl.ServiceProduitImpl;
import com.stockmanager.serviceimpl.ServiceLivraisonImpl;
import com.stockmanager.serviceimpl.ServiceVenteImpl;
import com.stockmanager.serviceimpl.UserInfo;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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
class NouveauLivraisonPanel extends JPanel {

    private JComboBox<String> factureCombo;
    private JComboBox<String> produitCombo;
    private JTextField txtDescription;
    private JTextField txtNomClient;
    private JTextField txtQuantite;
    private String nomClient;
    String refFacture;
    private int code = -1;
    private Livraison livraison;
    private LivraisonEcheance livraisonEcheance;
    private JTable venteTable;
    private JTable livraisonTable;
    double montant = 0;
    private DefaultTableModel tableModel;
    private DefaultTableModel tableLivraisonModel;
    private MainMenuPanel parent;
    private JPanel buttonPanel;
    File iconSave;
    File iconCreate;
    File iconUpdate;
    File iconDelete;
    File iconView;
    File iconLoad;

    IServiceClient modelClient = new ServiceClientImpl();
    IServiceProduit modelProduit = new ServiceProduitImpl();
    IServiceVente venteModels = new ServiceVenteImpl();
    IServiceFacture factureModel = new ServiceFactureImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    IServiceFacture modelFacture = new ServiceFactureImpl();
    IServiceLivraison livraisonModel = new ServiceLivraisonImpl();
    DecimalFormat df = new DecimalFormat("#,##0");

    public NouveauLivraisonPanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(20, 20));
        JPanel contenu = new JPanel();
        JLabel lbl = new JLabel("LIVRAISON DANS SOKAH STOCK-MANAGER");
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        factureCombo = new JComboBox<String>();

        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        haut.add(lbl);
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));

        factureCombo = initComboFacture(factureCombo);
        txtNomClient = new JTextField();
        txtNomClient.setEditable(false);
        txtDescription = new JTextField();
        JButton btnCharger = new JButton("Charger");
        builder.append("Facture", factureCombo);
        builder.append("Nom Client", txtNomClient);
        builder.append("Description", txtDescription);
        JButton enregistrer = new JButton("Enregistrer");
        JButton btnVisualiser = new JButton("Visualiser");

        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        iconView = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/View.png");
        iconLoad = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Load.png");

        btnCharger.setIcon(new ImageIcon(iconLoad.getPath()));
        enregistrer.setIcon(new ImageIcon(iconSave.getPath()));
        btnVisualiser.setIcon(new ImageIcon(iconView.getPath()));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnCharger);
        buttonPanel.add(btnVisualiser);
        buttonPanel.add(enregistrer);
        builder.append("", buttonPanel);
        tableModel = new DefaultTableModel(new Object[]{"Produit", "Client", "Reference", "Prix Unitaire", "Quantite", "Prix Total", "Date"}, 0);
        venteTable = new JTable(tableModel);

        btnCharger.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Facture facture;
                String chaineFacture = (String) factureCombo.getSelectedItem();
                produitCombo.removeAllItems();

                if (chaineFacture == null || chaineFacture.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veillez selectionner une Facture");
                    return;
                }

                StringTokenizer st = new StringTokenizer(chaineFacture, " ");
                List<String> results = new ArrayList<String>();

                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    results.add(token);
                }

                refFacture = results.get(0);
                nomClient = results.get(2);
                facture = factureModel.findFactureByNumero(refFacture);
                txtNomClient.setText(nomClient);

                for (int i = 0; i < facture.getVentes().size(); i++) {
                    try {
                        tableModel.addRow(new Object[]{
                            modelProduit.findProduitByCode(facture.getVentes().get(i).getCodeProduit()).getNom(),
                            facture.getNomClient(),
                            facture.getVentes().get(i).getReference(),
                            df.format(facture.getVentes().get(i).getPrixUnitaireVente()),
                            facture.getVentes().get(i).getQuantite(),
                            df.format(facture.getVentes().get(i).getPrixUnitaireVente() * facture.getVentes().get(i).getQuantite()),
                            facture.getVentes().get(i).getDate()
                        });
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauLivraisonPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                produitCombo = initComboProduit(produitCombo, refFacture);
            }
        });

        btnVisualiser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame frame = new JFrame();
                Facture facture = factureModel.findFactureByNumero(refFacture);
                frame.setTitle("Liste des produit de la Facture " + facture.getNumeroFacture());
                frame.setSize(1000, 500);
                Container c = frame.getContentPane();
                c.add(BorderLayout.CENTER, new JScrollPane(venteTable));
                frame.setVisible(true);
            }
        });

        add(BorderLayout.CENTER, builder.getPanel());

//        contenu.add(BorderLayout.CENTER, new JScrollPane(venteTable));
//        add(BorderLayout.WEST, contenu);
        DefaultFormBuilder livraisonBuilder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        produitCombo = new JComboBox<String>();
        txtNomClient = new JTextField();
        txtQuantite = new JTextField();
        JButton addLivraison = new JButton("Ajouter Livraison");
        JButton removeLivraison = new JButton("Supprimer Livraison");

        iconCreate = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Create.png");
        iconDelete = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Delete.png");

        addLivraison.setIcon(new ImageIcon(iconCreate.getPath()));
        removeLivraison.setIcon(new ImageIcon(iconDelete.getPath()));

        panelButton.add(addLivraison);
        panelButton.add(removeLivraison);
        livraisonBuilder.append("Produit", produitCombo);
        livraisonBuilder.append("Quantite à Livrer", txtQuantite);
        livraisonBuilder.append("", panelButton);
        add(BorderLayout.SOUTH, livraisonBuilder.getPanel());

        addLivraison.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Produit produit;

                String nomProduit = (String) produitCombo.getSelectedItem();
                try {

                    produit = modelProduit.getProduitByName(nomProduit);
                    int qte = Integer.parseInt(txtQuantite.getText());
                    Date date = new Date();
                    livraisonEcheance = new LivraisonEcheance(produit.getCode(), qte, date);
                } catch (SQLException ex) {
                    Logger.getLogger(NouveauLivraisonPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    tableLivraisonModel.addRow(new Object[]{
                        modelProduit.findProduitByCode(livraisonEcheance.getCodeProduit()).getNom(),
                        livraisonEcheance.getQuantiteLivre(),
                        livraisonEcheance.getDateLivraison()
                    });
                    viderChamp();
                } catch (SQLException ex) {
                    Logger.getLogger(NouveauLivraisonPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void viderChamp() {
                produitCombo.setSelectedItem(null);
                txtQuantite.setText("");
            }
        });
        
        removeLivraison.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = livraisonTable.getSelectedRow();
                
                if (selected >= 0) {
                   tableLivraisonModel.removeRow(selected);
                } else {
                    JOptionPane.showMessageDialog(null, "Aucune Livraison selectionnée");
                }
            }
        });

        tableLivraisonModel = new DefaultTableModel(new Object[]{"Produit", "Quantite à Livrer", "Date"}, 0);
        livraisonTable = new JTable(tableLivraisonModel);
        contenu.add(BorderLayout.CENTER, new JScrollPane(livraisonTable));
        add(BorderLayout.EAST, contenu);

        enregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    Facture facture;
                    Livraison livraison = new Livraison();

                    Client client = modelClient.getClientByName(nomClient);
                    facture = modelFacture.findFactureByNumero(refFacture);
                    Date date = new Date();

                    if (client == null) {
                        JOptionPane.showMessageDialog(null, "Le Client est obligatoire");
                        return;
                    }

                    String odreLivraison = generateOL(date);
                    livraison.setOrdreLivraision(odreLivraison);
                    livraison.setDateLivraison(date);
                    livraison.setDescription(txtDescription.getText());
                    livraison.setCodeFacture(facture.getCode());
                    try {
                        livraisonEcheance = new LivraisonEcheance();
                        for (int index = 0; index < tableLivraisonModel.getRowCount(); index++) {
                            livraisonEcheance.setRefLivraison(odreLivraison);
                            livraisonEcheance.setCodeProduit(modelProduit.getProduitByName((String) tableLivraisonModel.getValueAt(index, 0)).getCode());
                            livraisonEcheance.setQuantiteLivre((Integer) tableLivraisonModel.getValueAt(index, 1));
                            livraisonEcheance.setDateLivraison((Date) tableLivraisonModel.getValueAt(index, 2));

                            livraison.getLivraisonEcheances().add(livraisonEcheance);
                            livraisonModel.saveLivraisonEcheance(livraisonEcheance);
                        }

                        livraison = setEtatLivraison(livraison, tableLivraisonModel);
                        livraisonModel.saveLivraison(livraison);
                        Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'une Livraison par l'utilisateur " + UserInfo.getUsername() + " Numero :" + livraison.getOrdreLivraision());
                        modelJournal.saveJournal(journal);

                        parent.setContenu(new LivraisonPanel(parent));

                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauLivraisonPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(NouveauLivraisonPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            private Livraison setEtatLivraison(Livraison livraison, DefaultTableModel table) throws SQLException {
                Facture facture = factureModel.findFactureByCode(livraison.getCodeFacture());
                List<Vente> listeVente = venteModels.getListeVenteByFacture(facture.getNumeroFacture());

                if (table.getRowCount() == 0) {
                    livraison.setEtat(EtatLivraison.NonLivrer);
                    return livraison;
                }

                if (table.getRowCount() > 0 && table.getRowCount() < listeVente.size()) {
                    livraison.setEtat(EtatLivraison.EnCourDeLivraison);
                    return livraison;
                }

                if (table.getRowCount() == listeVente.size()) {
                    for (int index = 0; index < table.getRowCount(); index++) {
                        int index2 = 0;
                        while (listeVente.get(index2).getCodeProduit() != modelProduit.getProduitByName((String) table.getValueAt(index, 0)).getCode() && index2 < table.getRowCount()) {
                            index2++;
                        }
                        if (listeVente.get(index2).getCodeProduit() == modelProduit.getProduitByName((String) table.getValueAt(index, 0)).getCode()) {
                            if ((Integer) table.getValueAt(index, 1) < listeVente.get(index2).getQuantite()) {
                                livraison.setEtat(EtatLivraison.EnCourDeLivraison);
                                return livraison;
                            } else if ((Integer) table.getValueAt(index, 1) > listeVente.get(index2).getQuantite()) {
                                JOptionPane.showMessageDialog(null, "Vous depassez la quantité qui devrait etre livré");
                                return livraison;
                            }
                        }
                    }
                    livraison.setEtat(EtatLivraison.Livrer);
                }
                return livraison;
            }
        });
    }

    NouveauLivraisonPanel(MainMenuPanel parent, String ol) throws SQLException {
        this(parent);
        this.code = code;
        Facture facture;
        livraison = livraisonModel.getLivraisonByOrder(ol);
        facture = factureModel.findFactureByCode(livraison.getCodeFacture());
        factureCombo.setSelectedItem(facture.getNomClient());

        List<LivraisonEcheance> listEcheances = livraisonModel.getLivraisonEcheances(ol);
        for (int i = 0; i < listEcheances.size(); i++) {
            tableLivraisonModel.addRow(new Object[]{
                        modelProduit.findProduitByCode(listEcheances.get(i).getCodeProduit()).getNom(),
                        listEcheances.get(i).getQuantiteLivre(),
                        listEcheances.get(i).getDateLivraison()
                    });
        }
        txtNomClient.setText(" " + facture.getNomClient() + " ");
    }

    private JComboBox<String> initComboFacture(JComboBox<String> factureCombo) {
        factureCombo.addItem(null);

        for (Facture f : modelFacture.findAllFactureRegler()) {
            factureCombo.addItem(f.getNumeroFacture() + " --> " + f.getNomClient());
        }
        return factureCombo;
    }

    private javax.swing.JComboBox<String> initComboProduit(javax.swing.JComboBox<String> produitCombo, String facture) {
        //produitCombo = new JComboBox<String>();

        produitCombo.addItem(null);
        for (String produit : venteModels.findAllProduitNameFacture(facture)) {
            produitCombo.addItem(produit);
        }
        return produitCombo;
    }

    private String generateOL(Date date) {
        return "OL" +(new SimpleDateFormat("yyMMddHHmmss").format(date));
    }
}

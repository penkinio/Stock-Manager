package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Journal;
import com.stockmanager.data.MouvementStock;
import com.stockmanager.data.Produit;
import com.stockmanager.data.enumeration.ESensMouvement;
import com.stockmanager.data.enumeration.RoleType;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.service.IServiceMouvement;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.stockmanager.serviceimpl.ServiceProduitImpl;
import com.stockmanager.serviceimpl.ServiceMouvementImpl;
import com.stockmanager.serviceimpl.UserInfo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * classe permettant d'enregistrer un nouveau mouvement
 */
public class NouveauMouvementPanel extends JPanel {

    private JTextField descriptionText;
    private JComboBox<String> produitCombo;
    private JTextField quantiteText;
    private JComboBox<ESensMouvement> sens;
    private int code = -1;
    private MainMenuPanel parent;
    IServiceMouvement model = new ServiceMouvementImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    IServiceProduit modelProduit = new ServiceProduitImpl();
    MouvementStock mouv;
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public NouveauMouvementPanel() {
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public NouveauMouvementPanel(MainMenuPanel parentFrame) {
        setLayout(new BorderLayout(20, 20));
        this.parent = parentFrame;
        JLabel lbl = new JLabel("AJOUT D'UN NOUVEAU MOUVEMENT DANS STOCK-MANAGER");
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        produitCombo = new JComboBox<String>();
        descriptionText = new JTextField();
        quantiteText = new JTextField();
        sens = new JComboBox<ESensMouvement>();
        JButton enregistrer = new JButton("Enregistrer");

        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        enregistrer.setIcon(new ImageIcon(iconSave.getPath()));

        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        haut.add(lbl);
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        produitCombo = initComboProduit(produitCombo);
        builder.append("Description", descriptionText);
        builder.append("Produit", produitCombo);
        builder.append("Quantité", quantiteText);
        builder.append("Sens", sens);
        sens.addItem(null);
        sens.addItem(ESensMouvement.entre);
        sens.addItem(ESensMouvement.sortie);
        builder.append(enregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        enregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    String description = descriptionText.getText();
                    int qte = Integer.parseInt(quantiteText.getText());
                    Date date = new Date();
                    String nomProduit = (String) produitCombo.getSelectedItem();
                    Produit produit = modelProduit.getProduitByName(nomProduit);
                    ESensMouvement ss = (ESensMouvement) sens.getSelectedItem();
                    if ((description == null) || ("".equals(description))) {
                        JOptionPane.showMessageDialog(null, "Le description est obligatoire");
                        return;
                    }
                    if (qte <= 0) {
                        JOptionPane.showMessageDialog(null, "La quantité est obligatoire et doit etre une valeur positive");
                        return;
                    }

                    if (ss == null) {
                        JOptionPane.showMessageDialog(null, "Le sens est obligatoire");
                        return;
                    }

                    if (code <= 0) {
                        //creation d'un object mouvement
                        mouv = new MouvementStock(description, date, produit.getCode(), qte, ss);

                        //insersion d'un object mouvement dans la base de donnée
                        int res = model.saveMouvement(mouv);
                        if (res == 1) {
                            Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'un Mouvement de stock par l'utilisateur " + UserInfo.getUsername() + " Description :" + description);
                            modelJournal.saveJournal(journal);
                            if (ss.equals(ESensMouvement.entre)) {
                                modelProduit.addQuantiteProduit(produit.getCode(), qte);
                            } else {
                                modelProduit.removeQuantiteProduit(produit.getCode(), qte);
                            }
                            JOptionPane.showMessageDialog(null, "Nouveau mouvement creer");
                        } else {
                            JOptionPane.showMessageDialog(null, "Impossible de créer le mouvement");
                        }
                    } else {
                        mouv.setCodeProduit(produit.getCode());
                        mouv.setDateMouvement(date);
                        mouv.setDescription(description);
                        mouv.setQuantiteProduit(qte);
                        mouv.setSens(ss);
                        int res = model.updateMouvement(mouv);
                        if (res == 1) {
                            Journal journal = new Journal(" Modification ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Modification d'un Mouvement de stock par l'utilisateur " + UserInfo.getUsername() + " Description :" + description);
                            modelJournal.saveJournal(journal);
                            if (ss.equals(ESensMouvement.entre)) {
                                modelProduit.addQuantiteProduit(produit.getCode(), qte);
                            } else {
                                modelProduit.removeQuantiteProduit(produit.getCode(), qte);
                            }
                            JOptionPane.showMessageDialog(null, "Mouvement modifié");
                        } else {
                            JOptionPane.showMessageDialog(null, "Impossible de créer le mouvement");
                        }
                    }

                    try {
                        parent.setContenu(new MouvementPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauMouvementPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(NouveauMouvementPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     *
     * @param parentFrame
     * @param code
     * @throws java.sql.SQLException
     */
    public NouveauMouvementPanel(MainMenuPanel parentFrame, Integer code) throws SQLException {
        this(parentFrame);
        this.code = code;
        mouv = model.getMouvementByCode(code);
        descriptionText.setText(mouv.getDescription());
        produitCombo.setSelectedItem(modelProduit.findProduitByCode(mouv.getCodeProduit()).getNom());
        quantiteText.setText("" + mouv.getQuantiteProduit());
        sens.setSelectedItem(mouv.getSens());
    }

    private JComboBox<String> initComboProduit(JComboBox<String> produitCombo) {

        List<Produit> listeProduit = new ArrayList<Produit>();

        if (UserInfo.getRole().equals(RoleType.AdministrateurPrincipal) || UserInfo.getRole().equals(RoleType.Administrateur)) {
            try {
                listeProduit = modelProduit.findAllProduit();
            } catch (SQLException ex) {
                Logger.getLogger(NouveauMouvementPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            listeProduit = modelProduit.findProduitByNonCmmercialisable();
        }
        produitCombo.addItem(null);

        for (Produit produit : listeProduit) {
            produitCombo.addItem(produit.getNom());
        }
        return produitCombo;
    }
}

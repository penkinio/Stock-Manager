package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Journal;
import com.stockmanager.data.Produit;
import com.stockmanager.data.TypeProduit;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.service.IServiceTypeProduit;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.stockmanager.serviceimpl.ServiceProduitImpl;
import com.stockmanager.serviceimpl.ServiceTypeProduitImpl;
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
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * @author Ngnawen Samuel<sngawen@yahoo.fr>
 * classe permettant d'afficher le panel d'enregistrement d'un nouveau client
 */
public class NouveauProduitPanel extends JPanel {

    private JTextField nomText;
    private JComboBox<String> typeProduitCombo;
    private int code = -1;
    private MainMenuPanel parent;
    IServiceProduit model = new ServiceProduitImpl();
    IServiceTypeProduit modelType = new ServiceTypeProduitImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public NouveauProduitPanel() {
    }

    /**
     * Constructeur avec 2 arguments
     *
     * @param parentFrame
     * @param code
     * @throws SQLException
     */
    public NouveauProduitPanel(MainMenuPanel parentFrame, int code) throws SQLException {
        this(parentFrame);
        this.code = code;
        Produit c = model.findProduitByCode(code);
        nomText.setText(c.getNom());
        typeProduitCombo.setSelectedItem(modelType.findTypeProduitByCode(c.getCodeType()).getLibelle());
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public NouveauProduitPanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(10, 10));
        nomText = new JTextField();
        typeProduitCombo = new JComboBox<String>();
        JButton btnEnregistrer = new JButton("Enrégistrer");
        
        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        btnEnregistrer.setIcon(new ImageIcon(iconSave.getPath()));
        
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("AJOUT D'UN NOUVEAU PRODUIT DANS STOCK-MANAGER");
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        typeProduitCombo = initComboTypeProduit(typeProduitCombo);
        builder.append("Type De Produit", typeProduitCombo);
        builder.append("Nom", nomText);
        builder.append(btnEnregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        btnEnregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (code <= 0) {
                    String nom = nomText.getText();
                    String libelle = (String) typeProduitCombo.getSelectedItem();
                    TypeProduit typeProduit = modelType.getTypeProduitByLibelle(libelle);

                    verification(nom,0,libelle);

                    Produit c = new Produit(nom, 0.0, 0, typeProduit.getCode());

                    if (model.isExist(nom)) {
                        JOptionPane.showMessageDialog(null, "Le Produit " + nom + " Existe déjà !!!");
                    } else {
                        int resul = model.saveProduit(c);
                        if (resul == 1) {
                            Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'un Produit par l'utilisateur " + UserInfo.getUsername() + " de nom :" + nom);
                            modelJournal.saveJournal(journal);
                            JOptionPane.showMessageDialog(null, "Le Produit " + nom + " a été créé");
                        } else {
                            JOptionPane.showMessageDialog(null, "Impossible de creer ce Produit");
                        }
                    }

                    try {
                        parent.setContenu(new ProduitPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String nom = nomText.getText();
                    String libelle = (String) typeProduitCombo.getSelectedItem();
                    TypeProduit typeProduit = modelType.getTypeProduitByLibelle(libelle);

                    verification(nom, 0, libelle);

                    int result = model.modifierProduit(nom,0, code,typeProduit.getCode());

                    if (result == 1) {
                        Journal journal = new Journal(" Modification ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Modification d'un Produit par l'utilisateur " + UserInfo.getUsername() + " de nom :" + nom);
                        modelJournal.saveJournal(journal);
                        JOptionPane.showMessageDialog(null, "Operation reussit");
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de mettre a jour le produit");
                    }
                    try {
                        parent.setContenu(new ProduitPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            private void verification(String nom,Integer quantite, String libelle) {

                if ((libelle == null) || ("".equals(libelle))) {
                    JOptionPane.showMessageDialog(null, "Le libelle est obligatoire");
                    return;
                }

                if ((nom == null) || ("".equals(nom))) {
                    JOptionPane.showMessageDialog(null, "Le nom est obligatoire");
                    return;
                }
                
                if ((quantite == null) || ("".equals(quantite))) {
                    JOptionPane.showMessageDialog(null, "La quantite est obligatoire");
                }

            }
        });
    }

    private JComboBox<String> initComboTypeProduit(JComboBox<String> typeProduitCombo) {
        typeProduitCombo.addItem(null);

        for (TypeProduit typeProduit : modelType.findAllTypeProduit()) {
            typeProduitCombo.addItem(typeProduit.getLibelle());
        }
        return typeProduitCombo;
    }
}

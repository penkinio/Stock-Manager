package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Journal;
import com.stockmanager.data.CategorieClient;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceCategorie;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.stockmanager.serviceimpl.ServiceCategorieImpl;
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
public class NouveauCategoriePanel extends JPanel {

    private JTextField nomText;
    private JTextField tauxRemiseText;
    private int code = -1;
    private MainMenuPanel parent;
    IServiceCategorie model = new ServiceCategorieImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public NouveauCategoriePanel() {
    }

    /**
     * Constructeur avec 2 arguments
     *
     * @param parentFrame
     * @param code
     * @throws SQLException
     */
    public NouveauCategoriePanel(MainMenuPanel parentFrame, int code) throws SQLException {
        this(parentFrame);
        this.code = code;
        CategorieClient c = model.findCategorieByCode(code);
        nomText.setText(c.getNom());
        tauxRemiseText.setText(c.getTauxRemise().toString());
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public NouveauCategoriePanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(10, 10));
        nomText = new JTextField();
        tauxRemiseText = new JTextField();
        JButton btnEnregistrer = new JButton("Enrégistrer");
        
        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        btnEnregistrer.setIcon(new ImageIcon(iconSave.getPath()));
        
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("AJOUT D'UN NOUVEAU CATEGORIE DE CLIENT DANS STOCK-MANAGER");
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Nom", nomText);
        builder.append("Taux De Remise", tauxRemiseText);
        builder.append(btnEnregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        btnEnregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (code <= 0) {
                    String nom = nomText.getText();
                    Double taux = Double.parseDouble(tauxRemiseText.getText());

                    verification(nom, taux);

                    CategorieClient c = new CategorieClient(nom, taux);

                    if (model.isExist(nom)) {
                        JOptionPane.showMessageDialog(null, "Categorie " + nom + " Existe déjà !!!");
                    } else {
                        int resul = model.saveCategorie(c);
                        if (resul == 1) {
                            Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'un Produit par l'utilisateur " + UserInfo.getUsername() + " de nom :" + nom);
                            modelJournal.saveJournal(journal);
                            JOptionPane.showMessageDialog(null, "La Categorie " + nom + " a été créé");
                        } else {
                            JOptionPane.showMessageDialog(null, "Impossible de creer ce Type de Produit");
                        }
                    }

                    try {
                        parent.setContenu(new CategoriePanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String nom = nomText.getText();
                    Double taux = Double.parseDouble(tauxRemiseText.getText());

                    verification(nom, taux);

                    int result = model.modifierCategorie(nom, taux, code);

                    if (result == 1) {
                        Journal journal = new Journal(" Modification ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Modification d'un Produit par l'utilisateur " + UserInfo.getUsername() + " de nom :" + nom);
                        modelJournal.saveJournal(journal);
                        JOptionPane.showMessageDialog(null, "Operation reussit");
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de mettre a jour la Categorie");
                    }
                    try {
                        parent.setContenu(new CategoriePanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            private void verification(String nom, Double taux) {
                if ((nom == null) || ("".equals(nom))) {
                    JOptionPane.showMessageDialog(null, "Le Nom est obligatoire");
                    return;
                }

                if (taux == null) {
                    JOptionPane.showMessageDialog(null, "Le Taux De Remise est obligatoire");
                    return;
                }
            }
        });
    }
}

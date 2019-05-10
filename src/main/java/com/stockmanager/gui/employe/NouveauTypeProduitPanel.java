package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.Journal;
import com.stockmanager.data.TypeProduit;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceTypeProduit;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
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
public class NouveauTypeProduitPanel extends JPanel {

    private JTextField libelleText;
    private JTextField localisationText;
    private int code = -1;
    private MainMenuPanel parent;
    IServiceTypeProduit model = new ServiceTypeProduitImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public NouveauTypeProduitPanel() {
    }

    /**
     * Constructeur avec 2 arguments
     *
     * @param parentFrame
     * @param code
     * @throws SQLException
     */
    public NouveauTypeProduitPanel(MainMenuPanel parentFrame, int code) throws SQLException {
        this(parentFrame);
        this.code = code;
        TypeProduit c = model.findTypeProduitByCode(code);
        libelleText.setText(c.getLibelle());
        localisationText.setText(c.getLocalisation());
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public NouveauTypeProduitPanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(10, 10));
        libelleText = new JTextField();
        localisationText = new JTextField();
        JButton btnEnregistrer = new JButton("Enrégistrer");

        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        btnEnregistrer.setIcon(new ImageIcon(iconSave.getPath()));

        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("AJOUT D'UN NOUVEAU PRODUIT DANS STOCK-MANAGER");
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Libelle", libelleText);
        builder.append("Localisation", localisationText);
        builder.append(btnEnregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        btnEnregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (code <= 0) {
                    String lib = libelleText.getText();
                    String loc = localisationText.getText();

                    verification(lib, loc);

                    TypeProduit c = new TypeProduit(lib, loc);

                    if (model.isExist(lib)) {
                        JOptionPane.showMessageDialog(null, "Le Type de Produit " + lib + " Existe déjà !!!");
                    } else {
                        int resul = model.saveTypeProduit(c);
                        if (resul == 1) {
                            Journal journal = new Journal(" Creation ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Creation d'un Produit par l'utilisateur " + UserInfo.getUsername() + " de nom :" + lib);
                            modelJournal.saveJournal(journal);
                            JOptionPane.showMessageDialog(null, "Le Type de Produit " + lib + " a été créé");
                        } else {
                            JOptionPane.showMessageDialog(null, "Impossible de creer ce Type de Produit");
                        }
                    }

                    try {
                        parent.setContenu(new TypeProduitPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String lib = libelleText.getText();
                    String loc = localisationText.getText();

                    verification(lib, loc);

                    int result = model.modifierTypeProduit(lib, loc, code);

                    if (result == 1) {
                        Journal journal = new Journal(" Modification ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Modification d'un Produit par l'utilisateur " + UserInfo.getUsername() + " de nom :" + lib);
                        modelJournal.saveJournal(journal);
                        JOptionPane.showMessageDialog(null, "Operation reussit");
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de mettre a jour le produit");
                    }
                    try {
                        parent.setContenu(new TypeProduitPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            private void verification(String lib, String loc) {
                if ((lib == null) || ("".equals(lib))) {
                    JOptionPane.showMessageDialog(null, "Le Libelle est obligatoire");
                    return;
                }

                if ((loc == null) || ("".equals(loc))) {
                    JOptionPane.showMessageDialog(null, "La Localisation est obligatoire");
                    return;
                }
            }
        });
    }
}

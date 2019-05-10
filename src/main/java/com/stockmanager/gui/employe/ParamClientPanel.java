package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.data.CategorieClient;
import com.stockmanager.data.Journal;
import com.stockmanager.data.Client;
import com.stockmanager.service.IServiceCategorie;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.service.IServiceClient;
import com.stockmanager.serviceimpl.ServiceCategorieImpl;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.stockmanager.serviceimpl.ServiceClientImpl;
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

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * @author Ngnawen Samuel<sngawen@yahoo.fr>
 * classe permettant d'afficher le panel d'enregistrement d'un nouveau client
 */
public class ParamClientPanel extends JPanel {

    private JComboBox<String> categorieCombo;
    private JComboBox<String> clientCombo;
    private MainMenuPanel parent;
    IServiceClient model = new ServiceClientImpl();
    IServiceCategorie modelCategorie = new ServiceCategorieImpl();
    IServiceJournal modelJournal = new ServiceJournalImpl();
    File iconSave;

    /**
     * constructeur sans arguments
     */
    public ParamClientPanel() {
    }

    /**
     * constructeur avec 1 argument
     *
     * @param parentFrame
     */
    public ParamClientPanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(10, 10));
        clientCombo = new JComboBox<String>();
        categorieCombo = new JComboBox<String>();
        JButton btnEnregistrer = new JButton("Enrégistrer");
        
        iconSave = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Save.png");
        btnEnregistrer.setIcon(new ImageIcon(iconSave.getPath()));
        
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("PARAMETRAGE DES CLIENTS");
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        clientCombo = initComboClient(clientCombo);
        categorieCombo = initComboCategorie(categorieCombo);
        builder.append("Client", clientCombo);
        builder.append("Categorie", categorieCombo);
        builder.append(btnEnregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        btnEnregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    String nomCategorie = (String) categorieCombo.getSelectedItem();
                    CategorieClient categorie = modelCategorie.findCategorieByName(nomCategorie);
                    String nom = (String) clientCombo.getSelectedItem();
                    Client client = model.getClientByName(nom);

                    verification(nomCategorie, nom);

                    //int resul = model.modifierClient(client.getCode(), categorie.getCode());
//                    if (resul == 1) {
//                        Journal journal = new Journal(" Parametrage Client ", UserInfo.getPrenom() + " " + UserInfo.getNom(), new Date(), " Parametrage Prix d'un Client par l'utilisateur " + UserInfo.getUsername() + " de nom :" + nom);
//                        modelJournal.saveJournal(journal);
//                        JOptionPane.showMessageDialog(null, "Le Client " + nom + " a été parametre");
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Impossible de parametre ce Client");
//                    }

                    try {
                        parent.setContenu(new ClientPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ParamClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void verification(String categorie, String libelle) {
                if ((categorie == null) || ("".equals(categorie))) {
                    JOptionPane.showMessageDialog(null, "La Categorie est obligatoire");
                }

                if ((libelle == null) || ("".equals(libelle))) {
                    JOptionPane.showMessageDialog(null, "Le Client est obligatoire");
                }
            }
        });
    }

    private JComboBox<String> initComboClient(JComboBox<String> clientCombo) {
        clientCombo.addItem(null);

        try {
            for (Client client : model.findAllClient()) {
                clientCombo.addItem(client.getNom());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParamClientPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clientCombo;
    }

    private JComboBox<String> initComboCategorie(JComboBox<String> categorieCombo) {
        categorieCombo.addItem(null);

        for (CategorieClient categorie : modelCategorie.findAllCategorie()) {
            categorieCombo.addItem(categorie.getNom());
        }
        return categorieCombo;
    }
}

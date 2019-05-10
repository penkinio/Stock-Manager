package com.stockmanager.gui.employe;

import com.stockmanager.data.CategorieClient;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.service.IServiceCategorie;
import com.stockmanager.serviceimpl.ServiceCategorieImpl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * @author Ngnawen Samuel<sngawen@yahoo.fr>
 * classe permettant d'afficher la liste des Categories appartenant a un client
 */
public class CategoriePanel extends JPanel {

    IServiceCategorie modelbd = new ServiceCategorieImpl();

    private JTable categorieTable;
    private DefaultTableModel tableModel;
    private JTextField nomText;
    private JTextField tauxRemiseText;
    private MainMenuPanel parent;
    IServiceCategorie model = new ServiceCategorieImpl();

    /**
     *
     */
    public CategoriePanel() {
    }

    /**
     *
     * @param parentFrame
     * @throws SQLException
     */
    public CategoriePanel(MainMenuPanel parentFrame) throws SQLException {
        JButton nouveauBtn;
        JButton supprimerBtn;
        JButton modifierBtn;
        JButton filtreBtn;
        JButton imprimerBtn;
        File iconCreate;
        File iconUpdate;
        File iconDelete;
        File iconSearch;
        File iconPrint;

        this.parent = parentFrame;
        setLayout(new BorderLayout());
        JLabel lbl;
        lbl = new JLabel("LA LISTE DES CATEGORIES DE CLIENTS");
        nomText = new JTextField();
        tauxRemiseText = new JTextField();
        JPanel contenu = new JPanel();
        JPanel haut = new JPanel();
        JPanel bas = new JPanel();
        JPanel filtrePanel = new JPanel();
        bas.setLayout(new FlowLayout());
        nouveauBtn = new JButton("Ajouter");
        supprimerBtn = new JButton("Supprimer");
        modifierBtn = new JButton("Modifier");
        imprimerBtn = new JButton("Imprimer");
        filtreBtn = new JButton("Filtrer");
        
        iconCreate = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Create.png");
        iconUpdate = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Modify.png");
        iconDelete = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Delete.png");
        iconSearch = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Search.png");
        iconPrint = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Print.png");

        nouveauBtn.setIcon(new ImageIcon(iconCreate.getPath()));
        supprimerBtn.setIcon(new ImageIcon(iconDelete.getPath()));
        modifierBtn.setIcon(new ImageIcon(iconUpdate.getPath()));
        imprimerBtn.setIcon(new ImageIcon(iconPrint.getPath()));
        filtreBtn.setIcon(new ImageIcon(iconSearch.getPath()));
        
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        //Bouton permettant de filtrer les informations sur les produits
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String lib = nomText.getText();

                List<CategorieClient> listCategorie = model.findCategorieByCritere(lib);
                viderTable(tableModel, listCategorie.size());
                for (int i = 0; i < listCategorie.size(); i++) {
                    tableModel.addRow(new Object[]{
                        listCategorie.get(i).getNom(),
                        listCategorie.get(i).getTauxRemise()
                    });
                }
            }

            private void viderTable(DefaultTableModel tableModel, int size) {
                for (int i = 0; i < size; i++) {
                    tableModel.removeRow(i);
                }
            }
        });
        //bouton permetant l'affichage d'une petite fenetre pour l'insertion d'un nouveau produit
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauCategoriePanel(parent));
            }
        });
        //bouton permettant l'affichage la fenetre de modification
        modifierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = categorieTable.getSelectedRow();
                if (selected >= 0) {
                    try {
                        parent.setContenu(new NouveauCategoriePanel(parent, (Integer) tableModel.getValueAt(selected, 0)));
                    } catch (SQLException ex) {
                        Logger.getLogger(CategoriePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun produit selectionné");
                }
            }
        });
        //bouton permettant de supprimer les produits
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = categorieTable.getSelectedRow();
                if (selected >= 0) {
                    if (JOptionPane.showConfirmDialog(parent, "Voulez vous vraiment Supprimez?") == JOptionPane.OK_OPTION) {
                        int resultat = modelbd.deleteCategorieByCode((Integer) tableModel.getValueAt(selected, 0));
                        switch (resultat) {
                            case 1:
                                tableModel.removeRow(selected);
                                break;
                            case 2:
                                JOptionPane.showMessageDialog(null, "Impossible de supprimer ce Type de produit");
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "Impossible de fermer la ressource");
                                break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucune ligne de la table n'est selectionnee");
                }

            }
        });
        bas.add(nouveauBtn);
        bas.add(modifierBtn);
        bas.add(supprimerBtn);
        filtrePanel.setLayout(new FlowLayout());
        filtrePanel.add(new JLabel("Nom"));
        filtrePanel.add(nomText);
        nomText.setPreferredSize(new Dimension(100, 25));

        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Libelle Categorie", "Taux de Remise"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categorieTable = new JTable(tableModel);
        contenu.add(BorderLayout.CENTER, new JScrollPane(categorieTable));
        add(BorderLayout.CENTER, contenu);
        List<CategorieClient> listCategorie = model.findAllCategorie();
        for (int i = 0; i < listCategorie.size(); i++) {
            tableModel.addRow(new Object[]{
                listCategorie.get(i).getNom(),
                listCategorie.get(i).getTauxRemise()
            });
        }

    }
}

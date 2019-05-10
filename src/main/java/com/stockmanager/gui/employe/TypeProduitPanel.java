package com.stockmanager.gui.employe;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.TypeProduit;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.report.IPrintReportService;
import com.stockmanager.report.impl.PrintReportServiceImpl;
import com.stockmanager.service.IServiceTypeProduit;
import com.stockmanager.serviceimpl.LogoInfo;
import com.stockmanager.serviceimpl.ServiceTypeProduitImpl;
import com.stockmanager.serviceimpl.UserInfo;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * @author Ngnawen Samuel<sngawen@yahoo.fr>
 * classe permettant d'afficher la liste des TypeProduits appartenant a un
 * client
 */
public class TypeProduitPanel extends JPanel {

    IServiceTypeProduit modelbd = new ServiceTypeProduitImpl();

    private JTable typeProduitTable;
    private DefaultTableModel tableModel;
    private JTextField libelleText;
    private JTextField localisationText;
    private MainMenuPanel parent;
    IServiceTypeProduit model = new ServiceTypeProduitImpl();
    IPrintReportService print = new PrintReportServiceImpl();

    /**
     *
     */
    public TypeProduitPanel() {
    }

    /**
     *
     * @param parentFrame
     * @throws SQLException
     */
    public TypeProduitPanel(MainMenuPanel parentFrame) throws SQLException {
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
        lbl = new JLabel("LA LISTE DES TYPES DE PRODUIT");
        libelleText = new JTextField();
        localisationText = new JTextField();
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
                String lib = libelleText.getText();
                String loc = localisationText.getText();

                List<TypeProduit> listTypeProduit = model.findTypeProduitByCritere(lib, loc);
                viderTable(tableModel, listTypeProduit.size());
                for (int i = 0; i < listTypeProduit.size(); i++) {
                    tableModel.addRow(new Object[]{
                        listTypeProduit.get(i).getCode(),
                        listTypeProduit.get(i).getLibelle(),
                        listTypeProduit.get(i).getLocalisation()
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
                parent.setContenu(new NouveauTypeProduitPanel(parent));
            }
        });
        //bouton permettant l'affichage la fenetre de modification
        modifierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = typeProduitTable.getSelectedRow();
                if (selected >= 0) {
                    try {
                        parent.setContenu(new NouveauTypeProduitPanel(parent, (Integer) tableModel.getValueAt(selected, 0)));
                    } catch (SQLException ex) {
                        Logger.getLogger(TypeProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun produit selectionné");
                }
            }
        });
        //bouton permettant de supprimer les produits
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = typeProduitTable.getSelectedRow();
                if (selected >= 0) {
                    if (JOptionPane.showConfirmDialog(parent, "Voulez vous vraiment Supprimez?") == JOptionPane.OK_OPTION) {
                        int resultat = modelbd.deleteTypeProduitByCode((Integer) tableModel.getValueAt(selected, 0));
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
        filtrePanel.add(new JLabel("Libelle"));
        filtrePanel.add(libelleText);
        filtrePanel.add(new JLabel("Locisation"));
        filtrePanel.add(localisationText);
        libelleText.setPreferredSize(new Dimension(100, 25));
        localisationText.setPreferredSize(new Dimension(100, 25));

        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Code", "Libelle Type de produit", "Localisation"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        typeProduitTable = new JTable(tableModel);
        typeProduitTable.removeColumn(typeProduitTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(typeProduitTable));
        add(BorderLayout.CENTER, contenu);
        List<TypeProduit> listTypeProduit = model.findAllTypeProduit();
        for (int i = 0; i < listTypeProduit.size(); i++) {
            tableModel.addRow(new Object[]{
                listTypeProduit.get(i).getCode(),
                listTypeProduit.get(i).getLibelle(),
                listTypeProduit.get(i).getLocalisation()
            });
        }

    }
}

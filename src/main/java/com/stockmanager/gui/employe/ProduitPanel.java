package com.stockmanager.gui.employe;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.Produit;
import com.stockmanager.data.TypeProduit;
import com.stockmanager.data.enumeration.EGenreProduit;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.report.IPrintReportService;
import com.stockmanager.report.impl.PrintReportServiceImpl;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.service.IServiceTypeProduit;
import com.stockmanager.serviceimpl.LogoInfo;
import com.stockmanager.serviceimpl.ServiceProduitImpl;
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
import javax.swing.JComboBox;
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
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * classe permettant d'afficher la liste des Produits appartenant a un client
 */
public class ProduitPanel extends JPanel {

    IServiceProduit modelbd = new ServiceProduitImpl();

    private JTable produitTable;
    private DefaultTableModel tableModel;
    private JTextField nameText;
    private JComboBox<EGenreProduit> genre;
    private JComboBox<String> typeProduit;
    private MainMenuPanel parent;
    IServiceProduit model = new ServiceProduitImpl();
    IServiceTypeProduit modelType = new ServiceTypeProduitImpl();
    IPrintReportService print = new PrintReportServiceImpl();

    /**
     *
     */
    public ProduitPanel() {
    }

    /**
     *
     * @param parentFrame
     * @throws SQLException
     */
    public ProduitPanel(MainMenuPanel parentFrame) throws SQLException {
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
        lbl = new JLabel("LA LISTE DES PRODUITS");
        nameText = new JTextField();
        genre = new JComboBox<EGenreProduit>();
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
                String name = nameText.getText();
                EGenreProduit genreP = (EGenreProduit) genre.getSelectedItem();
                String typeP = (String) typeProduit.getSelectedItem();

                StringBuilder query = new StringBuilder("select * from produit where quantiteStock > 0");
                if ((name != null) && !("".equals(name))) {
                    query.append(" and nom like '%");
                    query.append(name);
                    query.append("%'");
                }
                if (genreP != null) {
                    query.append(" and genre = ");
                    query.append(genreP.ordinal());
                }
                if (typeP != null) {
                    query.append(" and codeType = ");
                    query.append(modelType.findTypeProduitByName(typeP).getCode());
                }

                tableModel.setRowCount(0);

                List<Produit> listProduit = model.findProduitByCriteria(query.toString());
                for (int i = 0; i < listProduit.size(); i++) {
                    tableModel.addRow(new Object[]{
                        listProduit.get(i).getCode(),
                        listProduit.get(i).getNom(),
                        listProduit.get(i).getPrixUnitaire(),
                        listProduit.get(i).getQuanteStock(),
                        modelType.findTypeProduitByCode(listProduit.get(i).getCodeType()).getLibelle(),
                        listProduit.get(i).getGenre()
                    });
                }
            }
        });
        //bouton permetant l'affichage d'une petite fenetre pour l'insertion d'un nouveau produit
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauProduitPanel(parent));
            }
        });
        //bouton permettant l'affichage la fenetre de modification
        modifierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = produitTable.getSelectedRow();
                if (selected >= 0) {
                    try {
                        parent.setContenu(new NouveauProduitPanel(parent, (Integer) tableModel.getValueAt(selected, 0)));
                    } catch (SQLException ex) {
                        Logger.getLogger(ProduitPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun produit selectionné");
                }
            }
        });
        //bouton permettant de supprimer les produits
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = produitTable.getSelectedRow();
                if (selected >= 0) {
                    if (JOptionPane.showConfirmDialog(parent, "Voulez vous vraiment Supprimez?") == JOptionPane.OK_OPTION) {
                        int resultat = modelbd.deleteProduitByCode((Integer) tableModel.getValueAt(selected, 0));
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

        imprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("PrmNomUtil", UserInfo.getNom());
                map.put("PrmPrenomUtil", UserInfo.getPrenom());
                map.put("LOGO", LogoInfo.getLogo());
                map.put("REPORT_CONNECTION", Connexion.getCon());
                JasperPrint jp = print.printSQLReport("ListeProduits.jasper", map, Connexion.getCon());
                JRViewer viewer = new JRViewer(jp);
                viewer.setOpaque(true);
                viewer.setVisible(true);
                JFrame frame = new JFrame();
                frame.setSize(2000, 1000);
                Container c = frame.getContentPane();
                c.add(viewer);
                frame.setVisible(true);
            }
        });

        bas.add(nouveauBtn);
        bas.add(modifierBtn);
        bas.add(supprimerBtn);
        bas.add(imprimerBtn);
        filtrePanel.setLayout(new FlowLayout());
        filtrePanel.add(new JLabel("Nom Produit"));
        filtrePanel.add(nameText);
        nameText.setPreferredSize(new Dimension(100, 25));

        typeProduit = iniTypeProduit();
        filtrePanel.add(new JLabel("Type de Produit"));
        filtrePanel.add(typeProduit);
        typeProduit.setPreferredSize(new Dimension(100, 25));

        filtrePanel.add(new JLabel("Genre de Produit"));
        filtrePanel.add(genre);
        genre.setPreferredSize(new Dimension(100, 25));

        genre.addItem(null);
        genre.addItem(EGenreProduit.Commercialisable);
        genre.addItem(EGenreProduit.NonCommercialisable);

        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Code", "Nom Produit", "Prix Unitaire", "Quantite en Stock", "Type de Produit", "Genre de Produit"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        produitTable = new JTable(tableModel);
        produitTable.removeColumn(produitTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(produitTable));
        add(BorderLayout.CENTER, contenu);
        List<Produit> listProduit = model.findAllProduit();
        for (int i = 0; i < listProduit.size(); i++) {
            tableModel.addRow(new Object[]{
                listProduit.get(i).getCode(),
                listProduit.get(i).getNom(),
                listProduit.get(i).getPrixUnitaire(),
                listProduit.get(i).getQuanteStock(),
                modelType.findTypeProduitByCode(listProduit.get(i).getCodeType()).getLibelle(),
                listProduit.get(i).getGenre()
            });
        }

    }

    private JComboBox<String> iniTypeProduit() {
        JComboBox<String> ComboTypeProduit = new JComboBox<String>();
        ComboTypeProduit.addItem(null);
        
        for(TypeProduit type :modelType.findAllTypeProduit()){
            ComboTypeProduit.addItem(type.getLibelle());
        }
        
        return ComboTypeProduit;
    }
}

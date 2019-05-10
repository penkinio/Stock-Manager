package com.stockmanager.gui.employe;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.Facture;
import com.stockmanager.data.Livraison;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.report.IPrintReportService;
import com.stockmanager.report.impl.PrintReportServiceImpl;
import com.stockmanager.service.IServiceFacture;
import com.stockmanager.service.IServiceLivraison;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.serviceimpl.LogoInfo;
import com.stockmanager.serviceimpl.ServiceFactureImpl;
import com.stockmanager.serviceimpl.ServiceLivraisonImpl;
import com.stockmanager.serviceimpl.ServiceProduitImpl;
import com.stockmanager.serviceimpl.UserInfo;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.Date;
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
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 */
public class LivraisonPanel extends JPanel {

    private JTable livraisonTable;
    private DefaultTableModel tableModel;
    private JTextField factureText;
    private JTextField olText;
    private MainMenuPanel parent;
    private JDateChooser dateLeft;
    private JDateChooser dateRight;
    IServiceLivraison model = new ServiceLivraisonImpl();
    IServiceProduit modelProduit = new ServiceProduitImpl();
    IServiceFacture modelFacture = new ServiceFactureImpl();
    IPrintReportService print = new PrintReportServiceImpl();

    /**
     *
     */
    public LivraisonPanel() {
    }

    /**
     *
     * @param parentFrame
     * @throws SQLException
     */
    public LivraisonPanel(MainMenuPanel parentFrame) throws SQLException {
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

        setLayout(new BorderLayout());
        this.parent = parentFrame;
        JLabel lbl;
        JPanel bas = new JPanel();
        JPanel haut = new JPanel();
        JPanel filtrePanel = new JPanel();
        factureText = new JTextField();
        olText = new JTextField();
        dateLeft = new JDateChooser();
        dateRight = new JDateChooser();
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

        JPanel contenu = new JPanel();
        lbl = new JLabel("LA LISTE DES LIVRAISONS");
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        bas.setLayout(new FlowLayout());
        //Bouton permettant de filtrer les informations sur les livraisons
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String facture = factureText.getText();
                String ol = olText.getText();
                Date dateL = dateLeft.getDate();
                Date dateR = dateRight.getDate();
                tableModel.setRowCount(0);
                List<Livraison> listLivraison = model.findLivraisonByCritere(facture, ol, dateL, dateR);
                //Affichage du tableau contenant la liste des livraisons
                for (int i = 0; i < listLivraison.size(); i++) {
                    tableModel.addRow(new Object[]{
                        listLivraison.get(i).getCode(),
                        listLivraison.get(i).getOrdreLivraision(),
                        listLivraison.get(i).getDescription(),
                        modelFacture.findFactureByCode(listLivraison.get(i).getCodeFacture()).getNumeroFacture(),
                        listLivraison.get(i).getEtat()
                    });
                }
            }
        });
        //bouton permetant l'affichage d'une petite fenetre pour l'insertion d'un nouveau livraison
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauLivraisonPanel(parent));
            }
        });
        //bouton permettant l'affichage la fenetre de modification
        modifierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = livraisonTable.getSelectedRow();
                if (selected >= 0) {
                    try {
                        parent.setContenu(new NouveauLivraisonPanel(parent, (String) tableModel.getValueAt(selected, 1)));
                    } catch (SQLException ex) {
                        Logger.getLogger(LivraisonPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun livraison n'est selectionné");
                }
            }
        });
        //bouton permettant de supprimer les livraisons
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = livraisonTable.getSelectedRow();
                if (selected >= 0) {
                    if (JOptionPane.showConfirmDialog(parent, "Voulez vous vraiment Supprimez?") == JOptionPane.OK_OPTION) {
                        int resultat = model.deleteLivraisonByCode((String) tableModel.getValueAt(selected, 1));
                        switch (resultat) {
                            case 1:
                                tableModel.removeRow(selected);
                                break;
                            case 2:
                                JOptionPane.showMessageDialog(null, "Impossible de supprimer ce client");
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "Impossible de fermer la ressource");
                                break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucune donnée n'est selectionnée");
                }
            }
        });

        imprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = livraisonTable.getSelectedRow();
                if (selected >= 0) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("PrmLivraison", (String) tableModel.getValueAt(selected, 1));
                    Livraison livraison = model.getLivraisonByOrder((String) tableModel.getValueAt(selected, 1));
                    Facture facture = modelFacture.findFactureByCode(livraison.getCodeFacture());
                    map.put("PrmNomClient", facture.getNomClient());
                    map.put("PrmNomUtil", UserInfo.getNom());
                    map.put("PrmPrenomUtil", UserInfo.getPrenom());
                    map.put("LOGO", LogoInfo.getLogo());
                    map.put("REPORT_CONNECTION", Connexion.getCon());
                    JasperPrint jp = print.printSQLReport("livraison.jasper", map, Connexion.getCon());
                    JRViewer viewer = new JRViewer(jp);
                    viewer.setOpaque(true);
                    viewer.setVisible(true);
                    JFrame frame = new JFrame();
                    frame.setSize(2000, 1000);
                    Container c = frame.getContentPane();
                    c.add(viewer);
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun vente n'est selectionné");
                }
            }
        });

        bas.add(nouveauBtn);
        bas.add(modifierBtn);
        bas.add(supprimerBtn);
        bas.add(imprimerBtn);
        filtrePanel.setLayout(new FlowLayout());
        filtrePanel.add(new JLabel("Facture :"));
        filtrePanel.add(factureText);
        filtrePanel.add(new JLabel("Ordre de Livraison :"));
        filtrePanel.add(olText);
        filtrePanel.add(new JLabel("De :"));
        filtrePanel.add(dateLeft);
        filtrePanel.add(new JLabel("A :"));
        filtrePanel.add(dateRight);
        olText.setPreferredSize(new Dimension(100, 25));
        factureText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Code", "Ordre de Livraison", "Description", "Facture", "Etat de Livraison"}, 0);
        livraisonTable = new JTable(tableModel);
        livraisonTable.removeColumn(livraisonTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(livraisonTable));
        add(BorderLayout.CENTER, contenu);
        List<Livraison> listLivraison = model.findAllLivraison();
        for (int i = 0; i < listLivraison.size(); i++) {
            tableModel.addRow(new Object[]{
                listLivraison.get(i).getCode(),
                listLivraison.get(i).getOrdreLivraision(),
                listLivraison.get(i).getDescription(),
                modelFacture.findFactureByCode(listLivraison.get(i).getCodeFacture()).getNumeroFacture(),
                listLivraison.get(i).getEtat()
            });
        }
    }
}

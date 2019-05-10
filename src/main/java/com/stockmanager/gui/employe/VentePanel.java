package com.stockmanager.gui.employe;

import com.stockmanager.connection.Connexion;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.data.Vente;
import com.stockmanager.report.IPrintReportService;
import com.stockmanager.report.impl.PrintReportServiceImpl;
import com.stockmanager.service.IServiceClient;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.service.IServiceVente;
import com.stockmanager.serviceimpl.LogoInfo;
import com.stockmanager.serviceimpl.ServiceClientImpl;
import com.stockmanager.serviceimpl.ServiceProduitImpl;
import com.stockmanager.serviceimpl.ServiceVenteImpl;
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
import java.text.DecimalFormat;
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
 * classe affichant le panel comportant les informations sur les ventes
 */
public class VentePanel extends JPanel {

    private JTable venteTable;
    private DefaultTableModel tableModel;
    private JTextField nameProduitText;
    private JTextField nameClientText;
    private JTextField referenceText;
    private JDateChooser dateLeft;
    private JDateChooser dateRight;
    private MainMenuPanel parent;
    IServiceVente model = new ServiceVenteImpl();
    IServiceClient modelClient = new ServiceClientImpl();
    IServiceProduit modelProduit = new ServiceProduitImpl();
    IPrintReportService print = new PrintReportServiceImpl();
    DecimalFormat df = new DecimalFormat("#,##0");

    /**
     *
     */
    public VentePanel() {
    }

    /**
     *
     * @param parentFrame
     * @throws SQLException
     */
    public VentePanel(MainMenuPanel parentFrame) throws SQLException {
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
        nameProduitText = new JTextField();
        nameClientText = new JTextField();
        referenceText = new JTextField();
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
        lbl = new JLabel("LA LISTE DES VENTES");
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        bas.setLayout(new FlowLayout());
        //Bouton permettant de filtrer les informations sur les ventes
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String nomProduit = nameProduitText.getText();
                String nomClient = nameClientText.getText();
                String reference = referenceText.getText();
                Date dateL = dateLeft.getDate();
                Date dateR = dateRight.getDate();
                tableModel.setRowCount(0);

                List<Vente> listVente = model.findVenteByCritere(nomProduit, nomClient, reference, dateL, dateR);
                //Affichage du tableau contenant la liste des ventes
                for (int i = 0; i < listVente.size(); i++) {
                    try {
                        tableModel.addRow(new Object[]{
                            modelProduit.findProduitByCode(listVente.get(i).getCodeProduit()).getNom(),
                            modelClient.findClientByCode(listVente.get(i).getCodeClient()).getNom(),
                            listVente.get(i).getReference(),
                            df.format(listVente.get(i).getPrixUnitaireVente()),
                            listVente.get(i).getQuantite(),
                            df.format(listVente.get(i).getPrixUnitaireVente() * listVente.get(i).getQuantite()),
                            listVente.get(i).getDate()
                        });
                    } catch (SQLException ex) {
                        Logger.getLogger(VentePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        //bouton permetant l'affichage d'une petite fenetre pour l'insertion d'un nouveau vente
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauVentePanel(parent));
            }
        });
        //bouton permettant l'affichage la fenetre de modification
        modifierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = venteTable.getSelectedRow();
                if (selected >= 0) {
                    parent.setContenu(new NouveauVentePanel(parent, (String) tableModel.getValueAt(selected, 2)));
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun achat n'est selectionné");
                }
            }
        });
        //bouton permettant de supprimer les ventes
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = venteTable.getSelectedRow();
                if (selected >= 0) {
                    if (JOptionPane.showConfirmDialog(parent, "Voulez vous vraiment Supprimez?") == JOptionPane.OK_OPTION) {
                        if (model.deleteVente((String) tableModel.getValueAt(selected, 2)) > 0) {
                            try {
                                modelProduit.addQuantiteProduit(modelProduit.getProduitByName((String) tableModel.getValueAt(selected, 0)).getCode(), (Integer) tableModel.getValueAt(selected, 4));
                                tableModel.removeRow(selected);
                            } catch (SQLException ex) {
                                Logger.getLogger(AchatPanel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du vente " + tableModel.getValueAt(selected, 1));
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucune donnée n'est selectionnée");
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
                JasperPrint jp = print.printSQLReport("ListeVente.jasper", map, Connexion.getCon());
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
        filtrePanel.add(new JLabel("Produit"));
        filtrePanel.add(nameProduitText);
        filtrePanel.add(new JLabel("Client"));
        filtrePanel.add(nameClientText);
        filtrePanel.add(new JLabel("Reference"));
        filtrePanel.add(referenceText);
        filtrePanel.add(new JLabel("De :"));
        filtrePanel.add(dateLeft);
        filtrePanel.add(new JLabel("A :"));
        filtrePanel.add(dateRight);
        nameProduitText.setPreferredSize(new Dimension(100, 25));
        nameClientText.setPreferredSize(new Dimension(100, 25));
        referenceText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Produit", "Client", "Reference", "Prix Unitaire", "Quantite", "Prix Total", "Date"}, 0);
        venteTable = new JTable(tableModel);
        //venteTable.removeColumn(venteTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(venteTable));
        add(BorderLayout.CENTER, contenu);
        List<Vente> listVente = model.findAllVente();
        for (int i = 0; i < listVente.size(); i++) {
            tableModel.addRow(new Object[]{
                modelProduit.findProduitByCode(listVente.get(i).getCodeProduit()).getNom(),
                modelClient.findClientByCode(listVente.get(i).getCodeClient()).getNom(),
                listVente.get(i).getReference(),
                df.format(listVente.get(i).getPrixUnitaireVente()),
                listVente.get(i).getQuantite(),
                df.format(listVente.get(i).getPrixUnitaireVente() * listVente.get(i).getQuantite()),
                listVente.get(i).getDate()
            });
        }
    }
}

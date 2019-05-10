package com.stockmanager.gui.employe;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.Facture;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.report.IPrintReportService;
import com.stockmanager.report.impl.PrintReportServiceImpl;
import com.stockmanager.service.IServiceFacture;
import com.stockmanager.serviceimpl.LogoInfo;
import com.stockmanager.serviceimpl.ServiceFactureImpl;
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
import java.text.DecimalFormat;
import java.util.HashMap;
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
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * classe affichant le panel comportant les informations sur les ventes
 */
public class FacturePanel extends JPanel {

    private JTable venteTable;
    private DefaultTableModel tableModel;
    private JTextField nameClientText;
    private JTextField numFactureText;
    private MainMenuPanel parent;
    IServiceFacture model = new ServiceFactureImpl();
    IPrintReportService print = new PrintReportServiceImpl();
    DecimalFormat df = new DecimalFormat("#,##0");

    /**
     *
     */
    public FacturePanel() {
    }

    /**
     *
     * @param parentFrame
     * @throws SQLException
     */
    public FacturePanel(MainMenuPanel parentFrame) throws SQLException {
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
        nameClientText = new JTextField();
        numFactureText = new JTextField();
        nouveauBtn = new JButton("Nouveau");
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
        lbl = new JLabel("LA LISTE DES FACTURES");
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        bas.setLayout(new FlowLayout());
        //Bouton permettant de filtrer les informations sur les ventes
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String nomClient = nameClientText.getText();
                String numFacture = numFactureText.getText();
                tableModel.setRowCount(0);

                List<Facture> listFactures = model.findFactureByCritere(nomClient, numFacture);
                //Affichage du tableau contenant la liste des ventes
                for (int i = 0; i < listFactures.size(); i++) {
                    tableModel.addRow(new Object[]{
                        listFactures.get(i).getCode(),
                        listFactures.get(i).getNomClient(),
                        listFactures.get(i).getNumeroFacture(),
                        df.format(model.getMontantFacture(listFactures.get(i).getNumeroFacture())),
                        listFactures.get(i).getDate()
                    });
                }
            }
        });
        //bouton permetant l'affichage d'une petite fenetre pour l'insertion d'un nouveau vente
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauFacturePanel(parent));
            }
        });

        imprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = venteTable.getSelectedRow();
                if (selected >= 0) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("PrmFacture", (String) tableModel.getValueAt(selected, 2));
                    Facture facture = model.findFactureByCode((Integer) tableModel.getValueAt(selected, 0));
                    map.put("PrmTva", facture.isApplicTva());
                    map.put("PrmNomUtil", UserInfo.getNom());
                    map.put("PrmPrenomUtil", UserInfo.getPrenom());
                    map.put("LOGO", LogoInfo.getLogo());
                    map.put("REPORT_CONNECTION", Connexion.getCon());
                    JasperPrint jp = print.printSQLReport("Facture.jasper", map, Connexion.getCon());
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
        //bouton permettant l'affichage la fenetre de modification
        modifierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = venteTable.getSelectedRow();
                if (selected >= 0) {
                    try {
                        parent.setContenu(new NouveauFacturePanel(parent, (Integer) tableModel.getValueAt(selected, 0)));
                    } catch (SQLException ex) {
                        Logger.getLogger(FacturePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun vente n'est selectionné");
                }
            }
        });
        //bouton permettant de supprimer les ventes
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = venteTable.getSelectedRow();
                if (selected >= 0) {
                    if (JOptionPane.showConfirmDialog(parent, "Voulez vous vraiment Supprimez?") == JOptionPane.OK_OPTION) {
                        if (model.deleteFacture((String) tableModel.getValueAt(selected, 2)) > 0) {
                            tableModel.removeRow(selected);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du vente " + tableModel.getValueAt(selected, 1));
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucune donnée n'est selectionnée");
                }
            }
        });
        bas.add(nouveauBtn);
        bas.add(modifierBtn);
        bas.add(supprimerBtn);
        bas.add(imprimerBtn);
        filtrePanel.setLayout(new FlowLayout());
        filtrePanel.add(new JLabel("Client"));
        filtrePanel.add(nameClientText);
        filtrePanel.add(new JLabel("Reference"));
        filtrePanel.add(numFactureText);
        nameClientText.setPreferredSize(new Dimension(100, 25));
        numFactureText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Code", "Client", "Numero de la Facture", "Montant de la Facture", "Date", "Etat de Facture"}, 0);
        venteTable = new JTable(tableModel);
        venteTable.removeColumn(venteTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(venteTable));
        add(BorderLayout.CENTER, contenu);
        List<Facture> listFactures = model.findAllFacture();
        for (int i = 0; i < listFactures.size(); i++) {
            tableModel.addRow(new Object[]{
                listFactures.get(i).getCode(),
                listFactures.get(i).getNomClient(),
                listFactures.get(i).getNumeroFacture(),
                df.format(model.getMontantFacture(listFactures.get(i).getNumeroFacture())),
                listFactures.get(i).getDate(),
                listFactures.get(i).getEtat()
            });
        }
    }
}

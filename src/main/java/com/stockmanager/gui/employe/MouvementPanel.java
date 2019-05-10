package com.stockmanager.gui.employe;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.MouvementStock;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.report.IPrintReportService;
import com.stockmanager.report.impl.PrintReportServiceImpl;
import com.stockmanager.service.IServiceMouvement;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.serviceimpl.LogoInfo;
import com.stockmanager.serviceimpl.ServiceMouvementImpl;
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
public class MouvementPanel extends JPanel {

    private JTable mouvementTable;
    private DefaultTableModel tableModel;
    private JTextField descriptionText;
    private MainMenuPanel parent;
    private JDateChooser dateLeft;
    private JDateChooser dateRight;
    IServiceMouvement model = new ServiceMouvementImpl();
    IServiceProduit modelProduit = new ServiceProduitImpl();
    IPrintReportService print = new PrintReportServiceImpl();

    /**
     *
     */
    public MouvementPanel() {
    }

    /**
     *
     * @param parentFrame
     * @throws SQLException
     */
    public MouvementPanel(MainMenuPanel parentFrame) throws SQLException {
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
        descriptionText = new JTextField();
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
        lbl = new JLabel("LA LISTE DES MOUVEMENTS DES STOCKS");
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        bas.setLayout(new FlowLayout());
        //Bouton permettant de filtrer les informations sur les mouvements
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String description = descriptionText.getText();
                Date dateL = dateLeft.getDate();
                Date dateR = dateRight.getDate();
                tableModel.setRowCount(0);
                List<MouvementStock> listMouvement = model.findMouvementByCritere(description, dateL, dateR);
                //Affichage du tableau contenant la liste des mouvements
                for (int i = 0; i < listMouvement.size(); i++) {
                    try {
                        tableModel.addRow(new Object[]{
                            listMouvement.get(i).getCode(),
                            listMouvement.get(i).getDescription(),
                            listMouvement.get(i).getDateMouvement(),
                            modelProduit.findProduitByCode(listMouvement.get(i).getCodeProduit()).getNom(),
                            listMouvement.get(i).getQuantiteProduit(),
                            listMouvement.get(i).getSens()
                        });
                    } catch (SQLException ex) {
                        Logger.getLogger(MouvementPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        //bouton permetant l'affichage d'une petite fenetre pour l'insertion d'un nouveau mouvement
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauMouvementPanel(parent));
            }
        });
        //bouton permettant l'affichage la fenetre de modification
        modifierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = mouvementTable.getSelectedRow();
                if (selected >= 0) {
                    try {
                        parent.setContenu(new NouveauMouvementPanel(parent, (Integer) tableModel.getValueAt(selected, 0)));
                    } catch (SQLException ex) {
                        Logger.getLogger(MouvementPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun mouvement n'est selectionné");
                }
            }
        });
        //bouton permettant de supprimer les mouvements
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = mouvementTable.getSelectedRow();
                if (selected >= 0) {
                    if (JOptionPane.showConfirmDialog(parent, "Voulez vous vraiment Supprimez?") == JOptionPane.OK_OPTION) {
                        if (model.deleteMouvement((Integer) tableModel.getValueAt(selected, 0)) > 0) {
                            tableModel.removeRow(selected);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du mouvement " + tableModel.getValueAt(selected, 1));
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
                JasperPrint jp = print.printSQLReport("ListeMouvements.jasper", map, Connexion.getCon());
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
        filtrePanel.add(new JLabel("Description :"));
        filtrePanel.add(descriptionText);
        filtrePanel.add(new JLabel("De :"));
        filtrePanel.add(dateLeft);
        filtrePanel.add(new JLabel("A :"));
        filtrePanel.add(dateRight);
        descriptionText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Code", "Description", "Date Mouvement", "Produit", "Quantite", "Sens du Mouvement"}, 0);
        mouvementTable = new JTable(tableModel);
        mouvementTable.removeColumn(mouvementTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(mouvementTable));
        add(BorderLayout.CENTER, contenu);
        List<MouvementStock> listMouvement = model.findAllMouvement();
        for (int i = 0; i < listMouvement.size(); i++) {
            try {
                tableModel.addRow(new Object[]{
                    listMouvement.get(i).getCode(),
                    listMouvement.get(i).getDescription(),
                    listMouvement.get(i).getDateMouvement(),
                    modelProduit.findProduitByCode(listMouvement.get(i).getCodeProduit()).getNom(),
                    listMouvement.get(i).getQuantiteProduit(),
                    listMouvement.get(i).getSens()
                });
            } catch (SQLException ex) {
                Logger.getLogger(MouvementPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

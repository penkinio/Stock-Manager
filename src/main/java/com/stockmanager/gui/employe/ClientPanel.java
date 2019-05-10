package com.stockmanager.gui.employe;

import com.stockmanager.connection.Connexion;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.data.Client;
import com.stockmanager.report.IPrintReportService;
import com.stockmanager.report.impl.PrintReportServiceImpl;
import com.stockmanager.service.IServiceClient;
import com.stockmanager.serviceimpl.LogoInfo;
import com.stockmanager.serviceimpl.ServiceClientImpl;
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
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * classe affichant le panel comportant les informations sur les clients
 */
public class ClientPanel extends JPanel {

    private JTable clientTable;
    private DefaultTableModel tableModel;
    private JTextField nameText;
    private MainMenuPanel parent;
    IServiceClient model = new ServiceClientImpl();
    IPrintReportService print = new PrintReportServiceImpl();

    /**
     *
     */
    public ClientPanel() {
    }

    /**
     *
     * @param parentFrame
     * @throws SQLException
     */
    public ClientPanel(MainMenuPanel parentFrame) throws SQLException {
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
        nameText = new JTextField();
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
        lbl = new JLabel("LA LISTE DES CLIENTS");
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        bas.setLayout(new FlowLayout());
        //Bouton permettant de filtrer les informations sur les clients
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String name = nameText.getText();
                tableModel.setRowCount(0);
                List<Client> listClient = model.findClientByName(name);
                //Affichage du tableau contenant la liste des clients
                for (int i = 0; i < listClient.size(); i++) {
                    tableModel.addRow(new Object[]{
                        listClient.get(i).getCode(),
                        listClient.get(i).getNom(),
                        listClient.get(i).getContact(),
                        listClient.get(i).getVille(),
                        listClient.get(i).getLocalisation(),
                        listClient.get(i).getPays(),
                        listClient.get(i).getEmail(),
                        listClient.get(i).getBoitePostal(),
                        listClient.get(i).getSiteWeb()
                    });
                }
            }
        });
        //bouton permetant l'affichage d'une petite fenetre pour l'insertion d'un nouveau client
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauClientPanel(parent));
            }
        });
        //bouton permettant l'affichage la fenetre de modification
        modifierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = clientTable.getSelectedRow();
                if (selected >= 0) {
                    try {
                        parent.setContenu(new NouveauClientPanel(parent, (Integer) tableModel.getValueAt(selected, 0)));
                    } catch (SQLException ex) {
                        Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun client n'est selectionné");
                }
            }
        });
        //bouton permettant de supprimer les clients
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = clientTable.getSelectedRow();
                if (selected >= 0) {
                    if (JOptionPane.showConfirmDialog(parent, "Voulez vous vraiment Supprimez?") == JOptionPane.OK_OPTION) {
                        int resultat = model.deleteClientByCode((Integer) tableModel.getValueAt(selected, 0));
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
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("PrmNomUtil", UserInfo.getNom());
                map.put("PrmPrenomUtil", UserInfo.getPrenom());
                map.put("LOGO", LogoInfo.getLogo());
                map.put("REPORT_CONNECTION", Connexion.getCon());
                JasperPrint jp = print.printSQLReport("ListeClients.jasper", map, Connexion.getCon());
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
        filtrePanel.add(new JLabel("Nom"));
        filtrePanel.add(nameText);
        nameText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Code", "Nom", "Contact", "Ville", "Localisation", "Pays", "Email", "Boite Postale", "Site Web"}, 0);
        clientTable = new JTable(tableModel);
        clientTable.removeColumn(clientTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(clientTable));
        add(BorderLayout.CENTER, contenu);
        List<Client> listClient = model.findAllClient();
        for (int i = 0; i < listClient.size(); i++) {
            tableModel.addRow(new Object[]{
                listClient.get(i).getCode(),
                listClient.get(i).getNom(),
                listClient.get(i).getContact(),
                listClient.get(i).getVille(),
                listClient.get(i).getLocalisation(),
                listClient.get(i).getPays(),
                listClient.get(i).getEmail(),
                listClient.get(i).getBoitePostal(),
                listClient.get(i).getSiteWeb()
            });
        }
    }
}

package com.stockmanager.gui.admin;

import com.stockmanager.connection.Connexion;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.data.enumeration.RoleType;
import com.stockmanager.data.User;
import com.stockmanager.report.IPrintReportService;
import com.stockmanager.report.impl.PrintReportServiceImpl;
import com.stockmanager.service.IServiceUser;
import com.stockmanager.serviceimpl.LogoInfo;
import com.stockmanager.serviceimpl.ServiceUserImpl;
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
 *
 * classe permettant l'affichage des utlisateurs et efffectuer les traitements
 * sur ces utilisateurs
 */
public class UtilisateurPanel extends JPanel {

    private JTextField nomText;
    private JComboBox<RoleType> role;
    private JTable utilisateurTable;
    private DefaultTableModel tableModel;
    private MainMenuPanel parent;
    IServiceUser model = new ServiceUserImpl();
    IPrintReportService print = new PrintReportServiceImpl();
    File iconCreate;
    File iconInit;
    File iconDelete;
    File iconSearch;
    File iconPrint;

    public UtilisateurPanel() {
    }

    public UtilisateurPanel(MainMenuPanel parentFrame) throws SQLException {
        JButton nouveauBtn;
        JButton supprimerBtn;
        JButton initialiserPasswdBtn;
        JButton filtreBtn;
        JButton imprimerBtn;

        setLayout(new BorderLayout());
        this.parent = parentFrame;
        JLabel lbl = new JLabel("LA LISTE DES UTILISATEURS DE STOCK-MANAGER");
        JPanel haut = new JPanel();
        JPanel contenu = new JPanel();
        JPanel bas = new JPanel();
        JPanel filtrePanel = new JPanel();
        nouveauBtn = new JButton("Ajouter");
        supprimerBtn = new JButton("Supprimer");
        initialiserPasswdBtn = new JButton("Reinitialiser Mot de Passe");
        imprimerBtn = new JButton("Imprimer");
        filtreBtn = new JButton("Filtrer");
        
        iconCreate = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Create.png");
        iconInit = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Application.png");
        iconDelete = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Delete.png");
        iconSearch = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Search.png");
        iconPrint = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Print.png");

        nouveauBtn.setIcon(new ImageIcon(iconCreate.getPath()));
        supprimerBtn.setIcon(new ImageIcon(iconDelete.getPath()));
        initialiserPasswdBtn.setIcon(new ImageIcon(iconInit.getPath()));
        imprimerBtn.setIcon(new ImageIcon(iconPrint.getPath()));
        filtreBtn.setIcon(new ImageIcon(iconSearch.getPath()));
        
        nomText = new JTextField();
        role = new JComboBox<RoleType>();
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        bas.setLayout(new FlowLayout());
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String nom = nomText.getText();
                RoleType roleN = (RoleType) role.getSelectedItem();
                StringBuilder query = new StringBuilder("select * from utilisateur where status = ? ");
                if ((nom != null) && !("".equals(nom))) {
                    query.append("and nom like '%");
                    query.append(nom);
                    query.append("%'");
                }
                if (roleN != null) {
                    query.append("and role = ");
                    query.append(roleN.ordinal());
                }
                tableModel.setRowCount(0);
                try {
                    System.out.println(query.toString());
                    List<User> listUsers = model.findAllUserSimple(query.toString());
                    for (int i = 0; i < listUsers.size(); i++) {
                        tableModel.addRow(new Object[]{
                            listUsers.get(i).getCode(),
                            listUsers.get(i).getUserName(),
                            listUsers.get(i).getRole(),
                            listUsers.get(i).getNom(),
                            listUsers.get(i).getPrenom()});

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(UtilisateurPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauUtilisateurPanel(parent));
            }
        });
        initialiserPasswdBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = utilisateurTable.getSelectedRow();
                if (selected >= 0) {
                    int res = model.reinitialiserPassWord((Integer) tableModel.getValueAt(selected, 0));
                    if (res == 1) {
                        JOptionPane.showMessageDialog(null, "Le mot de passe est reinitialisé à 'admin'");
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de reinitialiser le mot de passe");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun utilisateur n'est selectionné");
                }
            }
        });
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = utilisateurTable.getSelectedRow();
                if (selected >= 0) {
                    if (model.deleteUserByCode((Integer) tableModel.getValueAt(selected, 0)) > 0) {
                        tableModel.removeRow(selected);
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de supprimer cet utilisateur");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun utilisateur n'est selectionné");
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
                JasperPrint jp = print.printSQLReport("ListeUsers.jasper", map, Connexion.getCon());
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
        bas.add(initialiserPasswdBtn);
        bas.add(supprimerBtn);
        bas.add(imprimerBtn);
        filtrePanel.setLayout(new FlowLayout());
        filtrePanel.add(new JLabel("Nom"));
        filtrePanel.add(nomText);
        nomText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(new JLabel("Role"));
        filtrePanel.add(role);
        role.setPreferredSize(new Dimension(100, 25));
        role.addItem(null);
        role.addItem(RoleType.Employee);
        role.addItem(RoleType.Administrateur);
        role.addItem(RoleType.Controleur);
        role.addItem(RoleType.AdministrateurPrincipal);
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Code", "Login", "Role", "Nom", "Prenom"}, 0);
        utilisateurTable = new JTable(tableModel);
        utilisateurTable.removeColumn(utilisateurTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(utilisateurTable));
        add(BorderLayout.CENTER, contenu);
        String requete = "select * from utilisateur where status = ?";
        List<User> listUsers = model.findAllUserSimple(requete);
        for (int i = 0; i < listUsers.size(); i++) {
            tableModel.addRow(new Object[]{
                listUsers.get(i).getCode(),
                listUsers.get(i).getUserName(),
                listUsers.get(i).getRole(),
                listUsers.get(i).getNom(),
                listUsers.get(i).getPrenom()
            });
        }
    }
}

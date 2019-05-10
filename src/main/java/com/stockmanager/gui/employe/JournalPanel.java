package com.stockmanager.gui.employe;

import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.data.Journal;
import com.stockmanager.service.IServiceJournal;
import com.stockmanager.serviceimpl.ServiceJournalImpl;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * classe affichant le panel comportant les informations sur les ventes
 */
public class JournalPanel extends JPanel {

    private JTable venteTable;
    private DefaultTableModel tableModel;
    private JTextField userText;
    private JTextField operationText;
    private JDateChooser dateLeft;
    private JDateChooser dateRight;
    private MainMenuPanel parent;
    IServiceJournal model = new ServiceJournalImpl();

    /**
     *
     */
    public JournalPanel() {
    }

    /**
     *
     * @param parentFrame
     * @throws SQLException
     */
    public JournalPanel(MainMenuPanel parentFrame) throws SQLException {
        JButton filtreBtn;
        File iconSearch;

        setLayout(new BorderLayout());
        this.parent = parentFrame;
        JLabel lbl;
        JPanel bas = new JPanel();
        JPanel haut = new JPanel();
        JPanel filtrePanel = new JPanel();
        userText = new JTextField();
        operationText = new JTextField();
        dateLeft = new JDateChooser();
        dateRight = new JDateChooser();
        
        filtreBtn = new JButton("Filtrer");
        iconSearch = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Search.png");
        filtreBtn.setIcon(new ImageIcon(iconSearch.getPath()));
        
        JPanel contenu = new JPanel();
        lbl = new JLabel("JOURNALISATION DU SYSTEME");
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        bas.setLayout(new FlowLayout());
        //Bouton permettant de filtrer les informations sur les ventes
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    String user = userText.getText();
                    String operation = operationText.getText();
                    Date dateL = dateLeft.getDate();
                    Date dateR = dateRight.getDate();
                    tableModel.setRowCount(0);

                    List<Journal> listJournal = model.findAllByCritere(user, operation, dateL, dateR);
                    //Affichage du tableau contenant la liste des ventes
                    for (int i = 0; i < listJournal.size(); i++) {
                        tableModel.addRow(new Object[]{
                            listJournal.get(i).getUser(),
                            listJournal.get(i).getOperation(),
                            listJournal.get(i).getDateOperation(),
                            listJournal.get(i).getDescription()
                        });
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JournalPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        filtrePanel.setLayout(new FlowLayout());
        filtrePanel.add(new JLabel("Utilisateur"));
        filtrePanel.add(userText);
        filtrePanel.add(new JLabel("Operation"));
        filtrePanel.add(operationText);
        filtrePanel.add(new JLabel("De :"));
        filtrePanel.add(dateLeft);
        filtrePanel.add(new JLabel("A :"));
        filtrePanel.add(dateRight);
        userText.setPreferredSize(new Dimension(100, 25));
        operationText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Operation", "Utilisateur", "Date de l'operation","Description"}, 0);
        venteTable = new JTable(tableModel);
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");
        //venteTable.removeColumn(venteTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(venteTable));
        add(BorderLayout.CENTER, contenu);
        List<Journal> listJournal = model.findAllJournal();
        for (int i = 0; i < listJournal.size(); i++) {
            tableModel.addRow(new Object[]{
                listJournal.get(i).getOperation(),
                listJournal.get(i).getUser(),
                listJournal.get(i).getDateOperation(),
                listJournal.get(i).getDescription()
            });
        }
    }
}

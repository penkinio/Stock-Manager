package com.stockmanager.gui.common;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.stockmanager.gui.MainMenuPanel;
import com.stockmanager.service.IServiceUser;
import com.stockmanager.serviceimpl.ServiceUserImpl;
import com.stockmanager.serviceimpl.UserInfo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 *
 * classe affichant le panel d'authentification
 */
public class ChangePassWordPanel extends JPanel {

    private JPasswordField oldPasswordText;
    private JPasswordField newPasswdText;
    private JPasswordField confirmPasswdText;
    private JButton btnChange;
    private MainMenuPanel parent;
    IServiceUser model = new ServiceUserImpl();
    File iconChange;

    public ChangePassWordPanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(80, 350, 80, 300));
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl;
        haut.add(lbl = new JLabel("<html>Changer votre mot de Passe <br/> pour profiter des services de STOCK MANAGER"));
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        JButton btnChange = new JButton("Login");
        
        iconChange = new File(System.getProperty("user.dir") + "/src/main/java/com/stockmanager/images/Move.png");
        btnChange.setIcon(new ImageIcon(iconChange.getPath()));
        
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Ancien mot de Passe", oldPasswordText = new JPasswordField());
        builder.append("Nouveau mot de Passe", newPasswdText = new JPasswordField());
        builder.append("Confimer mot de Passe", confirmPasswdText = new JPasswordField());
        builder.append(btnChange);
        add(BorderLayout.CENTER, builder.getPanel());
        btnChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String username = UserInfo.getUsername();
                String oldPasswd = new String(oldPasswordText.getPassword());
                String newPasswd = new String(newPasswdText.getPassword());
                String confirmPasswd = new String(confirmPasswdText.getPassword());
                if ((username == null) || ("".equals(username))) {
                    JOptionPane.showMessageDialog(null, "Le login est obligatoire");
                    oldPasswordText.setText("");
                    newPasswdText.setText("");
                    confirmPasswdText.setText("");
                    return;
                }
                if ("".equals(oldPasswd)) {
                    JOptionPane.showMessageDialog(null, "Le mot de passe est obligatoire");
                    oldPasswordText.setText("");
                    newPasswdText.setText("");
                    confirmPasswdText.setText("");
                    return;
                }
                if ("".equals(newPasswd)) {
                    JOptionPane.showMessageDialog(null, "Le mot de passe est obligatoire");
                    oldPasswordText.setText("");
                    newPasswdText.setText("");
                    confirmPasswdText.setText("");
                    return;
                }
                if ("".equals(confirmPasswd)) {
                    JOptionPane.showMessageDialog(null, "Le mot de passe est obligatoire");
                    oldPasswordText.setText("");
                    newPasswdText.setText("");
                    confirmPasswdText.setText("");
                    return;
                }
                int statutAth = model.authentification(username, oldPasswd);
                switch (statutAth) {
                    case 1:
                        model.changePassword(username, newPasswd, confirmPasswd);
                        JOptionPane.showMessageDialog(null, "Modification du mot de passe effectue avec succes !!");
                        parent.setContenu(new JPanel());
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "Login ou mot de passe incorrect");
                        oldPasswordText.setText("");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Impossible de vérifier vos coordonnées");
                        oldPasswordText.setText("");
                        break;
                }
            }
        });
    }
}

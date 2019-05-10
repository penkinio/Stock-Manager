package com.stockmanager.gui;

import com.stockmanager.serviceimpl.LogoInfo;
import com.stockmanager.serviceimpl.UserInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXHyperlink;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * classe se chargant de l'affichage de l'enttete de l'application
 */
public abstract class HeaderPanel extends JPanel {

    private JXHyperlink deconnexion;
    private JLabel username;
    private JPanel logoPanel;
    private JPanel detailPanel;

    public HeaderPanel() {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        setBackground(new Color(255, 255, 255));
        //JPanel pan = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        //pan.setBorder(new EmptyBorder(0, 0, 0, 10));
        setPreferredSize(new Dimension(800, 100));
        username = new JLabel("Bonjour,");
        deconnexion = new JXHyperlink();
        deconnexion.setUnclickedColor(Color.blue);
        deconnexion.setClickedColor(Color.blue);
        deconnexion.setText("Deconnexion");
        deconnexion.setVisible(false);
        username.setVisible(false);
        add(username);
        add(deconnexion);

        deconnexion.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                deconnexion();
                setEnabledHeader(false);
            }
        });
    }

    /**
     * methode abstraite effectuant la deconnexion de l'utilisateur sur le
     * systeme
     */
    public abstract void deconnexion();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font font = new Font("Serif", Font.PLAIN, 32);
        GradientPaint gp = new GradientPaint(0f, 0f, Color.blue, 0f, 30f, Color.blue);
        ((Graphics2D) g).setPaint(gp);
        g.setFont(font);
        g.setColor(Color.red);
        g.drawString("SOKAH STOCK MANAGER", 310, 80);
    }

    /**
     * methode permettant l'activation de l'entete de l'application
     *
     * @param status
     */
    public void setEnabledHeader(boolean status) {
        username.setVisible(status);
        deconnexion.setVisible(status);
        if (status) {
            username.setText("Bonjour " + UserInfo.getPrenom() + " " + UserInfo.getNom() + ", ");
        }
    }

}

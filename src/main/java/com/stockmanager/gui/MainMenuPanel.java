package com.stockmanager.gui;

import com.stockmanager.data.enumeration.RoleType;
import com.stockmanager.gui.employe.ClientPanel;
import com.stockmanager.gui.employe.FournisseurPanel;
import com.stockmanager.gui.admin.UtilisateurPanel;
import com.stockmanager.gui.common.ChangePassWordPanel;
import com.stockmanager.gui.employe.AchatPanel;
import com.stockmanager.gui.employe.CategoriePanel;
import com.stockmanager.gui.employe.FacturePanel;
import com.stockmanager.gui.employe.JournalPanel;
import com.stockmanager.gui.employe.LivraisonPanel;
import com.stockmanager.gui.employe.MouvementPanel;
import com.stockmanager.gui.employe.ParamClientPanel;
import com.stockmanager.gui.employe.ParamProduitPanel;
import com.stockmanager.gui.employe.ParamUserPanel;
import com.stockmanager.gui.employe.ProduitPanel;
import com.stockmanager.gui.employe.TypeProduitPanel;
import com.stockmanager.gui.employe.VenteSpecialPanel;
import com.stockmanager.gui.employe.VentePanel;
import com.stockmanager.serviceimpl.UserInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * classe permettant d'afficher le nenu comportant la liste des operations
 * effectué sur le systeme
 */
public class MainMenuPanel extends JPanel {

    private JPanel container;

    public MainMenuPanel() {
        JXHyperlink produit;
        JXHyperlink utilisateurs;
        JXHyperlink client;
        JXHyperlink fournisseur;
        JXHyperlink vente;
        JXHyperlink achat;
        JXHyperlink facture;
        JXHyperlink changePassword;
        JXHyperlink paramProduit;
        JXHyperlink paramClient;
        JXHyperlink paramUser;
        JXHyperlink typeProduit;
        JXHyperlink mouvement;
        JXHyperlink journal;
        JXHyperlink livraison;
        JXHyperlink venteSpecial;
        JXHyperlink categorie;

        setLayout(new BorderLayout());
        produit = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal) || (UserInfo.getRole() == RoleType.Controleur);
            }
        };
        produit.setText("Produits");
        produit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new ProduitPanel(MainMenuPanel.this));
                } catch (Exception ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        produit.setUnclickedColor(Color.blue);
        produit.setClickedColor(Color.blue);

        fournisseur = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.Employee) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal) || (UserInfo.getRole() == RoleType.Controleur);
            }
        };
        fournisseur.setText("Fournisseur");
        fournisseur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new FournisseurPanel(MainMenuPanel.this));
                } catch (Exception ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        fournisseur.setUnclickedColor(Color.blue);
        fournisseur.setClickedColor(Color.blue);

        utilisateurs = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal);
            }
        };
        utilisateurs.setText("Utilisateurs");
        utilisateurs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new UtilisateurPanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        utilisateurs.setUnclickedColor(Color.blue);
        utilisateurs.setClickedColor(Color.blue);

        paramUser = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal);
            }
        };
        paramUser.setText("Parametrage Utilisateur");
        paramUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setContenu(new ParamUserPanel(MainMenuPanel.this));
            }
        });
        paramUser.setUnclickedColor(Color.blue);
        paramUser.setClickedColor(Color.blue);

        journal = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal);
            }
        };
        journal.setText("Journalisation");
        journal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new JournalPanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        journal.setUnclickedColor(Color.blue);
        journal.setClickedColor(Color.blue);

        client = new JXHyperlink();
        client.setText("Clients");
        client.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new ClientPanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        client.setUnclickedColor(Color.blue);
        client.setClickedColor(Color.blue);

        categorie = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return UserInfo.getRole() == RoleType.AdministrateurPrincipal;
            }
        };
        categorie.setText("Categorie Client");
        categorie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new CategoriePanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        categorie.setUnclickedColor(Color.blue);
        categorie.setClickedColor(Color.blue);

        paramClient = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return UserInfo.getRole() == RoleType.AdministrateurPrincipal;
            }
        };
        paramClient.setText("Parametrage Client");
        paramClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setContenu(new ParamClientPanel(MainMenuPanel.this));
            }
        });
        paramClient.setUnclickedColor(Color.blue);
        paramClient.setClickedColor(Color.blue);

        JXTaskPaneContainer menu = new JXTaskPaneContainer();
        JXTaskPane produitPane = new JXTaskPane() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal) || (UserInfo.getRole() == RoleType.Controleur);
            }
        };
        produitPane.setTitle("Espace Produit");
        produitPane.add(produit);

        JXTaskPane clientPane = new JXTaskPane() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.Employee) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal) || (UserInfo.getRole() == RoleType.Controleur);
            }
        };
        clientPane.setTitle("Espace Client");
        clientPane.add(client);

        JXTaskPane fournisseurPane = new JXTaskPane() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.Employee) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal) || (UserInfo.getRole() == RoleType.Controleur);
            }
        };
        fournisseurPane.setTitle("Espace Fournisseur");
        fournisseurPane.add(fournisseur);

        menu.add(produitPane);
        menu.add(clientPane);
        menu.add(fournisseurPane);

        JXTaskPane adminPane = new JXTaskPane() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal);
            }
        };
        adminPane.setTitle("Espace Administration");
        adminPane.add(utilisateurs);
        adminPane.add(paramUser);
        adminPane.add(journal);

        JXTaskPane OperationPane = new JXTaskPane() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.Employee) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal) || (UserInfo.getRole() == RoleType.Controleur);
            }
        };
        OperationPane.setTitle("Espace Operation");
        vente = new JXHyperlink();
        vente.setText("Vente");
        vente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new VentePanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        vente.setUnclickedColor(Color.blue);
        vente.setClickedColor(Color.blue);

        achat = new JXHyperlink();
        achat.setText("Achat");
        achat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new AchatPanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        achat.setUnclickedColor(Color.blue);
        achat.setClickedColor(Color.blue);

        facture = new JXHyperlink();
        facture.setText("Facture");
        facture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new FacturePanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        facture.setUnclickedColor(Color.blue);
        facture.setClickedColor(Color.blue);

        livraison = new JXHyperlink();
        livraison.setText("Livraison");
        livraison.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new LivraisonPanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        livraison.setUnclickedColor(Color.blue);
        livraison.setClickedColor(Color.blue);

        mouvement = new JXHyperlink();
        mouvement.setText("Mouvement de Stock");
        mouvement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new MouvementPanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        mouvement.setUnclickedColor(Color.blue);
        mouvement.setClickedColor(Color.blue);

        venteSpecial = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal);
            }
        };

        venteSpecial.setText("Vente Special");
        venteSpecial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setContenu(new VenteSpecialPanel(MainMenuPanel.this));
            }
        });
        venteSpecial.setUnclickedColor(Color.blue);
        venteSpecial.setClickedColor(Color.blue);

        OperationPane.add(achat);
        OperationPane.add(vente);
        OperationPane.add(facture);
        OperationPane.add(livraison);
        OperationPane.add(mouvement);
        OperationPane.add(venteSpecial);
        menu.add(OperationPane);

        JXTaskPane UtilisateurPane = new JXTaskPane() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.Employee) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal) || (UserInfo.getRole() == RoleType.Controleur);
            }
        };
        UtilisateurPane.setTitle("Espace Utilisateur");
        changePassword = new JXHyperlink();
        changePassword.setText("Changer Mot de Passe");
        changePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setContenu(new ChangePassWordPanel(MainMenuPanel.this));
            }
        });
        changePassword.setUnclickedColor(Color.blue);
        changePassword.setClickedColor(Color.blue);
        UtilisateurPane.add(changePassword);
        menu.add(UtilisateurPane);
        menu.add(adminPane);

        JXTaskPane ParameterPane = new JXTaskPane() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal);
            }
        };
        ParameterPane.setTitle("Espace De Parametrage");

        paramProduit = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal);
            }
        };

        paramProduit.setText("Paramrtrage Produit");
        paramProduit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setContenu(new ParamProduitPanel(MainMenuPanel.this));
            }
        });
        paramProduit.setUnclickedColor(Color.blue);
        paramProduit.setClickedColor(Color.blue);

        typeProduit = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.Administrateur) || (UserInfo.getRole() == RoleType.AdministrateurPrincipal);
            }
        };
        typeProduit.setText("Type de Produit");
        typeProduit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new TypeProduitPanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        typeProduit.setUnclickedColor(Color.blue);
        typeProduit.setClickedColor(Color.blue);

//        ParameterPane.add(paramProduit);
//        ParameterPane.add(typeProduit);
//        menu.add(ParameterPane);

        produitPane.add(paramProduit);
        produitPane.add(typeProduit);

        add(menu, BorderLayout.BEFORE_LINE_BEGINS);
        container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(2, 20, 20, 20));
        //container.setBackground(Color.GREEN);
        add(container, BorderLayout.CENTER);
    }

    public void setContenu(JPanel pan) {
        container.removeAll();
        container.add(BorderLayout.CENTER, pan);
        container.validate();
    }
}

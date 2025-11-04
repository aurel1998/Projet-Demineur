
import javax.swing.*;
import java.awt.event.* ; 

public class BarreMenu extends JMenuBar implements ActionListener
{
	
	private static final long serialVersionUID = 1;
		private static JMenuItem normal, quit;
        private GUI mon_GUI;         
        private grille grille_jeu;
        private int choix;
        
		// Constructeur du 	
		public BarreMenu(GUI gui) 
        {  	
			mon_GUI=gui;
			grille_jeu=mon_GUI.getGrille();
			
			JMenu menuJeu = new JMenu("Menu");
				//declaration des éléments du menu				
				normal = new JMenuItem("Recommencer");
				quit   = new JMenuItem("Quitter le jeu");				
								
				// ecouteur d'evenements du menu
				normal.addActionListener(this);
				quit.addActionListener(this);
				
				//ajout dans le menu
				menuJeu.add(normal);		
				menuJeu.addSeparator();
				menuJeu.add(quit);				
				
            // après on ajoute tout à la barre de menu
			this.add(menuJeu);			
		
		}
        
             	
     	public void actionPerformed(ActionEvent e)
     	/* procedure lancée lorsqu'un evenement se produit
     	 * traitement de cet evenement
     	 */
     	{     		
     		Object obj = e.getSource();
     		if(obj.equals(normal)) // cas Nouvelle Partie en mode Normal
     		{
     			grille_jeu=mon_GUI.nouvelle_partie(1);				
     		}
     		else if(obj.equals(quit)) // cas si on veut quitter
     		{
    			choix = JOptionPane.showConfirmDialog(null,"Etes vous sur de vouloir Quitter le jeu ?","Confirmation",JOptionPane.YES_NO_OPTION) ;
				if (choix == JOptionPane.YES_OPTION)
					System.exit(0) ; 
	        }    		
     		
        }
        
}
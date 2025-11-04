import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;


public class ZoneGraphique extends JPanel implements MouseListener
{
   
	private static final long serialVersionUID = 1L;
	private chrono timer;            
	private grille grille_jeu;
	private int compteur_drapeau;
	private GUI mon_GUI;
	private final int BOMBE=666;
	private Image bombe_img, drapeau_img, temps_img, 
				  perdu_img, clicked_img, gagne_img, en_cours_img, smiley_en_cours, gameover_img;
				  
	private Color[] couleurs = new Color[8];
	
	/* Constructeur
	 */
	public ZoneGraphique(GUI gui)
	{
	
		// Pré-chargement des images.
		// But : eviter la latence pour afficher une image
		MediaTracker trk = new MediaTracker(gui);
		
		drapeau_img     = Toolkit.getDefaultToolkit().getImage("img/drapeau.png");
		bombe_img       = Toolkit.getDefaultToolkit().getImage("img/bombe.png");
		temps_img       = Toolkit.getDefaultToolkit().getImage("img/temps.png");		
		perdu_img       = Toolkit.getDefaultToolkit().getImage("img/perdu.png");
		gagne_img       = Toolkit.getDefaultToolkit().getImage("img/gagne.png");
		clicked_img     = Toolkit.getDefaultToolkit().getImage("img/clicked.png");
		en_cours_img    = Toolkit.getDefaultToolkit().getImage("img/en_cours.png");
		smiley_en_cours = Toolkit.getDefaultToolkit().getImage("img/en_cours.png");
		gameover_img    = Toolkit.getDefaultToolkit().getImage("img/gameover.png");
		
		trk.addImage(drapeau_img, 0);
		trk.addImage(bombe_img, 1);
		trk.addImage(temps_img, 2);
		trk.addImage(perdu_img, 3);
		trk.addImage(gagne_img, 4);
		trk.addImage(clicked_img, 5);
		trk.addImage(en_cours_img, 6);
		trk.addImage(smiley_en_cours, 7);
		trk.addImage(gameover_img, 8);	
		
		
		try
		{			
				trk.waitForAll();
		} 
		catch (InterruptedException e) 
		{
			// Traitement de l'exception.
			System.out.println("Une erreur est survenue lors du chargement des images ...");
		}
		// les couleurs des chiffres
		couleurs[0] = new Color(0, 0, 255);
	    couleurs[1] = new Color(0, 128, 0);
	    couleurs[2] = new Color(255, 0, 0);
	    couleurs[3] = new Color(0, 0, 128);
	    couleurs[4] = new Color(128, 0, 0);
	    couleurs[5] = new Color(0, 128, 128);
	    couleurs[6] = new Color(128, 0, 128);
	    couleurs[7] = new Color(0, 0, 0);	
		
		
		//on stocke dans une variable locale la grille de jeu et le GUI
		mon_GUI=gui;
		grille_jeu=mon_GUI.getGrille();
		compteur_drapeau=grille_jeu.getNombreBombes();
		timer = new chrono(this);
		timer.start();
		timer.suspend();
		timer.reset();
		
		
		//les ecouteurs		
		addMouseListener(this);		
	}	
	
	
	public void paint(Graphics g)
	/* procedure de dessin lancée automatiquement
	 * ou lancée sur demande via repaint()
	 */
	{			
		
		super.paintComponent(g) ;   // on demande de repeindre correctement ce composant        
		super.revalidate(); // on revalide le composant
        
        Graphics2D g2 = (Graphics2D) g;
        
        grille_jeu=mon_GUI.getGrille();
        
        // le compteur de drapeau
        g.setFont(new java.awt.Font("Monospaced", 1, 15));
		g.setColor(Color.black);
		g.fillRect(1,10,39,20);
		g.setColor(Color.red);
		g.drawRect(1,10,39,20);
		
		g.setColor(Color.red);
		g.drawString(Integer.toString(compteur_drapeau),4,25);
        g.drawImage(bombe_img, 42, 11, this);
        
        
        // le bouton du milieu
        g.setColor(Color.black);
        g.draw3DRect(grille_jeu.getNbrColonnes()*20/2-13,7,25,25,true);
        g.drawImage(smiley_en_cours, grille_jeu.getNbrColonnes()*20/2-11, 9, this);
        
        
        // le timer      
		
		g.drawImage(temps_img, grille_jeu.getNbrColonnes()*20-63, 9, this);
		g.fillRect(grille_jeu.getNbrColonnes()*20-40,10,39,20);
		g.setColor(Color.red);
		g.drawRect(grille_jeu.getNbrColonnes()*20-40,10,39,20);
		
		g.setColor(Color.red);
		g.drawString(Integer.toString(timer.getAff()),grille_jeu.getNbrColonnes()*20-34,25);		
		
        //la grille
        dessiner_grille(g2);
    }
   
	
    
    private void dessiner_grille(Graphics2D g)
    // procedure d'affichage de la grille (cela inclue aussi les bombes et drapeaux)    
    {   
        int x_pos=0, y_pos=40;
        grille_jeu=mon_GUI.getGrille();
        
        for(int i=0; i<grille_jeu.getNbrLignes(); i++)
        {
        	for(int j=0; j<grille_jeu.getNbrColonnes(); j++)
        	{
	        	if(grille_jeu.getEtat(i,j)==0)
	        		g.setColor(Color.gray);
	        	else
	        		g.setColor(Color.lightGray);
	            g.fillRect(x_pos,y_pos, 20 ,20);
	        	g.setColor(Color.black);
	            g.drawRect(x_pos,y_pos,20,20);                                
	        	g.setFont(new java.awt.Font("Monospaced", 1, 15));	
	        	if(grille_jeu.getEtat(i,j)==1)
	        	{
	        		switch (grille_jeu.getValue(i,j))
					{		
						case BOMBE :  g.drawImage(bombe_img, x_pos+1, y_pos+1, this);									  
									  break;							
				
						case 0 :  	  break;						
				
						default : 	  g.setColor(couleurs[grille_jeu.getValue(i,j)-1]); g.drawString(Integer.toString(grille_jeu.getValue(i,j)),x_pos+5,y_pos+15);
								  	  break;						
					}
				}
				else if(grille_jeu.getEtat(i,j)==2)
					g.drawImage(drapeau_img, x_pos+1, y_pos+1, this);				
				
				x_pos+=20;
        	}
        	y_pos+=20;x_pos=0;
    	}
    	if(grille_jeu.getJeuFini())    	
    		g.drawImage(gameover_img, grille_jeu.getNbrColonnes()*20/2-75, grille_jeu.getNbrLignes()*20/2-54+40, this);
    }    
    
    
    public void mousePressed(MouseEvent e)
    /* procedure executée si un des boutons de la souris est pressé */
	{
		if(!grille_jeu.getJeuFini())
		{
			mon_GUI.setFocusActif(true);
			if(timer.isSuspended()) timer.resume();
			smiley_en_cours=clicked_img;
			repaint();
		}
	}		
		
	public void mouseReleased(MouseEvent e)
	/* procedure executée si un des boutons de la souris est relaché */
	{
		int check_x, check_y;
		
		if(!grille_jeu.getJeuFini()) smiley_en_cours=en_cours_img;
		grille_jeu=mon_GUI.getGrille();			
		
		if(e.getY()>=40)
		{
			if(timer.isSuspended() && !grille_jeu.getJeuFini()) timer.resume();
			mon_GUI.setFocusActif(true);
			
			// protection contre effets de bords
			check_x=e.getX()/20;
			if(check_x>=grille_jeu.getNbrColonnes()) check_x=grille_jeu.getNbrColonnes()-1;
			
			check_y=e.getY()/20-2;
			if(check_y>=grille_jeu.getNbrLignes()) check_y=grille_jeu.getNbrLignes()-1;
			
			// Choix du bouton
			switch(e.getButton())
			{
				// bouton gauche de la souris --> traitement des cases
				case 1 : if(!grille_jeu.getJeuFini() && grille_jeu.getEtat(check_y, check_x)==0)
							 if(grille_jeu.estBombe(check_y,check_x))
							 {
							 	timer.suspend();
							 	grille_jeu.DecouvrirBombes();
							 	grille_jeu.setJeuFini();
							 	smiley_en_cours=perdu_img;							 	
							 }
							 else
							 {
							 	grille_jeu.gestion_case(check_y,check_x);
							 	if(grille_jeu.estVictoire())
							 	{
							 		timer.suspend();
							 		grille_jeu.DecouvrirBombes();
							 		grille_jeu.setJeuFini();
							 		smiley_en_cours=gagne_img;
							 		
							 	}							 	
							 }
						 break;
						 
				//bouton droit de la souris --> placement ou retrait d'un drapeau
				case 3 : if(!grille_jeu.getJeuFini() && grille_jeu.getEtat(check_y, check_x)!=1)
						 	if(grille_jeu.getEtat(check_y, check_x)==2)
						 	{
								compteur_drapeau++;
								grille_jeu.setEtat(check_y, check_x, 0);
							}
						 	else if(compteur_drapeau>-50)
						 	{
						 		compteur_drapeau--;
						 		grille_jeu.setEtat(check_y, check_x, 2);
						 	}
						 break;
			}
			
		}
		else // ici on a cliqué sur le smiley du mileu --> on lance une nouvelle partie du niveau courant
		{
			if(e.getX()>=(grille_jeu.getNbrColonnes()*20/2-11) &&
				e.getX()<=(grille_jeu.getNbrColonnes()*20/2+11) &&
				e.getY()>=9 && e.getY()<=50)
			{				
				grille_jeu=mon_GUI.nouvelle_partie(grille_jeu.getNiveau());								
				smiley_en_cours=en_cours_img;
			}
		}				
		
		// Le traitement est terminé, on rafraîchit
		repaint();	
	}
		
	public chrono getChrono()
	/* retourne l'objet timer de type chrono */
	{
		return timer;
	}
	
	public void setValeurCompteurDrapeau(int value)
	/* assigne une valeur donnée au compteur de drapeaux */
	{
		compteur_drapeau=value;
	}
	
	/* procédures nécessaires pour le MouseListener mais non utilisées */
	public void mouseClicked(MouseEvent e){}	
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	
}   	

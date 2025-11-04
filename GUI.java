import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GUI extends JFrame implements FocusListener
{
	private static final long serialVersionUID = 1L;

	private int HAUTEUR, LARGEUR;
	
	private ZoneGraphique graphisme ;
	private grille grille_jeu;
	private Dimension staille;
	private chrono timer;
	private boolean gestion_focus;
	
	//Constructeur
	 
	public GUI()
	{
		this.setFocusActif(false);
				
		grille_jeu=new grille(1);
		HAUTEUR=20*grille_jeu.getNbrLignes()+100;
		LARGEUR=20*grille_jeu.getNbrColonnes()+11;
		setTitle("** DEMINEUR **");
		
		
		//on centre la fenetre au centre de l'ecran
		staille = Toolkit.getDefaultToolkit().getScreenSize() ;
		this.setBounds((staille.width-LARGEUR)/2,(staille.height-HAUTEUR)/2,LARGEUR,HAUTEUR) ;
		this.setIconImage(new ImageIcon("img/icon/icon.png").getImage());
		
		setJMenuBar(new BarreMenu(this));
		
		graphisme = new ZoneGraphique(this);
		timer=graphisme.getChrono();
		this.getContentPane().add(graphisme) ;
				
		this.setResizable(false);			
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		addFocusListener(this);
	}	
	
	
	public grille nouvelle_partie(int niveau)
	// lance une nouvelle partie avec un niveau 
	{
		this.setFocusActif(false);
		
		grille_jeu=null; // on utilise la propriété du Garbage Collector pour effacer la grille courante
		grille_jeu=new grille(niveau); // on génère la nouvelle grille
		
		// re-initialisation de la nouvelle taille de la fenêtre
		HAUTEUR=20*grille_jeu.getNbrLignes()+34+63;
		LARGEUR=20*grille_jeu.getNbrColonnes()+11;			
		
		// re-initialise le thread à 0 et le compteur de drapeau au nombre de bombes du niveau donné
		graphisme.setValeurCompteurDrapeau(grille_jeu.getNombreBombes());
		
		timer.suspend();
		timer.reset();				
		
		// on rafraîchit la fenêtre pour afficher les modifications apportées à l'interface
		this.setSize(LARGEUR,HAUTEUR);		
		setVisible(true);
		
		graphisme.repaint();
		
		return grille_jeu;		
	} 
	
	
	public boolean FocusActif()
	// retourne la valeur courante de la varibale focus_actif
	{
		return gestion_focus;	
	}
	
	public void setFocusActif(boolean bool)
	// assigne un booléen donné à la variable focus_actif
	{
		gestion_focus=bool;
	}
	
	public grille getGrille()
	// retourne la grille courante
	{
		return grille_jeu;	
	}
	
	public void focusLost(FocusEvent e)
	// on stop le chrono si on perd le focus sur la fenetre (la partie doit être commencée)
	{				
		if(!grille_jeu.getJeuFini() && !timer.isSuspended() && this.FocusActif()) timer.suspend();			
	}
	
	public void focusGained(FocusEvent e)
	// on stop le chrono si on perd le focus sur la fenetre (la partie doit être commencée)
	{		
		if(!grille_jeu.getJeuFini() && this.FocusActif()) timer.resume();	
	}
	
}
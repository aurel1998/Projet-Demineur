
import java.util.Random;

public class grille
{   
    //attributs
    private int NbrLignes, NbrColonnes, nbrbombe;
    private int ma_grille[][][]=null;
    private final int BOMBE=666;
    private boolean jeu_fini;
    private int niveau_jeu;    
	
	/* Constructeur */
	public grille(int niveau)
	{
		switch (niveau)
		{
		
		case 1 : NbrLignes=16; //mode Normal le mode demandé pour le jeu à realiser
		         NbrColonnes=16;
		         nbrbombe=70;
	 	         break;
	 	 
		case 2 : NbrLignes=9; //mode Débutant s'il y aura lieu pour faire evoluer le jeu
				 NbrColonnes=9;
				 nbrbombe=10;
				 break;
				 	 
			
				 	 	
		}
		
		niveau_jeu=niveau;
		
		//Initialisation de la grille et du score
		ma_grille = new int[NbrLignes][NbrColonnes][2];
		this.purge(); // mise à zéro
		this.placement_bombes(nbrbombe); // affectation des bombes
		jeu_fini=false;
	}
	
	
	//accesseurs	
	public int getNombreBombes()
	// retourne le nombre de bombes de la grille courante
	{
		return nbrbombe;	
	}
	
	
	public int getValue(int ligne, int colonne)
	// retourne la valeur d'une case donnée dans la grille courante
	{
		return ma_grille[ligne][colonne][0];
	}
	
	
	public void setValue(int ligne, int colonne, int valeur)
	// assigne une valeur à une case donnée dans la grille courante
	{
		ma_grille[ligne][colonne][0]=valeur;
	}
	
	
	public int getEtat(int ligne, int colonne)
	/* retourne l'état d'un case donnée de la grille courante
	 * 0 = non découverte
	 * 1 = découverte
	 * 2 = drapeau placé
	 */
	{
		return ma_grille[ligne][colonne][1];
	}
	
	
	public void setEtat(int ligne, int colonne, int valeur)
	// assigne un état à une case donnée de la grille courante
	{
		ma_grille[ligne][colonne][1]=valeur;
	}
	
	
	public boolean estBombe(int ligne, int colonne)
	// retourne vrai si la case donnée est une bombe, faux sinon
	{
		return (this.getValue(ligne, colonne)==BOMBE);
	}
	
	
	public int getNbrLignes()
	// retourne le nombre de lignes de la grille courante
	{
		return NbrLignes;	
	}
	
	
	public int getNbrColonnes()
	// retourne le nombre de colonnes de la grille courante
	{
		return NbrColonnes;	
	}
	
	public void setJeuFini()
	// annonce que la partie en cours est terminée
	{
		jeu_fini=true;	
	}
	
	public boolean getJeuFini()
	/* retourne le statut de la partie en cours
	 * vrai = jeu terminé
	 * faux = le jeu n'est pas terminé
	 */
	{
		return jeu_fini;	
	}
	
	
	public int getNiveau()
	/* retourne le niveau de jeu courant si le jeu est par niveau
	 * 1 = Normal
	 * 2 = Quitter
	 */
	{
		return niveau_jeu;	
	}
	
	
	//méthodes
	private int getVoisins(int i, int j)
	/* retourne le nombre de bombes sur les 8 cases voisines d'une case donnée 
	 * (dans le cas où cette case n'a pas de bombe
	 */
	{
		int nbre_voisins=0;		
	    if (this.getValue(i,j)!=BOMBE)
	    {
	       	if ((j-1>=0)&&(this.getValue(i,j-1)==BOMBE)) nbre_voisins++; // case gauche
			if (((i-1>=0)&&(j-1>=0))&&(this.getValue(i-1,j-1)==BOMBE)) nbre_voisins++; // case haut gauche
			if ((i-1>=0)&&(this.getValue(i-1,j)==BOMBE)) nbre_voisins++; // case haut
			if (((i-1>=0)&&(j+1<NbrColonnes))&&(this.getValue(i-1,j+1)==BOMBE)) nbre_voisins++; // case haut droit
			if ((j+1<NbrColonnes)&&(this.getValue(i,j+1)==BOMBE))  nbre_voisins++; //case droit
			if (((i+1<NbrLignes)&&(j+1<NbrColonnes))&&(this.getValue(i+1,j+1)==BOMBE)) nbre_voisins++; // case bas droit
			if ((i+1<NbrLignes)&&(this.getValue(i+1,j)==BOMBE)) nbre_voisins++; // case bas
			if (((i+1<NbrLignes)&&(j-1>=0))&&(this.getValue(i+1,j-1)==BOMBE)) nbre_voisins++; //case bas gauche
		}
		else
			nbre_voisins=BOMBE;
				
		return nbre_voisins;		
	}
	
	
	public void gestion_case(int i, int j)
	/* effectue un traitement récursif sur une case donnée
	 * - test si la case donnée est découverte ou non
	 * --> si la case n'est pas découverte alors on répete la procédure récursivement sur ses 8 voisins
	 * --> cela conduit à un découvrement automatique de la grille de jeu.
	 */
	{
	    int resultat=0;	    
	    
	    if (this.getEtat(i,j)==0) // case non découverte
	    {
	    	//on "decache" la case
	    	this.setEtat(i,j,1);
	    
	    	//cacul de nbre de bombes sur les 8 cases voisins 
	    	resultat=this.getVoisins(i,j);
	    	this.setValue(i,j,resultat);		
	
			//decouvrement des cases voisines recursivement 
			//si pas de bombe sur les 8 cases voisines de la case courante.
				if (resultat==0)
				{
					if (i>0)
					{
						if (j>0) this.gestion_case(i-1, j-1);
						this.gestion_case(i-1, j);
						if (j<NbrColonnes-1) this.gestion_case(i-1, j+1);
					}
					if (j>0) this.gestion_case(i,j-1);
					if (j<NbrColonnes-1) this.gestion_case(i,j+1);
					if (i<NbrLignes-1)
					{
						if (j>0) this.gestion_case(i+1,j-1);
						this.gestion_case(i+1,j);
						if (j<NbrColonnes-1) this.gestion_case(i+1, j+1);
					}
				}
				
		}
	}
	
	
	private void purge()
	// procédure qui remplit la grille de 0 pour être sur de ne pas avoir de valeurs innatendues
	{
		for(int i=0; i<NbrLignes; i++)
			for(int j=0; j<NbrColonnes; j++)
			{
				this.setValue(i,j,0); //valeur
				this.setEtat(i,j,0); //état
			}
	}
	
	
	public void DecouvrirBombes()
	/* procédure qui décache toutes les bombes de la grille
	 * (procédure lancée quand la partie est terminée)
	 */
	{
		for(int i=0; i<NbrLignes; i++)
			for(int j=0; j<NbrColonnes; j++)
			{
				if(this.estBombe(i,j)) this.setEtat(i,j,1);
			}
	}
	
	
	private void placement_bombes(int nbrboom)
	// procédure de placement aléatoire des bombes sur la grille courante
	{
	    int i,j,compteur=0;
	    Random aleat = new Random();
	    
	    while (compteur<nbrboom)
	    {
			
			i=aleat.nextInt(NbrLignes);
			j=aleat.nextInt(NbrColonnes);
			if (!this.estBombe(i,j))
			{
		   		this.setValue(i,j,BOMBE);
			   	compteur++;
			}
	    }
	}
	
	public boolean estVictoire()
	/* fonction effectuant un parcours de la grille afin de déterminer
	 * si le joueur a bel et bien gagner.
	 */
	{
		int compteur_cases_decouverte=0;
		for(int i=0; i<NbrLignes; i++)
			for(int j=0; j<NbrColonnes; j++)
			{
				if(this.getEtat(i,j)==1)
					compteur_cases_decouverte++;	
			}
		return((NbrLignes*NbrColonnes)-compteur_cases_decouverte==this.getNombreBombes());
	}
	
	
}
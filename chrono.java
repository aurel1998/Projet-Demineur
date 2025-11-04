
public class chrono implements Runnable
{
	Thread thread;
	int aff;
	ZoneGraphique ZG;
	boolean marche=true;
	boolean threadSuspended=false;
	
	public chrono(ZoneGraphique ZGraphe)
	{    
		ZG=ZGraphe;
		aff=0;
	}
	
	public void run()
	{
		while(marche)
		{
			try
			{
				Thread.sleep(1000);
				if(threadSuspended)
				{
					synchronized(this) 
					{
						while (threadSuspended)	wait();
					}
				}
			}
			catch(java.lang.InterruptedException e) {}
	
			int time = aff;//ZG.getChrono().;
			if (marche && time<999)
			{
				aff=time+1;
				ZG.repaint();        
			}
		}
	}
	
	public void start()
	// procédure de lancement du thread en mode priorité haute (afin de garantir un affichage correcte)
	{
		if (thread==null) thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	
	public void suspend()
	// met le thread en pause
	{
		threadSuspended=true;
	}
	
	public void reset()
	// met à 0 le compteur du thread
	{
		aff=0;  	
	}
	
	public synchronized void resume()
	// relance le thread après la mise en pause
	{
		threadSuspended=false;
		this.notify();
	}
	
	public int getAff()
	// retourne la valeur courante du compteur du thread
	{
		return aff;
	}
	
	public boolean isSuspended()
	// retourne vrai si le thread est en pause, faux sinon
	{
		return threadSuspended;
	}
	
}

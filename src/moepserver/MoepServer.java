package moepserver;

import java.util.ArrayList;
import java.util.logging.Level;
import moepserver.netzwerk.ServerListener;

/**
 * Startklasse des Servers
 * Hier wird eine neue Serverinstanz erstellt
 * @author Christian Diller
 * @version BETA 1.1
 */
public class MoepServer 
{
    private ArrayList<Server> servers = new ArrayList<Server>();
    private ServerListener listener;
    
    public static void main(String[] args) 
    {
        MoepLogger.log(Level.INFO, "*** Starte Moep ServerONLY Edition ***");
        MoepLogger.log(Level.INFO, "*** Composed by Christian Diller ***");
        new MoepServer();
    }
    
    public MoepServer()
    {
        listener = new ServerListener(this, 11111);
        listener.start();
        neuenServerStarten();
    }
    
    private void neuenServerStarten()
    {
        String name = "Server #" + (servers.size() + 1);        
        MoepLogger.log(Level.INFO, name + " wird gestartet");        
        servers.add(new Server(name, this));
    }
    
    public void spielerHinzufuegen(Spieler neu, int position)
    {
        for(int i = 0; i < servers.size(); i++)
        {
            if(!servers.get(i).istVoll()) {
                servers.get(i).spielerHinzufuegen(neu, position);
                return;
            }
        }
        neuenServerStarten();
        servers.get(servers.size() - 1).spielerHinzufuegen(neu, position);
    }
}

package moepserver.netzwerk;

import moepserver.Server;
import moepserver.Spieler;

/**
 * Essentiell, um das Netzwerk zu initialisieren und neue Spieler zum Server hinzuzufügen
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Netz
{
    private Server server;

    private ServerListener listener;
    private ServerBroadcast broadcast;

    public Netz(Server _server, int port)
    {
        server = _server;
        listenerStarten(port);
        broadcastStarten();
    }
    
    /**
     * Startet den ServerListener
     */
    public final void listenerStarten(int port)
    {
        listener = new ServerListener(this, port);
        listener.start();
    }
    
    /**
     * Stoppt den ServerListener (Neue Verbindungen werden dann nicht mehr akzeptiert!)
     */
    public void listenerStoppen()
    {
        listener.interrupt();
    }
    
    /**
     * Wird von LoginWaechter aufgerufen, wenn ein neuer Login erfolgt ist
     * @param spieler Der dem Spiel hinzuzufügende Spieler
     */
    protected void loginEvent(Verbindung verbindung, String name) {
        server.spielerHinzufuegen(new Spieler(verbindung, name, verbindung.gibIP()));
    }

    private void broadcastStarten() {
        broadcast = new ServerBroadcast();
        broadcast.start();
    }
}

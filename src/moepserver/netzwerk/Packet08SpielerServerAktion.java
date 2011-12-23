
package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Server dem Client mitteilt, dass sich ein Spieler angemeldet/abgemeldet hat
 * Der Client aktualisiert daraufhin seine Spielerliste
 * Zudem wird der Spieler uebermittelt, der aktuell am Zug ist
 * Client <- Server
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Packet08SpielerServerAktion extends Packet{
    
    private String spielername;
    private int art; //0: Login; 1: Logout
    
    public Packet08SpielerServerAktion(String _spielername, int _art)
    {
        spielername = _spielername;
        art = _art;
    }
    
    @Override
    public String gibData()
    {
        return "08" + seperator + spielername + seperator + art;
    }
    
    @Override
    public void eventAufrufen(Verbindung verbindung)
    {
        //Kein Serverevent!
    }    
    
    @Override
    public void eventAusloesen(Verbindung verbindung)
    {
        
    }
}

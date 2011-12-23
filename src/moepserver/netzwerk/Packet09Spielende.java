package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Server dem CLient mitteilt, 
 * dass das Spiel vorbei ist und dieser das Spielfeld aufräumen soll.
 * Ausserdem kann einem Spieler so übermittelt werden, dass er gewonnen hat
 * Client <- Server
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Packet09Spielende extends Packet{
    
    private boolean gewonnen;
    
    public Packet09Spielende(boolean _gewonnen)
    {
        gewonnen = _gewonnen;
    }
    
    @Override
    public String gibData()
    {
        return "09" + seperator + (gewonnen ? "Y":"N");
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

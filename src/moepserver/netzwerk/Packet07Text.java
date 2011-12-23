
package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Server dem Client beliebigen Text sendet,
 * der dann dem Spieler angezeigt wird
 * Client <- Server
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Packet07Text extends Packet{
    
    private String text;
    
    public Packet07Text(String _text)
    {
        text = _text;
    }
    
    @Override
    public String gibData()
    {
        return "07" + seperator + text;
    }
    
    @Override
    public void eventAufrufen(Verbindung verbindung)
    {
        //Kein Serverevent
    }    
    
    @Override
    public void eventAusloesen(Verbindung verbindung)
    {
        
    }
}

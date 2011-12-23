
package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Server einen Client kicken kann
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Packet02Kick extends Packet{
    
    private String grund;
    
    public Packet02Kick(String _grund)
    {
        grund = _grund;
    }
    
    @Override
    public String gibData()
    {
        return "02" + seperator + grund;
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

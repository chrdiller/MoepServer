
package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Server dem Client mitteilt, dass der letze Zug ungültig war
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Packet04ZugLegal extends Packet{
    
    private boolean legal;
    private int illegalArt;
    
    public Packet04ZugLegal(boolean _legal, int _illegalArt)
    {
        legal = _legal;
        illegalArt = _illegalArt;
    }
    
    @Override
    public String gibData()
    {
        return "04" + seperator + (legal ? "Y" : "N") + seperator + illegalArt;
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

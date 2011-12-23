
package moepserver.netzwerk;

import moepserver.Karte;
/**
 * Beschreibt das Packet, mit dem der Server dem Client eine Handkarte übermittelt
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Packet11Handkarte extends Packet{
    
    private Karte karte;
    
    public Packet11Handkarte(Karte _karte)
    {
        karte = _karte;
    }
    
    public Karte gibKarte()
    {
        return karte;
    }
    
    @Override
    public String gibData()
    {
        return "11" + seperator + karte.gibDaten();
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

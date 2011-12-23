
package moepserver.netzwerk;

import moepserver.Karte;
/**
 * Beschreibt das Packet, mit dem der Server dem Client eine Ablagestapelkarte übermittelt
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Packet12Ablagestapelkarte extends Packet{
    
    private Karte karte;
    
    public Packet12Ablagestapelkarte(Karte _karte)
    {
        karte = _karte;
    }
    
    @Override
    public String gibData()
    {
        return "12" + seperator + karte.gibDaten();
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

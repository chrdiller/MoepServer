
package moepserver.netzwerk;

import moepserver.Karte;
/**
 * Beschreibt das Packet, mit dem der Client dem Server die zu legende Karte übermittelt
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Packet13KarteLegen extends Packet{
    
    private Karte karte;
    
    public Packet13KarteLegen(Karte _karte)
    {
        karte = _karte;
    }
    
    @Override
    public String gibData()
    {
        return "13" + seperator + karte.gibDaten();
    }
    
    @Override
    public void eventAufrufen(Verbindung verbindung)
    {
        PacketInteractor pe = new PacketInteractor(this, verbindung);
        pe.start();
    }
    
    @Override
    public void eventAusloesen(Verbindung verbindung)
    {
        verbindung.karteLegenEvent(karte);        
    }
}


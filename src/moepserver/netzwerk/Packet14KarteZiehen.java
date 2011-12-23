
package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Client dem Server mitteilt, dass eine Karte gezogen werden soll
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Packet14KarteZiehen extends Packet{
    
    public Packet14KarteZiehen()
    {
        
    }
    
    @Override
    public String gibData()
    {
        return "14" + seperator;
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
        verbindung.karteZiehenEvent();        
    }
}

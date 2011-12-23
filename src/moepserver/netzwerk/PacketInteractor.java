
package moepserver.netzwerk;

/**
 *
 * @author Christian Diller
 */
public class PacketInteractor extends Thread
{
    private Verbindung verbindung;
    private Packet packet;
    
    public PacketInteractor(Packet _packet, Verbindung _verbindung)
    {
        verbindung = _verbindung;
        packet = _packet;
    }
    
    @Override
    public void run()
    {
        packet.eventAusloesen(verbindung);
    }
}

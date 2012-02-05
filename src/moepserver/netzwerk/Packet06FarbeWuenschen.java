package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Server dem Client mitteilt, dass dieser dem Server
 * eine gewünschte Farbe schicken soll; der Client schickt die Farbe an den Server
 * @author Christian Diller
 */
public class Packet06FarbeWuenschen extends Packet
{

    private int farbe;

    public Packet06FarbeWuenschen(int _farbe)
    {
        farbe = _farbe;
    }

    @Override
    public String gibData()
    {
        return "06" + seperator + farbe;
    }

    @Override
    public void serverEventAufrufen(Verbindung verbindung)
    {
        verbindung.farbeWuenschenEvent(farbe);
    }
}

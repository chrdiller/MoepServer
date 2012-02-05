package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Client dem Server mitteilt, dass eine Karte gezogen werden soll
 * Client -> Server
 * @author Christian Diller
 */
public class Packet14KarteZiehen extends Packet
{

    public Packet14KarteZiehen()
    {
    }

    @Override
    public String gibData()
    {
        return "14" + seperator;
    }

    @Override
    public void serverEventAufrufen(Verbindung verbindung)
    {
        verbindung.karteZiehenEvent();
    }
}

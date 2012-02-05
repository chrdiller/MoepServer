package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Server dem Client beliebigen Text sendet,
 * der dann dem Spieler angezeigt wird
 * Client <- Server
 * @author Christian Diller
 */
public class Packet07Text extends Packet
{

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
    public void serverEventAufrufen(Verbindung verbindung)
    {
        //Kein Serverevent
    }
}

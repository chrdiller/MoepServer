package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Server einen Client kicken kann
 * Client <- Server
 * @author Christian Diller
 */
public class Packet02Kick extends Packet
{

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
    public void serverEventAufrufen(Verbindung verbindung)
    {
        //Kein Serverevent!
    }
}

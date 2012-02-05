package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Server dem CLient mitteilt, 
 * dass das Spiel vorbei ist und dieser das Spielfeld aufräumen soll.
 * Ausserdem kann einem Spieler so übermittelt werden, ob er gewonnen hat
 * Client <- Server
 * @author Christian Diller
 */
public class Packet09Spielende extends Packet
{

    private boolean gewonnen;

    public Packet09Spielende(boolean _gewonnen)
    {
        gewonnen = _gewonnen;
    }

    @Override
    public String gibData()
    {
        return "09" + seperator + (gewonnen ? "Y" : "N");
    }

    @Override
    public void serverEventAufrufen(Verbindung verbindung)
    {
        //Kein Serverevent!
    }
}

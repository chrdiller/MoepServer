package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Server dem Client mitteilt, dass der letze Zug ungültig war
 * Client <- Server
 * @author Christian Diller
 */
public class Packet04ZugLegal extends Packet
{

    private boolean legal;
    private int illegalArt;

    public Packet04ZugLegal(boolean _legal, int _illegalArt)
    {
        legal = _legal;
        illegalArt = _illegalArt;
    }

    @Override
    public String gibData()
    {
        return "04" + seperator + (legal ? "Y" : "N") + seperator + illegalArt;
    }

    @Override
    public void serverEventAufrufen(Verbindung verbindung)
    {
        //Kein Serverevent!
    }
}

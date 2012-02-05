package moepserver.netzwerk;

import moepserver.Karte;

/**
 * Beschreibt das Packet, mit dem der Server dem Client eine Handkarte übermittelt
 * Client <- Server
 * @author Christian Diller
 */
public class Packet11Handkarte extends Packet
{

    private Karte karte;

    public Packet11Handkarte(Karte _karte)
    {
        karte = _karte;
    }

    public Karte gibKarte()
    {
        return karte;
    }

    @Override
    public String gibData()
    {
        return "11" + seperator + karte.gibDaten();
    }

    @Override
    public void serverEventAufrufen(Verbindung verbindung)
    {
        //Kein Serverevent!
    }
}

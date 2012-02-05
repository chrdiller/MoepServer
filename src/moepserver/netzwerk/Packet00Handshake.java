package moepserver.netzwerk;

/**
 * Beschreibt den Austausch der Protokollversionen
 * Der Client sendet dem Server seine Version, der Server schickt zurück,
 * ob die Versionen übereinstimmen
 * Client <-> Server
 * @author Christian Diller
 */
public class Packet00Handshake extends Packet
{

    private int protokollversion;
    private boolean gleicheVersion;

    public Packet00Handshake(int _protokollversion, boolean _gleicheVersion)
    {
        protokollversion = _protokollversion;
        gleicheVersion = _gleicheVersion;
    }

    @Override
    public String gibData()
    {
        return "00" + seperator + protokollversion + seperator + (gleicheVersion ? "Y" : "N");
    }

    @Override
    public void serverEventAufrufen(Verbindung verbindung)
    {
        verbindung.protokollversionAnfrage(protokollversion);
    }
}

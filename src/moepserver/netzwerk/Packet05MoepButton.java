package moepserver.netzwerk;

/**
 * Beschreibt das Packet, mit dem der Client dem Server mitteilt, dass der MoepButton gedrückt wurde
 * Client -> Server
 * @author Christian Diller
 */
public class Packet05MoepButton extends Packet
{

    public Packet05MoepButton()
    {
    }

    @Override
    public String gibData()
    {
        return "05" + seperator;
    }

    @Override
    public void serverEventAufrufen(Verbindung verbindung)
    {
        verbindung.moepButtonEvent();
    }
}


package moepserver.netzwerk;

/**
 * Beschreibt das für den Login verwendete Packet
 * @author Christian Diller
 * @version BETA 1.1
 */
public class Packet01Login extends Packet{
    
    private String name;
    private String passwort = ""; //(Noch) nicht implementiert
    private boolean akzeptiert; //Nur vom Server verwendet
    
    public Packet01Login(String _name, boolean _akzeptiert)
    {
        name = _name;
        akzeptiert = _akzeptiert;
    }
    
    @Override
    public String gibData()
    {
        return "01" + seperator + name + seperator + (akzeptiert ? "Y":"N");
    }
    
    @Override
    public void eventAufrufen(Verbindung verbindung)
    {
       //Kein direkter Eventaufruf im Server
    }
    
    @Override
    public void eventAusloesen(Verbindung verbindung)
    {
        
    }
    
    public String gibName()
    {
        return name;
    }
}

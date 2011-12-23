package moepserver.netzwerk;

/**
 * Überbrückt die Zeit von einer neuen Verbindung bis zum Login
 * @author Christian Diller
 * @version BETA 1.1
 */
public class LoginWaechter extends Thread{
    
    Verbindung verbindung;
    Netz netz;
    
    public LoginWaechter(Verbindung _verbindung, Netz _netz)
    {
        verbindung = _verbindung;
        netz = _netz;
        this.setName("LoginWaechterThread");
    }
    
    @Override
    public void run()
    {
        while(!verbindung.istAktiv)
        {
            //Warten...
            try{sleep(500);}catch(Exception ex){}
        }
        netz.loginEvent(verbindung, verbindung.loginName);
    }
    
}

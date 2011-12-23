
package moepserver.netzwerk;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import moepserver.MoepLogger;

/**
 * Wartet auf neue Verbindungen
 * @author Christian Diller
 * @version BETA 1.1
 */
public class ServerListener extends Thread
{
    private ServerSocket serverSocket;
    Netz netz; //Netz muss referenziert werden, um Logins behandeln zu können
    private int listenPort;
    
    private static final MoepLogger log = new MoepLogger();

    public ServerListener(Netz _netz, int _port)
    {
        listenPort = _port;
        if(listenPort < 0)
            listenPort = 11111;
        netz = _netz;
        this.setName("ServerListenerThread");
    }

    @Override
    public void run()
    {
        Socket clientSocket = null;

        try             
        {                       
            serverSocket = new ServerSocket(listenPort);
        } 
        catch (Exception ex) 
        {
            log.log(Level.SEVERE, "Fehler beim Starten des Servers auf Port " + listenPort);
            return;
        }

        while(true)
        {
            try 
            {
                clientSocket = serverSocket.accept();
            } 
            catch (Exception ex) 
            {
                log.log(Level.WARNING, "Akzeptieren einer neuen Verbindung fehlgeschlagen");
            }

            Verbindung verbindung = new Verbindung(new VerbindungReaderThread(clientSocket), new VerbindungWriterThread(clientSocket)); 
            verbindung.start();
            LoginWaechter logW = new LoginWaechter(verbindung, netz);
            logW.start();

            clientSocket = null;
            verbindung = null;
            logW = null;
        }
    }
}

package moepserver.netzwerk;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import moepserver.MoepLogger;

/**
 * Wird pro Verbindung erzeugt
 * Sendet die zu sendenden Strings
 * @author Christian Diller
 */

public class VerbindungWriter
{
    private Socket clientSocket;
    private PrintWriter output;
    
    public VerbindungWriter(Socket _clientSocket)
    {
        clientSocket = _clientSocket;
        try
        {
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        }
        catch(Exception ex)
        {
            MoepLogger.log(Level.SEVERE, "Initialisieren von VerbindungWriter fehlgeschlagen");
        }
    }
    
    public void senden(String data)
    {
        output.println(data);
    }
}

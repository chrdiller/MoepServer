package moepserver.netzwerk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import moepserver.MoepLogger;

/**
 * Wird pro Verbindung erzeugt
 * Wartet auf eingehende Packets und setzt diese auf eine Liste, die dann von der Verbindung abgearbeitet wird
 * @author Christian Diller
 */
public class VerbindungReader extends Thread
{

    private Socket clientSocket;
    private BufferedReader input;
    protected Verbindung verbindung;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public VerbindungReader(Socket _clientSocket)
    {

        clientSocket = _clientSocket;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception ex) {
            MoepLogger.log(Level.SEVERE, "Initialisieren von VerbindungReader fehlgeschlagen");
        }
    }

    @Override
    public void run()
    {
        try {
            while (true) {
                final String inputLine = input.readLine();
                if (inputLine == null) {
                    break;
                }
                executor.execute(new Runnable()
                {

                    public void run()
                    {
                        verbindung.neuesPacket(inputLine);
                    }
                });
            }
            verbindung.verbindungVerlorenEvent(); //Hier kommt die Ausführung nur hin, wenn der Server die Verbindung geschlossen hat

            input.close();
            clientSocket.close();
        } catch (Exception ex) {
            verbindung.verbindungVerlorenEvent();
        }
    }

    public String gibIP()
    {
        return clientSocket.getRemoteSocketAddress().toString();
    }
}

package moepserver.netzwerk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import moepserver.MoepLogger;
import moepserver.MoepServer;
import moepserver.SpielerRemote;

/**
 * Wartet auf neue Verbindungen
 * @author Christian Diller
 */
public class ServerListener extends Thread
{

    private ServerSocket serverSocket;
    private MoepServer server;
    private int listenPort;
    private boolean beendet = false;

    public ServerListener(MoepServer _server, int _port)
    {
        listenPort = _port;
        if (listenPort < 0) {
            listenPort = 11111;
        }
        server = _server;
        this.setName("ServerListenerThread");
    }

    @Override
    public void run()
    {
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(listenPort);
        } catch (Exception ex) {
            MoepLogger.log(Level.SEVERE, "Fehler beim Starten des Servers auf Port " + listenPort);
            return;
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                final Verbindung verbindung = new Verbindung(new VerbindungReader(clientSocket), new VerbindungWriter(clientSocket));
                new Thread()
                {

                    public void run()
                    {
                        warteAufLogin(verbindung);
                    }
                }.start();
            } catch (Exception ex) {
                if (!beendet) {
                    MoepLogger.log(Level.SEVERE, "Akzeptieren einer neuen Verbindung fehlgeschlagen");
                }
                break;
            }
        }
    }

    private void warteAufLogin(final Verbindung verbindung)
    {
        while (!verbindung.istAktiv) {
            //Warten...
            try {
                sleep(500);
            } catch (Exception ex) {
            }
        }
        server.spielerHinzufuegen(new SpielerRemote(verbindung, verbindung.loginName, verbindung.gibIP()), -1);
    }

    public void beenden()
    {
        beendet = true;
        try {
            serverSocket.close();
        } catch (IOException ex) {
        }
    }
}


package moepserver.netzwerk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import moepserver.MoepLogger;

/**
 * Wird pro Verbindung erzeugt
 * Wartet auf eingehende Packets und setzt diese auf eine Liste, die dann von der Verbindung abgearbeitet wird
 * @author Christian Diller
 * @version BETA 1.1
 */
public class VerbindungReaderThread extends Thread{
    
    private Socket clientSocket;
    private BufferedReader input;
    private List<String> inputListe;
    protected Verbindung verbindung;
    
    private static final MoepLogger log = new MoepLogger();
    
    public VerbindungReaderThread(Socket _clientSocket) {
        
        clientSocket = _clientSocket;
        try
        {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }
        catch(Exception ex)
        {
            log.log(Level.SEVERE, "Fehler beim Initialisieren von VerbindungReaderThread");
        }
        inputListe = new ArrayList<String>();
    }
    
    @Override
    public void run()
    {
        String inputLine;
        try
        {
            while ((inputLine = input.readLine()) != null) 
            {   
                inputListe.add(inputLine);
                synchronized(verbindung)
                {
                    verbindung.notify();
                }
            }
            verbindung.verbindungVerlorenEvent();

            input.close();
            clientSocket.close();
        }
        catch(Exception ex)
        {
            verbindung.verbindungVerlorenEvent();
        }
        try
        {
            input.close();
            clientSocket.close();
        }
        catch(Exception ex){/*Nö, der Fehler ist es nicht wert, ausgegeben zu werden ;)*/}
    }
    
    protected String pop()
    {
        String ausgabe = inputListe.get(0);
        inputListe.remove(0);
        return ausgabe;
    }
    
    protected boolean istLeer()
    {
        return inputListe.isEmpty();
    }
    
    protected String gibIP()
    {
        //return clientSocket.getInetAddress().getHostAddress();
        return clientSocket.getRemoteSocketAddress().toString();
    }
    
}


package moepserver.netzwerk;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import moepserver.MoepLogger;

/**
 * Wird pro Verbindung erzeugt
 * Arbeitet eine Liste von zu sendenden Strings ab
 * @author Christian Diller
 * @version BETA 1.1
 */
public class VerbindungWriterThread extends Thread{

    private Socket clientSocket;
    private PrintWriter output;
    
    private ArrayList<String> outputListe = new ArrayList<String>();
    
    private static final MoepLogger log = new MoepLogger();
    
    public VerbindungWriterThread(Socket _clientSocket) {
        
        clientSocket = _clientSocket;
        try
        {
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        }
        catch(Exception ex)
        {
            log.log(Level.SEVERE, "Fehler beim Initialisieren von VerbindungWriterThread");
        }
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            synchronized(this)
            {
                try
                {
                    this.wait();
                }
                catch(InterruptedException ex)
                {
                    output.close();
                    try{
                    clientSocket.close();}catch(Exception exep){}
                }
            }
            
            while(!outputListe.isEmpty())
            {
                output.println(outputListe.get(0));
                outputListe.remove(0);
            }
        }
    }
    
    public void push(String data)
    {
        outputListe.add(data);
    }
}

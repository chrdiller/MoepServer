package moepserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * Die zentrale Logging-Klasse des Servers
 * (Der Java-Logger wird nicht verwendet, da sich dieser beim Formatieren querstellt)
 * @author Christian Diller
 */
public class MoepLogger 
{
    public static void log(Level level, String meldung)
    {	
        SimpleDateFormat format = new SimpleDateFormat();       
        
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        sb.append(format.format(new Date()));
        sb.append(" - ");
        sb.append(level.getLocalizedName() +": ");
        sb.append(meldung);
        sb.append("\r\n");
        String pfad = Server.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        pfad = pfad.substring(0, pfad.lastIndexOf("/"));
        pfad = pfad + "/moepServer.log";
        try {
            FileWriter fw = new FileWriter(new File(pfad), true);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException ex) {}
        
        System.out.print(sb.toString());
    }
}
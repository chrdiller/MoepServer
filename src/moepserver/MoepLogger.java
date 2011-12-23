package moepserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * Die zentrale Logging-Klasse
 * (Der Java-Logger wird nicht verwendet, da sich dieser beim Formatieren querstellt)
 * @author Christian Diller
 * @version BETA 1.1
 */
public class MoepLogger 
{

    public void log(Level level, String meldung)
    {	
        SimpleDateFormat format = new SimpleDateFormat();       
        
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        sb.append(format.format(new Date()));
        sb.append(" - ");
        sb.append(level.getLocalizedName() +": ");
        sb.append(meldung);
        System.out.println(sb.toString());
    }
}
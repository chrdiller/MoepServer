
package moepserver;

/**
 * Beschreibt eine einzelne (Server-)Karte
 * @author Markus Klar, Michael Stoffels 
 * @version BETA 1.1
 */
public class Karte implements Comparable<Karte>
{
    private int farbe;
    private int nummer;
    
    public Karte(int farbeN, int nummerN)
    {
        farbe = farbeN;
        nummer = nummerN;
    }
    
    public Karte(String dataString)
    {
        String dataStringArray[] = dataString.split("\\|");
        farbe = Integer.valueOf(dataStringArray[0]).intValue();
        nummer = Integer.valueOf(dataStringArray[1]).intValue();
    }
    
    public int gibFarbe()
    {
        return farbe;
    }
    
    public int gibNummer()
    {
        return nummer;
    }
    
    public String gibDaten()
    {
        return farbe+"|"+nummer;
    }
    
    @Override
    public int compareTo(Karte k)
    {        
        if(farbe>k.gibFarbe())
        {
            return -1;
        }
        else if(farbe<k.gibFarbe())
        {
            return 1;
        }
        else if(farbe==k.gibFarbe() && nummer>k.gibNummer())
        {
            return -1;
        }
        else if(farbe==k.gibFarbe() && nummer<k.gibNummer())
        {
            return 1;
        }
        else if(farbe==k.gibFarbe() && nummer==k.gibNummer())
        {
            return 0;
        }
        return -2;
    }
}

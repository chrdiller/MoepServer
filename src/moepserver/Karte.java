package moepserver;

/**
 * Stellt eine einzelne Karte dar
 * Spezifikation:
 * Farbe-int: 0 = Blau, 1 = Rot, 2 = Grün, 3 = Gelb
 * Nummer-int: 0-9 = normale Nummern, 10 = Aussetzen, 11 = 2+ -Karte, 12 = Richtungswechsel
 * Sonderkarten: Farbe = 4; Nummer = 13 => FarbeWünschen-Karte, Nummer = 14 => 4+ -Karte
 * Daten-Strings: farbe+"|"+nummer
 * @author Christian Diller, Markus Klar, Michael Stoffels 
 */
public class Karte implements Comparable<Karte>
{

    private int farbe;
    private int nummer;

    /**
     * Erzeugt eine Karte anhand der zugehörigen Farbe und Nummer
     * @param farbeN Der int-Wert der Farbe
     * @param nummerN Der int-Wert der Nummer
     */
    public Karte(int farbeN, int nummerN)
    {
        farbe = farbeN;
        nummer = nummerN;
    }

    /**
     * Erzeugt eine Karte anhand des zugehörigen dataStrings
     * @param dataString Der datenString der Karte (siehe Spezifikation)
     */
    public Karte(String dataString)
    {
        String dataStringArray[] = dataString.split("\\|");
        farbe = Integer.valueOf(dataStringArray[0]).intValue();
        nummer = Integer.valueOf(dataStringArray[1]).intValue();
    }

    /**
     * Gibt die Farbe der Karte zurück
     * @return Der int-Wert der Farbe
     */
    public int gibFarbe()
    {
        return farbe;
    }

    /**
     * Gibt die Nummer der Karte zurück
     * @return Der int-Wert der Nummer
     */
    public int gibNummer()
    {
        return nummer;
    }

    /**
     * Erzeugt den dataString der Karte
     * @return Der dataString der Karte
     */
    public String gibDaten()
    {
        return farbe + "|" + nummer;
    }

    /**
     * Dient in einer geordneten Liste dazu, diese Karte richtig einzusortieren
     * @param k Die Karte, mit der verglichen wird
     * @return Das Ergebnis des Vergleichs in int-Form
     */
    @Override
    public int compareTo(Karte k)
    {
        if (farbe > k.gibFarbe()) {
            return -1;
        } else if (farbe < k.gibFarbe()) {
            return 1;
        } else if (farbe == k.gibFarbe() && nummer > k.gibNummer()) {
            return -1;
        } else if (farbe == k.gibFarbe() && nummer < k.gibNummer()) {
            return 1;
        } else if (farbe == k.gibFarbe() && nummer == k.gibNummer()) {
            return 0;
        }
        return -2;
    }
}

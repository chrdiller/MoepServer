package moepserver;

import java.util.ArrayList;
import moepserver.netzwerk.Verbindung;

/**
 * Die abstrakte Oberklasse, die einen Spieler vorgibt
 * @author Christian Diller
 */
public abstract class Spieler
{

    public String spielername;
    public int kartenanzahl;
    protected ArrayList<Karte> hand;
    protected Verbindung verbindung;
    public Server server;
    protected boolean moep = false;
    protected String loginIP;

    public abstract void handReset();

    public abstract void karteHinzufuegen(Karte neu);

    public abstract void karteEntfernen(Karte karte);

    public abstract int gibKartenanzahl();

    public abstract ArrayList<Karte> gibHand();

    public abstract boolean istInHand(Karte gesucht);

    public abstract void fehlerEvent(String beschreibung);

    public abstract void verbindungVerlorenEvent();

    public abstract void karteLegenEvent(Karte karte);

    public abstract void karteZiehenEvent();

    public abstract void moepButtonEvent();

    public abstract void neueHandkarte(Karte karte);

    public abstract void neueAblagekarte(Karte k);

    public abstract void amZug(boolean wert);

    public abstract void ungueltigerZug(int art);

    public abstract void gueltigerZug();

    public abstract void loginAblehnen();

    public abstract void textSenden(String t);

    public abstract int farbeFragen();

    public abstract void loginAkzeptieren();

    public abstract void spielerServerAktion(String sn, int wert, int kartenzahl, int position);

    public abstract void spielEnde(boolean gewonnen);

    public abstract String gibIP();

    public abstract void warteAufMoep();

    public abstract void kick(String grund);
}

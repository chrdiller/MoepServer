package moepserver;

import java.util.ArrayList;
import java.util.logging.Level;
import moepserver.netzwerk.Verbindung;

/**
 * Beschreibt einen Remote-Spieler, also einen Spieler,
 * der über das Netzwerk verbunden ist
 * @author Christian Diller, (Frank Kottler)
 */
public class SpielerRemote extends Spieler
{

    public SpielerRemote(Verbindung _verbindung, String _spielername, String _loginIP)
    {
        verbindung = _verbindung;
        loginIP = _loginIP;
        spielername = _spielername;
        verbindung.spieler = this; //Kreisreferenz muss sein, um in Spieler Events auslösen zu können
        hand = new ArrayList<Karte>();
    }

    @Override
    public void handReset()
    {
        hand = new ArrayList();
        kartenanzahl = 0;
    }

    @Override
    public void karteHinzufuegen(Karte neu)
    {
        hand.add(neu);
        kartenanzahl++;
    }

    @Override
    public void karteEntfernen(Karte karte)
    {
        for (int j = 0; j < kartenanzahl; j++) {
            if ((hand.get(j).gibNummer() == karte.gibNummer()) && (hand.get(j).gibFarbe() == karte.gibFarbe())) {
                hand.remove(j);
                kartenanzahl--;
                return;
            }
        }
    }

    @Override
    public int gibKartenanzahl()
    {
        return kartenanzahl;
    }

    @Override
    public ArrayList<Karte> gibHand()
    {
        return hand;
    }

    @Override
    public boolean istInHand(Karte gesucht)
    {
        //return hand.contains(searched);
        for (int i = 0; i < kartenanzahl; i++) {
            if ((hand.get(i).gibNummer() == gesucht.gibNummer()) && (hand.get(i).gibFarbe() == gesucht.gibFarbe())) {
                return true;
            }

        }
        return false;
    }

    @Override
    public void fehlerEvent(String beschreibung)
    {
        MoepLogger.log(Level.SEVERE, beschreibung);
    }

    @Override
    public void verbindungVerlorenEvent()
    {
        MoepLogger.log(Level.WARNING, "Verbindung zu Spieler " + spielername + " verloren");
        server.spielerEntfernen(this);
    }

    @Override
    public void karteLegenEvent(Karte karte)
    {
        server.spielerzugEvent(karte);
    }

    @Override
    public void karteZiehenEvent()
    {
        server.karteZiehenEvent();
    }

    @Override
    public void moepButtonEvent()
    {
        server.moep(this);
    }

    @Override
    public void neueHandkarte(Karte karte)
    {
        verbindung.sendeHandkarte(karte);
    }

    @Override
    public void neueAblagekarte(Karte k)
    {
        verbindung.sendeAblagestapelkarte(k);
    }

    @Override
    public void amZug(boolean wert)
    {
        verbindung.sendeAmZug(wert);
    }

    @Override
    public void ungueltigerZug(int art)
    {
        verbindung.sendeUngueltigerZug(art);
    }

    @Override
    public void gueltigerZug()
    {
        verbindung.sendeGueltigerZug();
    }

    @Override
    public void loginAblehnen()
    {
        verbindung.sendeLoginAntwort(false);
    }

    @Override
    public void textSenden(String t)
    {
        verbindung.sendeText(t);
    }

    @Override
    public int farbeFragen()
    {
        verbindung.sendeFarbeWuenschen();
        while (!verbindung.farbeWuenschenAntwortErhalten()) {
            try {
                Thread.currentThread().sleep(200);
            } catch (InterruptedException ex) {
            }
        }
        return verbindung.gibFarbeWuenschenInt();
    }

    @Override
    public void loginAkzeptieren()
    {
        verbindung.sendeLoginAntwort(true);
    }

    @Override
    public void spielerServerAktion(String sn, int wert, int kartenzahl, int position)
    {
        verbindung.sendeSpielerServerAktion(sn, wert, kartenzahl, position);
    }

    @Override
    public void spielEnde(boolean gewonnen)
    {
        hand = new ArrayList<Karte>();
        this.kartenanzahl = this.gibKartenanzahl();
        verbindung.sendeSpielende(gewonnen);
    }

    @Override
    public String gibIP()
    {
        return verbindung.gibIP();
    }

    @Override
    public void warteAufMoep()
    {
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException ex) {
        }
    }

    @Override
    public void kick(String grund)
    {
        verbindung.sendeKick(grund);
    }
}

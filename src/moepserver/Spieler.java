
package moepserver;

import java.util.ArrayList;
import java.util.logging.Level;
import moepserver.netzwerk.Verbindung;

/**
 * Beschreibt einen einzelnen Spieler
 * @author Frank Kottler, Christian Diller
 * @version BETA 1.1
 */
public class Spieler {

    private static final MoepLogger log = new MoepLogger();
    public String spielername;
    public int kartenanzahl;
    private ArrayList<Karte> hand;
    private Verbindung verbindung;
    public Server server;
    protected boolean moep = false;
    protected String loginIP;

    public Spieler(String n)

    {
        spielername = n;
        hand = new ArrayList<Karte>();
    }

    public Spieler(Verbindung _verbindung, String _spielername, String _loginIP)
    {
        verbindung = _verbindung;
        loginIP = _loginIP;
        spielername = _spielername;
        verbindung.spieler = this; //Kreisreferenz muss sein, um in Spieler Events auslösen zu können
        hand = new ArrayList<Karte>();
    }
    
    public void handReset()
    {
        hand = new ArrayList();
        kartenanzahl = 0;
    }

    public void getcards(Karte neu)
    {
        hand.add(neu);
        kartenanzahl++;
    }
    
    public void legecards(Karte Chosen)
    {
        for(int j = 0; j < kartenanzahl; j++)
        {
            if((hand.get(j).gibNummer()==Chosen.gibNummer())&&(hand.get(j).gibFarbe()==Chosen.gibFarbe()))
            {
                hand.remove(j);
                kartenanzahl--;  
                return;
            }
        }    
    }
    
    public int givekartenanzahl()
    {
        return kartenanzahl;
    }
    
    public ArrayList<Karte> getList()
    {
        return hand;
    }

    public boolean inHand(Karte searched)
    {
        //return hand.contains(searched);
        for(int i=0;i<kartenanzahl;i++)
        {
            if((hand.get(i).gibNummer()==searched.gibNummer())&&(hand.get(i).gibFarbe()==searched.gibFarbe()))
            {
                return true;
            }
            
        }
        return false;
    }

    public void fehlerEvent(String beschreibung) {
        log.log(Level.SEVERE, "Fehler: " + beschreibung);
    }

    public void verbindungVerlorenEvent() {
        log.log(Level.INFO, "Verbindung zu Spieler " + spielername + " verloren");
        server.spielerEntfernen(this);
    }

    public void karteLegenEvent(Karte karte) {
        server.spielerzugEvent(karte);
    }

    public void karteZiehenEvent() {
        server.karteZiehenEvent();
    }

    public void moepButtonEvent() {
        server.moep(this);
    }

    public void karteBekommen(Karte karte) {
        verbindung.sendeHandkarte(karte);
    }

    public void neueAblagekarte(Karte k) {
        verbindung.sendeAblagestapelkarte(k);
    }

    public void amZug(boolean wert) {
        verbindung.sendeAmZug(wert);        
        if(wert)
        {
            server.broadcast(spielername + " ist am Zug");     
            log.log(Level.INFO, "Spieler " + spielername + " ist am Zug");
        }
    }

    public void ungueltigerZug(int art) {
        verbindung.sendeUngueltigerZug(art);
    }
    
    public void gueltigerZug()
    {
        verbindung.sendeGueltigerZug();
    }

    public void loginAblehnen() {
        verbindung.sendeLoginAntwort(false);
    }

    public void textSenden(String t) {
       verbindung.sendeText(t);
    }

    public int farbeFragen() {
        verbindung.sendeFarbeWuenschen();
        while(!verbindung.farbeWuenschenAntwortErhalten()){try {
                Thread.currentThread().sleep(200);
            } catch (InterruptedException ex) {}}
        return verbindung.gibFarbeWuenschenInt();
    }
    
    public void loginAkzeptieren() {
        verbindung.sendeLoginAntwort(true);
    }
    
    public void spielerServerAktion(String sn, int wert)
    {
        verbindung.sendeSpielerServerAktion(sn, wert);
    }
    
    public void spielEnde(boolean gewonnen)
    {
        hand = new ArrayList<Karte>();
        this.kartenanzahl = this.givekartenanzahl();
        verbindung.sendeSpielende(gewonnen);
    }

    public String gibIP()
    {
        return verbindung.gibIP();
    }

    public void warteAufMoep() {      
        try
        {
            Thread.currentThread().sleep(2000); 
        }        
        catch (InterruptedException ex) {}     
    }
    
    public void kick(String grund)
    {
        verbindung.sendeKick(grund);
    }
}


package moepserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import moepserver.netzwerk.Netz;

/**
 * Die zentrale Serverklasse
 * @author Frank Kottler & Christian Diller
 * @version BETA 1.1
 */
public class Server {
    private static final MoepLogger log = new MoepLogger();
    public static int STARTKARTEN;

    private Netz netz;

    private Karte offen;
    private ArrayList<Karte> verdeckt;
    private ArrayList<Spieler> spieler;
    private String[] realspieler;
    private int richtung; // 1 fuer im Uhrzeigersinn, -1 fuer gegen den UZS
    private int aktuellerSpielerIndex;
    private int neueFarbe;
    private int alterSpielerIndex;
    private Properties properties;
    

    public Server() {
        log.log(Level.INFO, "*** Starte MoepServer BETA 1.1 :) ***");
        loadProperties();
        netz = new Netz(this, Integer.valueOf(properties.getProperty("Port", "11111")).intValue());
    	verdeckt = this.kartenSet();
	spieler = new ArrayList();
	richtung = 1;
        neueFarbe = 4;
        realspieler = new String[4];
	aktuellerSpielerIndex = 0;
	deckeErsteKarteAuf();
        alterSpielerIndex = 0;
        STARTKARTEN = Integer.valueOf(properties.getProperty("Startkarten", "7")).intValue();
    }

    /**
     * Erzeugt eine Arraylist, die alle am Spiel beteiligten Karten enthält
     * @return Die Arraylist
     */
    private ArrayList<Karte> kartenSet() {
        ArrayList<Karte> temp = new ArrayList();

        //Wert 0 jeweils einmal
        for(int j=0; j<4; j++) {
            temp.add(new Karte(j, 0));
        }

        // Alle anderen bis auf schwarz: 2x
        for(int i = 0; i<2; i++) {
            for(int j = 1; j<=12; j++) {
                for(int k = 0; k<4; k++) {
                    temp.add(new Karte(k, j));
                }
            }
        }

        // Sonderkarten: 4x
        for(int i = 13; i <= 14; i++) {
            for(int j = 0; j < 4; j++) {
                temp.add(new Karte(4, i));
            }
        }

        return temp;
    }

    /**
     * Fügt einen Spieler zum Spiel hinzu; wird von LoginWaechter ausgerufen
     * @param neu Der neue Spieler mit passender Verbindung
     */
    public void spielerHinzufuegen(Spieler neu) {
        log.log(Level.INFO, "Spieler " + neu.spielername + " (" + neu.gibIP() + ") hat sich verbunden");
        if(spieler.size() < 4) {
            neu.loginAkzeptieren();
            log.log(Level.INFO, "Spieler " + neu.spielername + " wurde akzeptiert (Spieler " + (aktuellerSpielerIndex + 1) + " von 4)");
            for(Spieler s : spieler) //Übermittlung der aktuell angemeldeten Spieler an den neuen Spieler
            {
                neu.spielerServerAktion(s.spielername, 0);                  
            }
          
            spieler.add(neu);
            neu.server = this;

            for(Spieler s : spieler)
            {
                s.spielerServerAktion(neu.spielername, 0); //Login-Nachricht an alle
                s.textSenden(neu.spielername + " ist dem Spiel beigetreten");
            }

            this.erzeugeHand(neu);
            aktuellerSpielerIndex = (aktuellerSpielerIndex+1)%4;

            if(aktuellerSpielerIndex == 0) { // Das Spiel geht los
                log.log(Level.INFO, "Ein neues Spiel wurde gestartet");
                broadcast("Ein neues Spiel wurde gestartet");
                for(int i = 0; i<4; i++)  {
                    spieler.get(i).neueAblagekarte(offen);
                    for(Karte k : spieler.get(i).getList()) {
                        spieler.get(i).karteBekommen(k);
                    }
                }
                spieler.get(aktuellerSpielerIndex).amZug(true);
                for(Spieler s : spieler)
                {
                    s.spielerServerAktion(spieler.get(aktuellerSpielerIndex).spielername, 2); //2 = Am Zug
                }
            }
        } else 
        {
            neu.loginAblehnen();
            log.log(Level.INFO, "Spieler " + neu.spielername + " wurde abgewiesen");
        }

    }
    
    /**
     * Entfernt einen Spieler aus dem Spiel und beendet anschließend das Spiel
     * @param entf Den zu entfernenden Spieler
     */
    public void spielerEntfernen(Spieler entf)
    {
        spieler.remove(entf);
        log.log(Level.INFO, "Spieler " + entf.spielername + " wurde vom Server entfernt");
        for(Spieler s : spieler)
        {
            s.spielerServerAktion(entf.spielername, 1);
            s.amZug(false);            
            s.textSenden("Spieler " + entf.spielername + " hat das Spiel verlassen");
            s.textSenden("Das Spiel wurde beendet");
            s.textSenden("Spielneustart, sobald wieder 4 Spieler online sind");
            s.spielEnde(false);
        }
        spielBeenden();
    }

    public void spielGewonnen(Spieler p) {
        log.log(Level.INFO, "Dieses Spiel wurde von " + p.spielername + " gewonnen");
        for(Spieler s : spieler) {
            s.amZug(false);
            s.textSenden("Spieler " + p.spielername + " hat dieses Spiel gewonnen.");
            s.textSenden("Das Spiel wurde beendet.");
            s.textSenden("Bitte das Spiel verlassen. Nicht nochmal spielen, du alter Zocker!");
            if(s.equals(p))
                s.spielEnde(true);
            else
                s.spielEnde(false);
        }
        spielBeenden();
    }

    /**
     * Nimmt eine normale Karte(keine Sonderkarte!) vom verdeckt-Stapel und speichert sie im Feld offen
     */
    private void deckeErsteKarteAuf() {
    	while (true) {
    		offen = verdeckt.remove(new Random().nextInt(verdeckt.size()));
		if (offen.gibNummer() < 10) break;
		verdeckt.add(offen);
	}
	log.log(Level.INFO, "Erste Karte wurde aufgedeckt");
    }

    /**
     * Gibt die zurzeit offene Karte zurück
     * @return Die offene Karte
     */
    public Karte gibOffen() {
	return offen;
    }

    /**
     * Gibt eine zufällige Karte vom verdeckt-Stapel zurück
     * @return Die zufällige Karte
     */
    public Karte gibZufaelligeKarte() {
	return verdeckt.remove(new Random().nextInt(verdeckt.size()));
    }

    /**
     * Gibt die Spielerliste zurück
     * @return Die Spieler-Arraylist
     */
    public ArrayList<Spieler> gibSpieler() {
    	return spieler;
    }

    /**
     * Gibt eine Referenz auf den aktuellen Spieler zurück
     * @return Der aktuelle Spieler
     */
    public Spieler gibAktuellenSpieler() {
	return spieler.get(aktuellerSpielerIndex);
    }

    /**
     * Erzeugt für den übergebenen Spieler eine Hand gemäß der Anzahl der Startkarten
     * @param sp Der betroffene Spieler
     */
    private void erzeugeHand(Spieler sp) {
        sp.handReset();
	for (int i = 0; i < STARTKARTEN; i++) { sp.getcards(this.gibZufaelligeKarte()); }
    }

    /**
     * Prüft, ob eine Karte auf eine andere gelegt werden kann
     * @param legen Die zu legende Karte
     * @param liegt Die Karte, auf die gelegt werden soll
     * @return Kann gelegt werden ja/nein
     */
    private boolean kannGelegtWerdenAuf(Karte legen, Karte liegt) {
        if((neueFarbe != 4) && (legen.gibFarbe() == neueFarbe)) return true;
        if(((legen.gibFarbe() == liegt.gibFarbe()) || legen.gibNummer() == liegt.gibNummer()) && (neueFarbe == 4)) return true;
        if(legen.gibFarbe() == 4) return true;
        return false;
    }

    /**
     * Wird aufgerufen, wenn ein Spieler eine Karte zieht; sendet diesem eine neue Karte und beendet dessen Zug
     * @param quellIP Die IP, von der der Aufruf kommt
     */
    protected void karteZiehenEvent() 
    {
        log.log(Level.INFO, "Spieler " + spieler.get(aktuellerSpielerIndex).spielername + " zieht eine Karte");
        Karte neu = this.gibZufaelligeKarte();
        spieler.get(aktuellerSpielerIndex).getcards(neu);
        spieler.get(aktuellerSpielerIndex).karteBekommen(neu);
		for (Spieler s : spieler) {
		s.textSenden(spieler.get(aktuellerSpielerIndex).spielername + " zieht eine Karte");
	}
        spieler.get(aktuellerSpielerIndex).amZug(false);
        aktuellerSpielerIndex = (aktuellerSpielerIndex
			+ richtung
			+ spieler.size()) % spieler.size();
        spieler.get(aktuellerSpielerIndex).amZug(true);
    }


    /**
     * Wird bei einem Spielerzug (Spieler legt eine Karte) aufgerufen
     * @param karte 
     */
    protected void spielerzugEvent(Karte karte) 
    {
        
        log.log(Level.INFO, "Spieler " + spieler.get(aktuellerSpielerIndex).spielername + " spielt " + karte.gibDaten());
        if (!this.kannGelegtWerdenAuf(karte, offen)) { //Kann die Karte gelegt werden?
                spieler.get(aktuellerSpielerIndex).ungueltigerZug(0);
                log.log(Level.INFO, "Spieler " + spieler.get(aktuellerSpielerIndex).spielername + " spielt einen ungültigen Zug");
                return;
        }

        if(!spieler.get(aktuellerSpielerIndex).inHand(karte)) { //Hat der Spieler die Karte in seiner Hand?
            spieler.get(aktuellerSpielerIndex).ungueltigerZug(1);
            log.log(Level.INFO, "Spieler " + spieler.get(aktuellerSpielerIndex).spielername + " spielt eine Karte, die er nicht besitzt");
            return;
        }
        spieler.get(aktuellerSpielerIndex).gueltigerZug();

        int symbol = karte.gibNummer(); //Ist die Karte eine Sonderkarte?
        boolean istAussetzen        = symbol == 10;
        boolean istZweiPlus         = symbol == 11;
        boolean istRichtungsWechsel = symbol == 12;
        boolean istWuenschen        = symbol == 13;
        boolean istVierPlus         = symbol == 14;

        if (istRichtungsWechsel) { richtung *= -1;  //Richtungswechsel durchführen
            log.log(Level.INFO, "Richtung wurde geändert");}

        spieler.get(aktuellerSpielerIndex).legecards(karte); // Dem aktuellen Spieler OK geben

        alterSpielerIndex = aktuellerSpielerIndex;

        aktuellerSpielerIndex = (aktuellerSpielerIndex //Nächsten Spieler bestimmen gemäß Richtung und Aussetzen
                + (istAussetzen ? 2 : 1) * richtung
                + spieler.size()) % spieler.size();

        verdeckt.add(offen);
        offen = karte;
        neueFarbe = 4; //Karten aktualisieren

        // Jedem Spieler die neue Karte zeigen
        // Aktueller Spieler ist dran.
        for(Spieler p : spieler) {
            p.neueAblagekarte(karte);
        }       

        broadcast(spieler.get(alterSpielerIndex).spielername + " legt eine " + karteZuMeldung(karte));
        switch(karte.gibNummer()) {
            case 11:
                Karte neu = this.gibZufaelligeKarte();
                spieler.get(aktuellerSpielerIndex).karteBekommen(neu);
                spieler.get(aktuellerSpielerIndex).getcards(neu);
                neu = this.gibZufaelligeKarte();
                spieler.get(aktuellerSpielerIndex).karteBekommen(neu);
                spieler.get(aktuellerSpielerIndex).getcards(neu);

                break;
            case 13:
                neueFarbe = spieler.get(alterSpielerIndex).farbeFragen();
                broadcast(spieler.get(alterSpielerIndex).spielername + " wünscht sich " + intZuFarbe(neueFarbe));

                break;
            case 14:
                for(int i = 0; i < 4; i++) {
                    neu = this.gibZufaelligeKarte();
                    spieler.get(aktuellerSpielerIndex).karteBekommen(neu);
                    spieler.get(aktuellerSpielerIndex).getcards(neu);
                }

                neueFarbe = spieler.get(alterSpielerIndex).farbeFragen();
		broadcast(spieler.get(alterSpielerIndex).spielername + " wünscht sich " + intZuFarbe(neueFarbe));

                break;
        }
        if((spieler.get(alterSpielerIndex).givekartenanzahl() == 1))
        {
        spieler.get(alterSpielerIndex).moep = false;
        spieler.get(alterSpielerIndex).warteAufMoep();

            if(!spieler.get(alterSpielerIndex).moep) {
                Karte neu = this.gibZufaelligeKarte();
                log.log(Level.INFO, "Spieler " + spieler.get(aktuellerSpielerIndex).spielername + " hat nicht rechtzeitig Moep gerufen");
                broadcast(spieler.get(alterSpielerIndex).spielername + " hat nicht MOEP gerufen");

                spieler.get(alterSpielerIndex).karteBekommen(neu); 
                spieler.get(alterSpielerIndex).getcards(neu);
            }
            else if(spieler.get(alterSpielerIndex).moep) {
                log.log(Level.INFO, "Spieler " + spieler.get(aktuellerSpielerIndex).spielername + " ruft Moep");
                broadcast(spieler.get(alterSpielerIndex).spielername + " ruft MOEP");
            }
        }
        spieler.get(alterSpielerIndex).moep = false;
        
        spieler.get(alterSpielerIndex).amZug(false);
        
        if(spieler.get(alterSpielerIndex).givekartenanzahl() == 0) {
            
            this.spielGewonnen(spieler.get(alterSpielerIndex));
        }
        else
        {
            spieler.get(aktuellerSpielerIndex).amZug(true);   
            for(Spieler s : spieler)
            {
                s.spielerServerAktion(spieler.get(aktuellerSpielerIndex).spielername, 2); //2 = Am Zug
            }
        }

    }

    protected void moep(Spieler s) {
        if(spieler.indexOf(s) == alterSpielerIndex) spieler.get(alterSpielerIndex).moep = true;
    }
    
    private String karteZuMeldung(Karte karte)
    {
        String ausgabe = "";
        switch(karte.gibFarbe())
        {
            case 0:
                ausgabe += "blaue ";break;
            case 1:
                ausgabe += "rote ";break;
            case 2:
                ausgabe += "grüne ";break;
            case 3:
                ausgabe += "gelbe ";break;
            case 4:
                if(karte.gibNummer() == 13)
                    ausgabe += "Farbe-Wünschen-Karte";
                else if(karte.gibNummer() == 14)
                    ausgabe += "4+ -Karte";
                break;
                
        }
        if(karte.gibNummer() <= 9)
            ausgabe += karte.gibNummer();
        else if(karte.gibNummer() == 10)
            ausgabe += "Aussetzen-Karte";
        else if(karte.gibNummer() == 11)
            ausgabe += "2+ -Karte";
        else if(karte.gibNummer() == 12)
            ausgabe += "Richtungswechsel-Karte";
            
        return ausgabe;
    }
    
    public void broadcast(String text)
    {
        for(Spieler s : spieler)
        {
            s.textSenden(text);
        }
    }

    private void spielBeenden() {
        verdeckt = this.kartenSet();
	richtung = 1;
        neueFarbe = 4;
        realspieler = new String[4];
	aktuellerSpielerIndex = spieler.size();
        for(Spieler s : spieler)
        {
            s.moep = false;
            this.erzeugeHand(s);
        }
	deckeErsteKarteAuf();
        alterSpielerIndex = 0;
        log.log(Level.INFO, "Das Spiel wurde beendet");
    }

    private String intZuFarbe(int farbeInt) {
        String ausgabe = "";
        switch(farbeInt)
        {
            case 0:
                ausgabe = "blau";break;
            case 1:
                ausgabe = "rot";break;
            case 2:
                ausgabe = "grün";break;
            case 3:
                ausgabe = "gelb";break;
        }
        return ausgabe;
    }
    
    
     /**
     * Hier werden die Properties aus der server.properties eingelesen
     * Achtung: Sobald ein Fehler auftritt, wird die gesamte Datei ignoriert!
     */
    private void loadProperties()
    {
        properties = new Properties(); 
        BufferedInputStream fileStream;
        String pfad = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        pfad = pfad.substring(0, pfad.lastIndexOf("/"));
        pfad = pfad + "/server.properties";
        try {
            fileStream = new BufferedInputStream(new FileInputStream(pfad));
            properties.load(fileStream);
            fileStream.close();
            
            log.log(Level.INFO, "Properties: " + properties.size() + " Eintraege erfolgreich eingelesen");
        } catch (Exception ex) {log.log(Level.WARNING, "Properties-Datei konnte nicht eingelesen werden");}
    }
    
}
 
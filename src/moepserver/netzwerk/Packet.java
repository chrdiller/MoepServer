package moepserver.netzwerk;

import moepserver.Karte;

/**
 * Beschreibt die abstrakte Oberklasse der Packetklassen;
 * enthält die statische Methode zum Parsen von Strings in Packets
 * @author Christian Diller
 */
public abstract class Packet
{

    /**
     * Parst einen String in ein Packet
     * @param data Der String, der geparst werden soll
     * @return Wenn gültiger String: Passendes Packet mit entsprechenden Werten; Wenn ungültiger String: null
     */
    public static Packet erstellePacket(String data)
    {
        if (data.isEmpty()) {
            return null;
        }

        String[] dataArray = data.split(seperator);

        if (dataArray[0].equals("00") && dataArray.length == 3) {
            return new Packet00Handshake(Integer.parseInt(dataArray[1]), dataArray[2].equals("Y") ? true : false);
        }
        if (dataArray[0].equals("01") && dataArray.length == 3) {
            return new Packet01Login(dataArray[1], dataArray[2].equals("Y") ? true : false);
        } else if (dataArray[0].equals("05") && dataArray.length == 1) {
            return new Packet05MoepButton();
        } else if (dataArray[0].equals("06") && dataArray.length == 2) {
            return new Packet06FarbeWuenschen(Integer.parseInt(dataArray[1]));
        } else if (dataArray[0].equals("13") && dataArray.length == 2) {
            return new Packet13KarteLegen(new Karte(dataArray[1]));
        } else if (dataArray[0].equals("14") && dataArray.length == 1) {
            return new Packet14KarteZiehen();
        } else {
            return null;
        }
    }

    /**
     * Gibt den Datenstring dieses Packets zurück
     * @return Der weiterverwendbare Datenstring (z.B. fürs Senden)
     */
    public abstract String gibData();

    /**
     * Ruft packetspezifische Events in spieler auf
     * @param spieler Spieler, bei dem das Event aufgerufen werden soll
     */
    public abstract void serverEventAufrufen(Verbindung verbindung);
    protected static String seperator = "#";
}

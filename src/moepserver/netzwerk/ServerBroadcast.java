/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moepserver.netzwerk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author Christian
 */
public class ServerBroadcast extends Thread
{
    public void run()
    {
        DatagramSocket udpSocket = null;
        try {
            udpSocket = new DatagramSocket(111111);
            udpSocket.setBroadcast(true);
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(packet);
                InetAddress sendeAdresse = packet.getAddress();
                System.out.print("Nachricht von " + sendeAdresse.getHostAddress() + ":");
                System.out.println(new String(packet.getData(), 0, packet.getLength()));
                System.out.println("Sende Antwort.. ");
                String antwort = "Hallo!";
                packet = new DatagramPacket(antwort.getBytes(), antwort.length(), sendeAdresse, 111111);
                udpSocket.send(packet);
                System.out.println("Antwort gesendet!");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            udpSocket.close();
        }
    }
}

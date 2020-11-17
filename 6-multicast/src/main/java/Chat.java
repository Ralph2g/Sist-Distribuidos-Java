import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.InputStreamReader;
import java.io.BufferedReader;

class Chat{
    static void envia_mensaje(byte[] buffer,String ip,int puerto) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress grupo = InetAddress.getByName(ip);
        DatagramPacket paquete = new DatagramPacket(buffer,buffer.length,grupo,puerto);
        socket.send(paquete);
        socket.close();
    }
      static byte[] recibe_mensaje(MulticastSocket socket,int longitud_mensaje) throws IOException {
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }
    static class Worker extends Thread{
        public void run(){
            try {
                //Tenemos nuestro hilo que constantemente está al pendiente de los mensajes en el grupo
                for(;;){
                    //Ingresamos al grupo
                    InetAddress grupo = InetAddress.getByName("230.0.0.0");
                    MulticastSocket socket = new MulticastSocket(50000);
                    socket.joinGroup(grupo);
                    //recibimos los mensajes
                    byte[] a = recibe_mensaje(socket,100);
                    System.out.println(new String(a,"UTF-8"));
                    socket.leaveGroup(grupo);
                    socket.close();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) throws Exception{
        Worker w = new Worker();
        w.start();
        //Obtenemos el nombre por medio de nuestro parámetro
        String nombre = args[0];
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
        String mensaje;
        //Se lee el texto y se envía al grupo
        //se leerá los mensajes del teclado y se enviarán
        //al grupo 230.0.0.0 a través del puerto 50000.
        for (;;){
            System.out.println("Escribe el mensaje:");
            mensaje = b.readLine();
            mensaje = nombre + ":" + mensaje;
            envia_mensaje(mensaje.getBytes(),"230.0.0.0",50000);
        }
    }
}
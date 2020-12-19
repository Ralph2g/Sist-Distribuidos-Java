import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;
import java.nio.ByteBuffer;

class uno
{
  public static void main(String[] args) throws Exception
  {
    Socket conexion = new Socket("sisdis.sytes.net",10000);
    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());
    salida.writeInt(1);
    salida.writeInt(20);
    salida.writeDouble(200.0);
    System.out.println(entrada.readInt());
  }
}

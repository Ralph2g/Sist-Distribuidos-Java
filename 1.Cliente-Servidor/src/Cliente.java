import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

class Cliente
{
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception
  {
    while (longitud > 0)
    {
      int n = f.read(b,posicion,longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception
  {
    Socket conexion = new Socket("localhost",50000);

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    // enva un entero de 32 bits
    salida.writeInt(123);

    // envia un numero punto flotante
    salida.writeDouble(1234567890.1234567890);

    // envia una cadena
    salida.write("hola".getBytes());

    // recibe una cadena
    byte[] buffer = new byte[4];
    read(entrada,buffer,0,4);
    System.out.println(new String(buffer,"UTF-8"));

    // envia 5 numeros punto flotante
    ByteBuffer b = ByteBuffer.allocate(5*8);
    b.putDouble(1.1);
    b.putDouble(1.2);
    b.putDouble(1.3);
    b.putDouble(1.4);
    b.putDouble(1.5);
    byte[] a = b.array();
    salida.write(a);
    //===========Tiempo de inicio
	long startTime = System.nanoTime();
    //envia mil numeros de tipo flotante
    for(double num = 1.0; num <= 1000.0; num++)
    	salida.writeDouble(num);
    long endTime = System.nanoTime();
    System.out.println("Took "+(endTime - startTime) + " ns");
    //===========Fin del tiempo
    
    //PARTE 2
    b = ByteBuffer.allocate(1000*8);
    for(double num = 1.0; num <= 1000.0; num++)
    	b.putDouble(num);
    a = b.array();
    //===========Tiempo de inicio
	startTime = System.nanoTime();
    salida.write(a);
    endTime = System.nanoTime();
    System.out.println("Took "+(endTime - startTime) + " ns");
    //===========Fin del tiempo
    
    salida.close();
    entrada.close();
    conexion.close();

  }
}
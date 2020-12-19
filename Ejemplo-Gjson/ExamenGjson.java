import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Timestamp;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;
import java.nio.ByteBuffer;
import java.io.*;
class dos
{
  static class Coordenada{
    int x;
    int y;
    int z;
  }

  static byte[] lee_archivo(String nombre_archivo) throws Exception{
    FileInputStream f = new FileInputStream(nombre_archivo);
    byte[] buffer;
    try{
      buffer = new byte[f.available()];
      f.read(buffer);
    }
    finally{
      f.close();
    }
    return buffer;
  }
  public static void main(String[] args) throws Exception{
    byte[] contenido;
    contenido = lee_archivo("coordenadas.txt");
    Gson contenidos = new GsonBuilder().create();
    String s = contenidos.toJson(contenido);
    System.out.println(s);
    Coordenada[] v = (Coordenada[])contenidos.fromJson(s,Coordenada[].class);
    for (int i = 0; i < v.length; i++)
      System.out.println(v[i].x + " ");
  }
}

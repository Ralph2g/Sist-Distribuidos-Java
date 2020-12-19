import java.io.*; 
import java.net.HttpURLConnection; 
import java.net.URL; 

public class EjercicioTres
{
    public static void main(String args[]) throws Exception
    {
        URL url = new URL("http://sisdis.sytes.net:8080/Servicio/rest/ws/prueba");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);
        // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
        conexion.setRequestMethod("POST");
        // indica que la petición estará codificada como URL
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es "c@com"
        String parametros = "a=1000";
        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();
        // se debe verificar si hubo error
        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
        BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
        String respuesta;
        // el método web regresa una string en formato JSON
        while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
        conexion.disconnect();
    }
}
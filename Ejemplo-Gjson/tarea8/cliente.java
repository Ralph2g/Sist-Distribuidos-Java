import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URLEncoder;


public class Cliente{
    static Scanner teclado = new Scanner(System.in);

    static class Usuario {
      String email;
      String nombre;
      String apellido_paterno;
      String apellido_materno;
      String fecha_nacimiento;
      String telefono;
      String genero;
      byte[] foto;
      Usuario(String email,String nombre,String apellido_paterno,String apellido_materno,String fecha_nacimiento,String telefono,String genero,byte[] foto){
        this.email = email;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.fecha_nacimiento = fecha_nacimiento;
        this.telefono = telefono;
        this.genero = genero;
        this.foto = foto;
      }
    }
    public static void borrarTodos() throws Exception {
        // Se conecta con el servicio REST
        URL url = new URL("http://52.171.215.82:8080/Servicio/rest/ws/limpiar");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);
        conexion.setRequestMethod("POST");
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        // se debe verificar si hubo error
        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
            // throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
            System.out.print("\n**Error eliminando usuarios**\n");
        else
            System.out.print("\n**Los usuario se han eliminado con exito**\n");
        conexion.disconnect();
    }

    public static void alta() throws Exception {
        System.out.print("Email: ");
        String email = teclado.nextLine();
        System.out.print("Nombre: ");
        String nombre = teclado.nextLine();
        System.out.print("ApellidoPaterno: ");
        String apellido_paterno = teclado.nextLine();
        System.out.print("ApellidoMaterno: ");
        String apellido_materno = teclado.nextLine();
        System.out.print("FechaNacimiento: ");
        String fecha_nacimiento = teclado.nextLine();
        System.out.print("Telefono: ");
        String telefono = teclado.nextLine();
        System.out.print("Genero: ");
        String genero = teclado.nextLine();
        Usuario user = new Usuario(email,nombre,apellido_paterno,apellido_materno,fecha_nacimiento,telefono,genero,null);
        Gson j = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        String jsonData = j.toJson(user);
        // Se conecta con el servicio REST
        URL url = new URL("http://52.171.215.82:8080/Servicio/rest/ws/alta");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);
        conexion.setRequestMethod("POST");
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        String parametros = "usuario=" + URLEncoder.encode(jsonData,"UTF-8");
        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();
        // se debe verificar si hubo error
        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
        else
            System.out.print("Ok\n");
        conexion.disconnect();
    }

    public static void consulta() throws Exception {
        System.out.print("Email: ");
        String email = teclado.nextLine();
        // Se conecta con el servicio REST
        URL url = new URL("http://52.171.215.82:8080/Servicio/rest/ws/consulta");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);
        conexion.setRequestMethod("POST");
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        String parametros = "email=" + URLEncoder.encode(email,"UTF-8");
        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();
        // se debe verificar si hubo error
        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
            // throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
            System.out.println("\n**Usuario NO encontrado**\n");
        else {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String jsonData = br.readLine();
            Gson j = new GsonBuilder().create();
            Usuario user = (Usuario)j.fromJson(jsonData,Usuario.class);
            System.out.println("\nUSUARIO ENCONTRADO: \nEmail: " + user.email + "\nNombre: " + user.nombre + "\nApellido Paterno: " + user.apellido_paterno + "\nApellido Materno: " + user.apellido_materno + "\nFecha de nacimiento: " + user.fecha_nacimiento + "\nTelefono: " + user.telefono + "\nGenero: " + user.genero + "\n");
        }
        conexion.disconnect();
    }

    public static void borrar() throws Exception {
        System.out.print("Email: ");
        String email = teclado.nextLine();
        // Se conecta con el servicio REST
        URL url = new URL("http://52.171.215.82:8080/Servicio/rest/ws/borra");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);
        conexion.setRequestMethod("POST");
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        String parametros = "email=" + URLEncoder.encode(email,"UTF-8");
        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();
        // se debe verificar si hubo error
        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
            // throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
            System.out.print("\n**El usuario NO se ha podido eliminar**\n");
        else
            System.out.print("\n**El usuario se ha eliminado con exito**\n");
        conexion.disconnect();
    }


    public static void main(String args[]) throws Exception {
        for(;;) {
            System.out.println("1-Dar de alta un usuario");
            System.out.println("2-Consultar un usuario");
            System.out.println("3-Borrar  un usuario");
            System.out.println("4-Borrar los usuarios");
            System.out.println("otro. Salir");
            System.out.print("Opcion: ");
            String opcion = teclado.nextLine();
            switch (opcion) {
                case "1":
                    alta();
                    break;
                case "2":
                    consulta();
                    break;
                case "3":
                    borrar();
                    break;
                case "4":
                    borrarTodos();
                    break;
                default:
                    System.exit(0);
                    break;
            }
        }
    }
}

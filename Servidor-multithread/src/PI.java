/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ralph
 */
import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;
class PI{
    static Object lock = new Object();
    static double pi = 0;
    
    static class Worker extends Thread{
        Socket conexion;
        Worker(Socket conexion){
            this.conexion = conexion;
        }
        @Override
        public void run(){
            // Algoritmo 1
            //Creamos try y catch
            
            try{
                //Creamos los streams de entrada y salida
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                //Recibimos la suma del cliente
                double x = entrada.readDouble();
                //bloque sincronizado de la suma
                synchronized(lock){
                    pi = x+pi;
                }
                //cerramos nuestros sockets de entrada y salida
                salida.close();
                entrada.close();
                //Cerramos la conexión
                conexion.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws Exception{
        if (args.length != 1){
            System.err.println("Uso:");
            System.err.println("java PI <nodo>");
            System.exit(0);
        }
        int nodo = Integer.valueOf(args[0]);
        if (nodo == 0){
            // Algoritmo 2
            //Declaramos servidor asignando puerto 50000
            ServerSocket servidor = new ServerSocket(50000);
            //Arreglo de Objeto Worker tamaño 3
            Worker[] w = new Worker[3];
            //Loop que limita el número de conexiones que aceptaremos
            for (int i =0;i!=3;i++){
                //Aceptamos la conexión
                Socket conexion = servidor.accept();
                //Instanciamos nuestra conexion
                w[i] = new Worker(conexion);
                //Inicializamos nuestro hilo
                w[i].start();
            }
            double suma =0;
            //Realizamos la suma para que converja al valor
            for (int i=0;i!=10000000;i++)
                suma =  4.0/(8*i+1)+ suma;
            //asignar valor de la convergencia a PI
            synchronized(lock){
                pi = suma+pi;
            }
            for(int i =0;i!=3;i++)
                //ejecutamos nuestros hilos
                w[i].join();
            System.out.println(pi);
        }else{
            // Algoritmo 3
            //En esta parte del código inicializamos nuestros clientes
            Socket conexion =null;
            //Realizamos el intento de conexión al servidor
            for(;;){
                try{
                    conexion = new Socket("localhost",50000);
                    break;
                }catch (Exception e){
                    Thread.sleep(100);
                }//fin catch
            }//fin for
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            double suma = 0;
            for(int i=0;i!=10000000;i++)
                suma =4.0/(8*i+2*(nodo-1)+3)+suma;
            suma = nodo%2==0?suma:-suma;
            salida.writeDouble(suma);
            entrada.close();
            salida.close();
            conexion.close();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mult.matrices.dist;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;
/**
 *
 * @author Ralph
 */
public class MultiplicaMatrices {
    //declaración de variables estaticas
    static int N = 1000;
    static int[][] A = new int[N][N];
    static int[][] B = new int[N][N];
    static int[][] C = new int[N][N];
    //imprime una matriz
    static void imprimirMatriz(int[][] A){
        for (int i =0;i<N;i++){
            for(int j=0;j<N;j++)
                System.out.print(Integer.toString(A[i][j]).concat(" ") );
            System.out.print("\n");
        }
    }
    //Metodo que lee el buffer
    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception{
        while(longitud > 0){
            int n = f.read(b,posicion,longitud);
            posicion +=n;
            longitud -=n;
        }
    }
    static class Worker extends Thread{
        Socket conexion;
        int nodo;
        Worker(Socket conexion){
            this.conexion = conexion;
        }
        public void run(){
            try{
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                nodo = entrada.readInt();
                System.out.println("Se conectó el nodo: ".concat(Integer.toString(nodo)));
                switch (nodo){
                    case 1:
                        
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
                //cerramos nuestros sockets y conexión
                entrada.close();
                salida.close();
                //Cerramos conexión
                conexion.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args)throws Exception{
        //Control de errores
        if (args.length !=1){
            System.err.print("Uso:");
            System.err.print("java PI <node>");
            System.exit(0);
        }
        //transformamos los argumentos
        int nodo = Integer.valueOf(args[0]);
        if (nodo == 0){
            //inicializamos las matrices
            for (int i=0;i < N; i++)
                for (int j=0; j <N ;j++){
                    A[i][j]= 2 * i + j;
                    B[i][j] = 2 * i - j;
                    C[i][j] = 0;
                }
            // Iniciamos nuestro servidor
            ServerSocket servidor = new ServerSocket(50000);
            //Calculamos Checksum de la matriz C
            
            //Despliegue de datos segun los casos
            if (N<5){
                //Desplegamos la Matriz A y B
                System.out.println("Matriz A");
                imprimirMatriz(A);
                System.out.println("Matriz B");
                imprimirMatriz(B);
                System.out.println("Matriz C= AxB");
                imprimirMatriz(C);
            }
            
            //desplegamos el checksum de la matriz C
            System.out.println("El checksum de la matriz C es:");
        }else{
            //iniciamos la conexión del lado del cliente
            Socket conexion = null;
            //Realizamos el intento de conexion hasta que el servidor
            for(;;){
                try{
                    conexion = new Socket("localhost",50000);
                    break;
                }catch(Exception e){
                    Thread.sleep(100);
                }
            }
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            salida.writeInt(nodo);
            
            System.out.println("Se logró conectar con el servidor correctamente");
            
            entrada.close();
            salida.close();
            conexion.close();
        }
    
    }
}
//

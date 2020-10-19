import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
/**
 *
 * @author Ralph
 */
public class MultiplicaMatrices{
    //declaración de variables estaticas
    static int N = 1000;
    static int[][] A;
    static int[][] B;
    static int[][] C;
    //imprime una matriz
    static void imprimirMatriz(int[][] A){
        for (int i =0;i<A.length;i++){
            for(int j=0;j<A[0].length;j++)
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
    //Metodo que multiplica 2 matrices y regresar una tercera
    static int[][] multiplicar(int[][] a, int[][] b, int[][] c){
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b.length; j++)
                for (int k = 0; k < a[0].length; k++)
                    c[i][j] += a[i][k] * b[j][k];
        return c;
    }
    static void checksum(){
        int chck = 0;
        for(int i =0;i<N;i++)
            for(int j =0;j<N;j++)
                chck += C[i][j];
        System.out.println("El Checksum es: ".concat(Integer.toString(chck)));
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
                ByteBuffer b = ByteBuffer.allocate(N*N*4);
                switch (nodo){
                    case 1:
                        //Enviar matriz A1
                        for (int i= 0;i!=N/2;i++)
                            for(int j= 0;j!=N;j++)
                                b.putInt(A[i][j]);
                        //Enviar matriz B1
                        for (int i= 0;i!=N/2;i++)
                            for(int j= 0;j!=N;j++)
                                b.putInt(B[i][j]);
                        salida.write(b.array());
                        //Recibir matriz C1
                        byte[] a1 = new byte[N*N];
                        read(entrada,a1,0,N*N);
                        ByteBuffer b1 = ByteBuffer.wrap(a1);
                        //llenar Matriz C con c1
                        for (int i= 0;i!=N/2;i++)
                            for(int j= 0;j!=N/2;j++)
                                C[i][j] = b1.getInt();
                        break;
                    case 2:
                       //Enviar matriz A1
                        for (int i= 0;i!=N/2;i++)
                            for(int j= 0;j!=N;j++)
                                b.putInt(A[i][j]);
                        //Enviar matriz B2
                        for (int i= N/2;i!=N;i++)
                            for(int j= 0;j!=N;j++)
                                b.putInt(B[i][j]);
                        salida.write(b.array());
                        //Enviar matriz C2
                        byte[] a2 = new byte[N*N];
                        read(entrada,a2,0,N*N);
                        //llenar Matriz C con c2
                        ByteBuffer b2 = ByteBuffer.wrap(a2);
                        //llenar Matriz C con c1
                        for (int i= 0;i!=N/2;i++)
                            for(int j= N/2;j!=N;j++)
                                C[i][j] = b2.getInt();

                        break;
                    case 3:
                        //Enviar matriz A2
                        for (int i= N/2;i!=N;i++)
                            for(int j= 0;j!=N;j++)
                                b.putInt(A[i][j]);
                        //Enviar matriz B1
                        for (int i= 0;i!=N/2;i++)
                            for(int j= 0;j!=N;j++)
                                b.putInt(B[i][j]);
                        salida.write(b.array());
                        //Recibir matriz C3
                        byte[] a3 = new byte[N*N];
                        read(entrada,a3,0,N*N);
                        //llenar Matriz C con c3
                        ByteBuffer b3 = ByteBuffer.wrap(a3);
                        //llenar Matriz C con c1
                        for (int i= N/2;i!=N;i++)
                            for(int j= 0;j!=N/2;j++)
                                C[i][j] = b3.getInt();
                        break;
                    case 4:
                        //Enviar matriz A2
                        for (int i= N/2;i!=N;i++)
                            for(int j= 0;j!=N;j++)
                                b.putInt(A[i][j]);
                        //Enviar matriz B2
                        for (int i= N/2;i!=N;i++)
                            for(int j= 0;j!=N;j++)
                                b.putInt(B[i][j]);
                        salida.write(b.array());
                        //Recibir matriz C4
                        byte[] a4 = new byte[N*N];
                        read(entrada,a4,0,N*N);
                        //llenar Matriz C con c4
                        ByteBuffer b4 = ByteBuffer.wrap(a4);
                        //llenar Matriz C con c1
                        for (int i= N/2;i!=N;i++)
                            for(int j= N/2;j!=N;j++)
                                C[i][j] = b4.getInt();
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

    public static void main(String[] args) throws Exception{
        //Control de errores
        if (args.length !=1){
            System.err.print("Uso:");
            System.err.print("java PI <node>");
            System.exit(0);
        }
        //inicializamos las variables solo en el servidor
        A= new int[N][N];
        B = new int[N][N];
        C = new int[N][N];
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
                // transpone la matriz B, la matriz traspuesta queda en B

            for (int i = 0; i < N; i++)
              for (int j = 0; j < i; j++)
              {
                int x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
              }
            // Iniciamos nuestro servidor
            ServerSocket servidor = new ServerSocket(50000);
            //Creamos nuestro Arreglo de Workers para nuestros clientes
            Worker[] w = new Worker[4];
            //Loop que limita el número de conexiones que aceptaremos
            for (int i =0;i!=4;i++){
                //Aceptamos la conexión
                Socket conexion = servidor.accept();
                //Instanciamos nuestra conexion
                w[i] = new Worker(conexion);
                //Inicializamos nuestro hilo
                w[i].start();
            }
            for(int i =0;i<4;i++)
                //ejecutamos nuestros hilos
                w[i].join();
            //Calculamos Checksum de la matriz C
            checksum();
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

        }else{
            //iniciamos la conexión del lado del cliente
            Socket conexion = null;
            //Inicializamos Ax Bx y Cx
            int[][] Ax = new int[N/2][N];
            int[][] Bx = new int[N/2][N];
            int[][] Cx = new int[N/2][N/2];
            for(int i=0;i!=N/2;i++)
                for(int j=0;j!=N;j++){
                    Ax[i][j] = 0;
                    Bx[i][j] = 0;
                }
            for(int i=0;i!=N/2;i++)
                for(int j=0;j!=N/2;j++)
                    Cx[i][j] = 0;
            //Realizamos el intento de conexion hasta que el servidor
            for(;;){
                try{
                    conexion = new Socket("localhost",50000);
                    break;
                }catch(Exception e){
                    Thread.sleep(100);
                }
            }
            System.out.println("Se logró conectar con el servidor correctamente");
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            salida.writeInt(nodo);

            //Recibimos Ax y Bx
            byte[] a = new byte[(N)*(N)*4];
            read(entrada,a,0,(N)*(N)*4);
            ByteBuffer b = ByteBuffer.wrap(a);
            //Dividimos el arreglo
            for (int i=0;i!=N/2;i++)
                for(int j=0;j!=N;j++)
                    Ax[i][j] = b.getInt();
            for (int i=0;i!=N/2;i++)
                for(int j=0;j!=N;j++)
                    Bx[i][j] = b.getInt();
            //multiplicamos Ax y Bx y pbtenemos Cx para enviarlo al servidor
            Cx = multiplicar(Ax,Bx,Cx);
            //Enviamos Cx al servidor
            ByteBuffer buff = ByteBuffer.allocate(N*N);
            for(int i=0;i!= N/2;i++)
                for(int j=0;j!=N/2;j++)
                    buff.putInt(Cx[i][j]);
            salida.write(buff.array());
            //Cerramos nuestro cliente
            entrada.close();
            salida.close();
            conexion.close();
        }

    }
}
//

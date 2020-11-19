import java.rmi.Naming;

public class ClienteRMI{
    static int N=4;
    static void imprimirMatriz(int[][] A){
        for (int i =0;i<A.length;i++){
            for(int j=0;j<A[0].length;j++)
                System.out.print(Integer.toString(A[i][j]).concat(" ") );
            System.out.print("\n");
        }
    }
    static int[][] parte_matriz(int[][] A,int inicio){
        int[][] M = new int[N/2][N];
        for (int i = 0; i < N/2; i++)
            for (int j = 0; j < N; j++)
                M[i][j] = A[i + inicio][j];
        return M;
    }
    static void acomoda_matriz(int[][] C,int[][] A,int renglon,int columna){
        for (int i = 0; i < N/2; i++)
            for (int j = 0; j < N/2; j++)
              C[i + renglon][j + columna] = A[i][j];
    }
    public static int[][] trasMatriz(int[][] B){
    for (int i = 0; i < N; i++)
      for (int j = 0; j < i; j++)
      {
        int x = B[i][j];
        B[i][j] = B[j][i];
        B[j][i] = x;
      }
  return B;
  }
    public static void main(String args[]) throws Exception{
        // en este caso el objeto remoto se llama "prueba", notar que se utiliza el puerto default 1099
        String url0 = "rmi://localhost:1099/objeto";
        String url1 = "rmi://13.66.25.26:1099/objeto";
        String url2 = "rmi://40.84.189.194:1099/objeto";
        String url3 = "rmi://157.55.183.181:1099/objeto";
        // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
        InterfaceRMI r0 = (InterfaceRMI)Naming.lookup(url0);
        InterfaceRMI r1 = (InterfaceRMI)Naming.lookup(url0);
        InterfaceRMI r2 = (InterfaceRMI)Naming.lookup(url0);
        InterfaceRMI r3 = (InterfaceRMI)Naming.lookup(url0);
        
        int[][] A= new int[N][N];
        int[][] B= new int[N][N];
        int[][] C= new int[N][N];
        //inicializamos las matrices
        for (int i=0;i < N; i++)
            for (int j=0; j <N ;j++){
                A[i][j]=2 * i - j;
                B[i][j] = 2 * i + j;
            }
        //Transponemos la matriz
        B = trasMatriz(B);
        int[][] A1 = parte_matriz(A,0);
        int[][] A2 = parte_matriz(A,N/2);
        int[][] B1 = parte_matriz(B,0);
        int[][] B2 = parte_matriz(B,N/2);
        
        int[][] C1 = r0.multiplica_matrices(A1,B1,N);
        int[][] C2 = r1.multiplica_matrices(A1,B2,N);
        int[][] C3 = r2.multiplica_matrices(A2,B1,N);
        int[][] C4 = r3.multiplica_matrices(A2,B2,N);
        
        acomoda_matriz(C,C1,0,0);
        acomoda_matriz(C,C2,0,N/2);
        acomoda_matriz(C,C3,N/2,0);
        acomoda_matriz(C,C4,N/2,N/2);
        
        //Checksum
        long checksum = 0;
        
        for (int x=0; x < C.length; x++) {
            for (int y=0; y < C[x].length; y++)
                checksum+=C[x][y];
        }
        imprimirMatriz(C);
        System.out.println(checksum);
  }
}

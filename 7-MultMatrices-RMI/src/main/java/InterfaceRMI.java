import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote{
    int[][] multiplica_matrices(int[][] a, int[][] b, int n)throws RemoteException;
}

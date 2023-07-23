package Singleton_Design_Pattern;
import java.sql.*;
public class SingletonDatabaseConnection {
    private static SingletonDatabaseConnection instance;
    private  Connection connection;

    private SingletonDatabaseConnection(){
        // Have a private constructor to prevent instantiation directly
        // Use the constructor to form the connection

        try{
            //Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybd", "root","");
            System.out.println("CONNECTION SUCCESSFUL");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static SingletonDatabaseConnection getInstance() {
        // check if no connection was created first
        if (instance==null){
            // use synchronized keyword to ensure that only one connection to the DB can be made thus
            // allowing us to check if the instance exists already, then instantiating if not
            // we synchro it to the class to allow only one entry to that class at a time
            synchronized (SingletonDatabaseConnection.class){
                if (instance==null)
                    // ensure that only the first thread to acquire the lock will create the instance
                    // while the subsequent threads will exit the synchronized block without creating a new instance.
                    instance = new SingletonDatabaseConnection();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}


//Once the first thread completes creating the instance of DatabaseConnection and exits the synchronized block
// one of the waiting threads will be able to acquire the lock and enter the synchronized block.
// This thread will then perform the inner if (instance == null) check.
//If the instance is still null, meaning that no other thread has created an instance in the meantime,
// the thread that acquired the lock will create a new instance. Otherwise, if the instance is not null,
// it means that another thread has already created the instance
// so the current thread will simply exit the synchronized block without creating a new instance.
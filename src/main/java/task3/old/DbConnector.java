package task3.old;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Deprecated
public class DbConnector {
    private static Connection connection;
    public static final String DB_URL = "jdbc:hsqldb:mem:users";
    public static final String DB_Driver = "org.hsqldb.jdbc.JDBCDriver";

    public static void connect() {
        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            connection = DriverManager.getConnection(DB_URL);//соединениесБД
            System.out.println("Connected.");
            ScriptRunner scriptRunner = new ScriptRunner(connection, true, true);
            try {
                FileReader reader = new FileReader("/Users/annettesimbi/IdeaProjects/MusicAdvisor/src/main/java/task3/databases/initDb.sql");
                scriptRunner.runScript(reader);
            }catch (FileNotFoundException fnfe){
                fnfe.printStackTrace();
                System.out.println("File not fund");

            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.out.println("Cant Read ScriptFile");
            }

            // connection.close();       // отключение от БД
            //  System.out.println("Отключение от СУБД выполнено.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC driver not found");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("SQL exception");
        }
    }

  /*  public static Connection getConnection() {
        connect();
        return connection;
    }*/

    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected");
            }catch (SQLException sqle){
                sqle.printStackTrace();
                System.out.println("Connection cant be closed");
            }

        }
    }
}

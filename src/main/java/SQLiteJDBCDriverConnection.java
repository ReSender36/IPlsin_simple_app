import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteJDBCDriverConnection {
    private static String DRIVER_NAME = "org.sqlite.JDBC" ;
    private static String DB_PATH = "jdbc:sqlite:D:/docs/Programming/git-repo/mdi/IPlsin_simple_app/src/main/resources/testDB.s3db" ;//?useUnicode=true&characterEncoding=UTF-8" ;

    public static Connection connect(){
        Connection conn = null ;

        try{
            Class.forName(DRIVER_NAME) ;
        } catch(ClassNotFoundException ec){
            System.out.println("Не могу найти драйвер БД");
            ec.printStackTrace();
            return null;
        }
        try{
            conn = DriverManager.getConnection(DB_PATH) ;
            System.out.println("Соединение установлено");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
//            try{
//               if (conn != null)
//                    conn.close();
//            }catch(SQLException ex){
//                System.out.println(ex.getMessage());
//            }
        }
        return conn ;
    }
    public static void disconnect(Connection conn){
        try{
            if (conn != null) {
                conn.close();
                System.out.println("Соединение закрыто");
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
}

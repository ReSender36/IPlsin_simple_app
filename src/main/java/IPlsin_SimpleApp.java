import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IPlsin_SimpleApp {
    private static String DRIVER_NAME = "org.sqlite.JDBC" ;
    private static String DB_PATH = "jdbc:sqlite:D:/docs/Programming/git-repo/mdi/IPlsin_simple_app/src/main/resources/testDB.s3db" ;

    public static void connect(){
        Connection conn = null ;

        try{
            Class.forName(DRIVER_NAME) ;
        } catch(ClassNotFoundException ec){
            System.out.println("Не могу найти драйвер БД");
            ec.printStackTrace();
            return;
        }
        try{
            conn = DriverManager.getConnection(DB_PATH) ;
            System.out.println("Соединение установлено");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            try{
                if (conn != null)
                    conn.close();
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void main(String[] args){
        connect();
    }
}

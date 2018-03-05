import java.io.UnsupportedEncodingException;
import java.sql.*;

public class Contragent {
    private static String TABLE_NAME = "CONTRAGENTS" ;
    private int Id ;
    private String Name ;
    private double Balance ;

    public Contragent(int id, String name, double balance) {
        Id = id;
        Name = name;
        Balance = balance;
    }

    public Contragent(int id) {
        SQLiteJDBCDriverConnection c = new SQLiteJDBCDriverConnection();
        Connection connect = c.connect();
        String query = "SELECT NAME, BALANCE FROM CONTRAGENTS where ID = " + id ;
        String strName = "" ;
        double balance = 0 ;
        try{
            Statement stmt = connect.createStatement() ;
            ResultSet rs = stmt.executeQuery(query) ;

            while (rs.next()) {
                strName = new String(rs.getString("NAME").getBytes("ISO-8859-1"), "cp1251");
                balance = rs.getDouble("BALANCE") ;
                break;
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
        }finally {
            if (null != connect)
                try {
                    connect.close();
                }catch (SQLException eSQL){
                    eSQL.printStackTrace();
                }
        }

        this.Id = id;
        this.Name = strName ;
        this.Balance = balance ;
    }

    public int getId() {
        return Id;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public boolean increaseBalance(int Id, double sumDecr){
        boolean bResult = false ;
        double currBalance = 0 ;
        SQLiteJDBCDriverConnection c = new SQLiteJDBCDriverConnection();
        Connection connect = c.connect();
        String sqlComm = "UPDATE CONTRAGENTS SET BALANCE =  ? WHERE ID = ? ;" ;
        try {
            PreparedStatement pstmt = connect.prepareStatement(sqlComm);
            currBalance = this.Balance + sumDecr ;
            pstmt.setDouble(1, currBalance);
            pstmt.setInt(2, Id) ;
            setBalance(currBalance);
            pstmt.executeUpdate();
            bResult = true ;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return bResult ;
    }

    public boolean decreaseBalance(int Id, double sumDecr){
        boolean bResult = false ;
        double currBalance = 0 ;
        SQLiteJDBCDriverConnection c = new SQLiteJDBCDriverConnection();
        Connection connect = c.connect();
        String sqlComm = "UPDATE CONTRAGENTS SET BALANCE =  ? WHERE ID = ? ;" ;
        try {
            PreparedStatement pstmt = connect.prepareStatement(sqlComm);
            currBalance = this.Balance - sumDecr ;
            pstmt.setDouble(1, currBalance);
            pstmt.setInt(2, Id) ;
            setBalance(currBalance);
            pstmt.executeUpdate();
            bResult = true ;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return bResult ;
    }
    public void getStatstic(String fromDate, String toDate){
        Id = getId() ;
        SQLiteJDBCDriverConnection c = new SQLiteJDBCDriverConnection();
        Connection connect = c.connect();
        String query = "select Q1.DOC_TYPE_CODE AS TYPE_CODE, DOC_TYPE, Q1.DATE_DOC, Q1.CLIENT_CODE, C.NAME, Q1.CLIENT_CODE_ACCEPTOR, C1.NAME AS NAME_ACCEPTOR, Q1.OPER_SUM " +
                "FROM (select 1 as DOC_TYPE_CODE, DOC_DATE AS DATE_DOC, CONTR AS CLIENT_CODE, null AS CLIENT_CODE_ACCEPTOR, SUM_EXPENCE AS OPER_SUM " +
                "FROM DOC_EXPENSE " +
                "UNION " +
                "select 2, DOC_DATE, CONTR, null, SUM_INCOME " +
                "FROM DOC_INCOME " +
                "UNION " +
                "select 3, DOC_DATE, CONTR, CONTR_ACCEPTOR, SUM_TRANSFER " +
                "FROM DOC_TRANSFER) AS Q1 " +
                "INNER JOIN KIND_DOC_TYPES ON KIND = Q1.DOC_TYPE_CODE " +
                "INNER JOIN CONTRAGENTS As C ON C.ID = Q1.CLIENT_CODE " +
                "LEFT JOIN CONTRAGENTS AS C1 ON C1.ID = Q1.CLIENT_CODE_ACCEPTOR " +
                "where Q1.DATE_DOC BETWEEN '" + fromDate + "' AND '" + toDate + "' AND Q1.CLIENT_CODE = "+ String.valueOf(Id) + " " +
                "ORDER BY Q1.DATE_DOC ;" ;

        Statement stmt ;
        ResultSet rs ;
        try{
            stmt = connect.createStatement();
            rs = stmt.executeQuery(query) ;
            System.out.println("Тип движения    Дата движения   Контрагент  Контрагент-Получатель   Сумма");

            while (rs.next()) {
                String strng = new String(rs.getString("DOC_TYPE").getBytes("ISO-8859-1"), "cp1251");
                String strDatedoc = new String(rs.getString("DATE_DOC").getBytes("ISO-8859-1"), "cp1251");
                String strName = new String(rs.getString("NAME").getBytes("ISO-8859-1"), "cp1251");
                String str = rs.getString("NAME_ACCEPTOR") ;
                String strNameAcceptor = "" ;
                if (null != str)
                    strNameAcceptor = new String(str.getBytes("ISO-8859-1"), "cp1251");
                else
                    strNameAcceptor = "" ;
                String strOperSum = new String(rs.getString("OPER_SUM").getBytes("ISO-8859-1"), "cp1251");
                System.out.println(strng + "    " + strDatedoc + "  " + strName + " " + strNameAcceptor + " " + strOperSum);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {

        }
    }
}

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class SimpleDoc {
    String TABLE_NAME ;
    int docType ;
    String DocNum ;
    String DocDate ;
    Contragent contr ;
    double sumOper ;

    public SimpleDoc(int docType, String docNum, String docDate, Contragent contr, double sumOper) {
        if (1 == docType)
            this.TABLE_NAME = "DOC_INCOME";
        else{
            if (2 == docType)
                this.TABLE_NAME = "DOC_EXPENSE";
            else
                this.TABLE_NAME = "DOC_TRANSFER";
        }
        this.docType = docType;
        this.DocNum = docNum;
        this.DocDate = docDate ;
        this.contr = contr;
        this.sumOper = sumOper;
    }

    public SimpleDoc() {
        this.TABLE_NAME = "";
        this.docType = 0;
        this.DocNum = "";
        this.DocDate = null;
        this.contr = contr;
        this.sumOper = 0.0;
    }

    public boolean goOper(){
        boolean result = false ;

//        String contrId = String.valueOf(this.contr.getId()) ;

        int contrId = this.contr.getId() ;

//        System.out.println(sqlComm);
        SQLiteJDBCDriverConnection c = new SQLiteJDBCDriverConnection();
        Connection connect = c.connect();
        try{
                if (1 == docType){
                    String sqlComm = "INSERT INTO " + this.TABLE_NAME + "(DOC_NUMBER, DOC_DATE,CONTR,SUM_INCOME) VALUES(?, '" + this.DocDate + "',?,?);" ;
                    PreparedStatement pstmt = connect.prepareStatement(sqlComm) ;
                    pstmt.setString(1, this.DocNum);
                    pstmt.setInt(2, contrId);
                    pstmt.setDouble(3, this.sumOper);

                    pstmt.executeUpdate() ;

                    if (contr.increaseBalance(contrId, this.sumOper))
                        result = true ;
                }
                if (2 == docType){
                    String sqlComm = "INSERT INTO " + this.TABLE_NAME + "(DOC_NUMBER, DOC_DATE,CONTR,SUM_EXPENCE) VALUES(?, '" + this.DocDate + "',?,?);" ;
                    PreparedStatement pstmt = connect.prepareStatement(sqlComm) ;
                    pstmt.setString(1, this.DocNum);
                    pstmt.setInt(2, contrId);
                    pstmt.setDouble(3, this.sumOper);

                    pstmt.executeUpdate() ;
                    if (contr.decreaseBalance(contrId, this.sumOper))
                        result = true ;
                }
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return result ;
    }
}

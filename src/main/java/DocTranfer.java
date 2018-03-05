import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DocTranfer extends SimpleDoc {
    Contragent contrAcceptor ;

    public DocTranfer() {
        this.contrAcceptor = contrAcceptor;

    }
    public DocTranfer(int docType, String docNum, String docDate, Contragent contr, double sumOper, Contragent contrAcceptor) {
        super(docType, docNum, docDate, contr, sumOper);
        this.TABLE_NAME = "DOC_TRANSFER" ;
        this.contrAcceptor = contrAcceptor;
    }
    public boolean goOper(){
        boolean bResult = false ;
        SQLiteJDBCDriverConnection c = new SQLiteJDBCDriverConnection();
        Connection connect = c.connect();
        int contrId = this.contr.getId() ;
        int contrAcceptorId = this.contrAcceptor.getId() ;
        try{
            String sqlComm = "INSERT INTO " + this.TABLE_NAME + "(DOC_NUMBER, DOC_DATE,CONTR,CONTR_ACCEPTOR,SUM_TRANSFER) VALUES(?, '" + this.DocDate + "',?,?,?);" ;
            PreparedStatement pstmt = connect.prepareStatement(sqlComm) ;
            pstmt.setString(1, this.DocNum);
            pstmt.setInt(2, contrId);
            pstmt.setInt(3, contrAcceptorId);
            pstmt.setDouble(4, this.sumOper);

            pstmt.executeUpdate() ;

            if (contr.decreaseBalance(contrId, this.sumOper) && contr.increaseBalance(contrAcceptorId, this.sumOper)){
                bResult = true ;
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return bResult ;
    }

}

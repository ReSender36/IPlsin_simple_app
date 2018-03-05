import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class IPlsin_SimpleApp {


    public static void main(String[] args) throws ParseException {
        IPlsin_SimpleApp ipls = new IPlsin_SimpleApp() ;
        ipls.go();
    }

    public void go(){
        System.out.println("Cистема управления абонентами. Выберите пункт меню:");
        System.out.println("1. Проведение начисления на лицевой счет клиента");
        System.out.println("2. Проведение списания на лицевой счет клиента");
        System.out.println("3. Проведение перемещения средств между лицевыми счетами клиентов");
        System.out.println("4. Получение статистики по движению средств на лицевом счете клиента");
        System.out.println("9. Выход");

        Scanner in = new Scanner(System.in);
        System.out.println("Введите ваш выбор:");
        short sel = in.nextShort() ;

        System.out.println("Ваш выбор: " + sel);

        SQLiteJDBCDriverConnection c = new SQLiteJDBCDriverConnection();
        Connection connect = c.connect();

//        if (null == connect)
//            return ;
        int docType = 0 ;

        switch (sel){
            case 1:{
                System.out.println("Проведение начисления на лицевой счет клиента");
                docType = 1 ;
                String query = "SELECT TABLE_NAME FROM KIND_DOC_TYPES where KIND = " + String.valueOf(docType) + " ;" ;
                int docNo = 0 ;
                String strDocNo = "" ;
                try {
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query) ;

                    while (rs.next()) {
                        String strng = new String(rs.getString("TABLE_NAME").getBytes("ISO-8859-1"), "cp1251");
                        query = "SELECT max(DOC_ID) AS MAX_DOC_NUM FROM " + strng ;
                        break;
                    }
                    stmt = connect.createStatement() ;
                    rs = stmt.executeQuery(query) ;
                    while (rs.next()) {
                        docNo = rs.getInt("MAX_DOC_NUM");
                        docNo++ ;
                        System.out.println("номер " + docNo);
                        strDocNo = String.valueOf(docNo) ;

                        query = "SELECT ID, NAME FROM CONTRAGENTS" ;
                        break;
                    }
                    stmt = connect.createStatement() ;
                    rs = stmt.executeQuery(query) ;
                    System.out.println("Укажите код нужного контрагента:");
                    while (rs.next()) {
                        String contrId = rs.getString("ID") ;
                        String contrName = new String(rs.getString("NAME").getBytes("ISO-8859-1"), "cp1251");
                        System.out.println(contrId + " " + contrName);

                    }
                } catch (SQLException ex){
                    ex.printStackTrace();
                } catch (UnsupportedEncodingException ex) {
                }finally {
                    c.disconnect(connect);
                }
                System.out.print("Код: ");
                short selContrId = in.nextShort() ;
                Contragent contr = new Contragent(selContrId) ;

                Date date = new Date() ;
//                SimpleDateFormat formatForCurrDate = new SimpleDateFormat("dd.MM.YYYY") ;
                SimpleDateFormat formatForCurrDate = new SimpleDateFormat("YYYY-MM-dd") ;
                String docDate = formatForCurrDate.format(date);
//                docDate = "'" + docDate + "'" ;
                System.out.println(docDate);

                System.out.println("Введите сумму начисления:");
                double sumOper = in.nextDouble() ;
                SimpleDoc docIncome = new SimpleDoc(docType, strDocNo, docDate, contr, sumOper) ;
                if (docIncome.goOper()){
                    System.out.println("Документ поступления записан успешно");
                }

                c.disconnect(connect) ;

                go();
                break;
            }
            case 2:{
                System.out.println("Проведение списания с лицевого счета клиента");
                docType = 2 ;
                String query = "SELECT TABLE_NAME FROM KIND_DOC_TYPES where KIND = " + String.valueOf(docType) + " ;" ;
                int docNo = 0 ;
                String strDocNo = "" ;
                Statement stmt ;
                ResultSet rs ;
                try {
                    stmt = connect.createStatement();
                    rs = stmt.executeQuery(query) ;

                    while (rs.next()) {
                        String strng = new String(rs.getString("TABLE_NAME").getBytes("ISO-8859-1"), "cp1251");
                        query = "SELECT max(DOC_ID) AS MAX_DOC_NUM FROM " + strng ;
                        System.out.println(strng);
                        break;
                    }
                    stmt = connect.createStatement() ;
                    rs = stmt.executeQuery(query) ;
                    while (rs.next()) {
                        docNo = rs.getInt("MAX_DOC_NUM");
                        docNo++ ;
                        System.out.println("номер " + docNo);
                        strDocNo = String.valueOf(docNo) ;

                        query = "SELECT ID, NAME FROM CONTRAGENTS" ;
                        break;
                    }
                    stmt = connect.createStatement() ;
                    rs = stmt.executeQuery(query) ;
                    System.out.println("Укажите код контрагента, у которого будут списываться средства:");
                    while (rs.next()) {
                        String contrId = rs.getString("ID") ;
                        String contrName = new String(rs.getString("NAME").getBytes("ISO-8859-1"), "cp1251");
                        System.out.println(contrId + " " + contrName);

                    }
                } catch (SQLException ex){
                    ex.printStackTrace();
                } catch (UnsupportedEncodingException ex) {
                }finally {
                    c.disconnect(connect);
                }
                System.out.print("Код клиента-источника: ");
                short selContrId = in.nextShort() ;
                Contragent contr = new Contragent(selContrId) ;

                Date date = new Date() ;
//                SimpleDateFormat formatForCurrDate = new SimpleDateFormat("dd.MM.YYYY") ;
                SimpleDateFormat formatForCurrDate = new SimpleDateFormat("YYYY-MM-dd") ;
                String docDate = formatForCurrDate.format(date);
//                docDate = "'" + docDate + "'" ;
                System.out.println(docDate);

                System.out.println("Введите сумму списания:");
                double sumOper = in.nextDouble() ;
                SimpleDoc docExpence = new SimpleDoc(docType, strDocNo, docDate, contr, sumOper) ;
                if (docExpence.goOper()){
                    System.out.println("Документ списания записан успешно");
                }

                c.disconnect(connect) ;

                go();
                break;
            }
            case 3:{
                System.out.println("Проведение списания с лицевого счета клиента");
                docType = 3 ;
                String query = "SELECT TABLE_NAME FROM KIND_DOC_TYPES where KIND = " + String.valueOf(docType) + " ;" ;
                int docNo = 0 ;
                String strDocNo = "" ;
                Statement stmt ;
                ResultSet rs ;
                try {
                    stmt = connect.createStatement();
                    rs = stmt.executeQuery(query) ;

                    while (rs.next()) {
                        String strng = new String(rs.getString("TABLE_NAME").getBytes("ISO-8859-1"), "cp1251");
                        query = "SELECT max(DOC_ID) AS MAX_DOC_NUM FROM " + strng ;
                        System.out.println(strng);
                        break;
                    }
                    stmt = connect.createStatement() ;
                    rs = stmt.executeQuery(query) ;
                    while (rs.next()) {
                        docNo = rs.getInt("MAX_DOC_NUM");
                        docNo++ ;
                        System.out.println("номер " + docNo);
                        strDocNo = String.valueOf(docNo) ;

                        query = "SELECT ID, NAME FROM CONTRAGENTS" ;
                        break;
                    }
                    stmt = connect.createStatement() ;
                    rs = stmt.executeQuery(query) ;
                    System.out.println("Укажите код контрагента, с которого будут списываться средства:");
                    while (rs.next()) {
                        String contrId = rs.getString("ID") ;
                        String contrName = new String(rs.getString("NAME").getBytes("ISO-8859-1"), "cp1251");
                        System.out.println(contrId + " " + contrName);

                    }
                } catch (SQLException ex){
                    ex.printStackTrace();
                } catch (UnsupportedEncodingException ex) {
                }
                System.out.print("Код клиента-источника: ");
                short selContrId = in.nextShort() ;
                Contragent contr = new Contragent(selContrId) ;

                try{
                    stmt = connect.createStatement() ;
                    rs = stmt.executeQuery(query) ;
                    System.out.println("Укажите код контрагента, которому будут начисляться  средства:");
                    while (rs.next()) {
                        String contrId = rs.getString("ID") ;
                        String contrName = new String(rs.getString("NAME").getBytes("ISO-8859-1"), "cp1251");
                        System.out.println(contrId + " " + contrName);

                    }
                } catch (SQLException ex){
                    ex.printStackTrace();
                } catch (UnsupportedEncodingException ex) {
                }finally {
                    c.disconnect(connect);
                }
                System.out.print("Код клиента-получателя: ");
                short selContrAccId = in.nextShort() ;
                Contragent contrAcceptor = new Contragent(selContrAccId) ;

                Date date = new Date() ;
//                SimpleDateFormat formatForCurrDate = new SimpleDateFormat("dd.MM.YYYY") ;
                SimpleDateFormat formatForCurrDate = new SimpleDateFormat("YYYY-MM-dd") ;
                String docDate = formatForCurrDate.format(date);
//                docDate = "'" + docDate + "'" ;
                System.out.println(docDate);

                System.out.println("Введите перемещаемую сумму:");
                double sumOper = in.nextDouble() ;
                DocTranfer docTranfer = new DocTranfer(docType, strDocNo, docDate, contr, sumOper, contrAcceptor) ;
                if (docTranfer.goOper()){
                    System.out.println("Документ перемещения записан успешно");
                }

                c.disconnect(connect) ;

                go();
                break;
            }
            case 4:{
                System.out.println("Получение статистики по коду клиента");
                String query = "SELECT ID, NAME FROM CONTRAGENTS ;" ;
                int docNo = 0 ;
                String strDocNo = "" ;
                Statement stmt ;
                ResultSet rs ;
                try {
                    stmt = connect.createStatement();
                    rs = stmt.executeQuery(query) ;

                    System.out.println("Укажите код контрагента, по корому надо вывести статистику:");
                    while (rs.next()) {
                        String contrId = rs.getString("ID") ;
                        String contrName = new String(rs.getString("NAME").getBytes("ISO-8859-1"), "cp1251");
                        System.out.println(contrId + " " + contrName);

                    }
                } catch (SQLException ex){
                    ex.printStackTrace();
                } catch (UnsupportedEncodingException ex) {
                }finally {
                    c.disconnect(connect);
                }
                System.out.print("Код клиента: ");
                short selContrId = in.nextShort() ;
                Contragent contr = new Contragent(selContrId) ;

                System.out.println("Введите начальное число");
                String dateFrom = in.next() ;

                System.out.println("Введите конечное число");
                String dateTo = in.next() ;

                contr.getStatstic(dateFrom, dateTo);

                c.disconnect(connect) ;

                go();
                break;
            }
            case 9:{
                System.out.println("Выходим...");
//                c.disconnect(connect) ;
                break;
            }
        }

    }
}

import oracle.sql.BLOB;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        String WRITE_OBJECT_SQL = "Begin" +
                " INSERT INTO java_objects(object_id, object_name, object_value)" +
                " VALUES(?,?,empty_blob()) " + " RETURN object_value INTO ?; " + "END;";
        String READ_OBJECT_SQL = "SELECT object_value FROM java_objects WHERE object_id = ?";
        Connection conn = getOracleConnection();
        conn.setAutoCommit(false);
        List<Object> list = new ArrayList<Object>();
        list.add("This is another short string");
        list.add(1234);
        list.add(new java.util.Date());
        /*
         * WRITE
         */
        long id = 0002;
        String className = list.getClass().getName();
        CallableStatement cstmt = conn.prepareCall(WRITE_OBJECT_SQL);

        cstmt.setLong(1, id);
        cstmt.setString(2, className);
        cstmt.registerOutParameter(3, Types.BLOB);

        cstmt.executeUpdate();
        BLOB blob = (BLOB) cstmt.getBlob(3);
        OutputStream os = blob.getBinaryOutputStream();
        ObjectOutputStream oop = new ObjectOutputStream(os);
        oop.writeObject(list);
        oop.flush();
        oop.close();
        os.close();

        /*
         * READ
         */

        PreparedStatement pstmt = conn.prepareStatement(READ_OBJECT_SQL);
        pstmt.setLong(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        InputStream is = rs.getBlob(1).getBinaryStream();
        ObjectInputStream oip = new ObjectInputStream(is);
        Object object = oip.readObject();
        className = object.getClass().getName();
        oip.close();
        is.close();
        rs.close();
        pstmt.close();
        conn.commit();

        /*
        Deserialize list a java object from a given object id
         */
        List listFromDatabase = (List) object;
        System.out.println("[AFTER DE-SERIALIZATION] list=" + listFromDatabase);
        conn.close();
    }
        private static Connection getOracleConnection()throws Exception{
            String driver = "oracle.jdbc.driver.OracleDriver";
            String url = "jdbc:oracle:thin:@localhost:1521:XE";
            String userName = "STUDENT";
            String password = "STUDENT";

            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, userName, password);
            return conn;
        }

}

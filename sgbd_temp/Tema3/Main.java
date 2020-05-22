import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static Database db;

    public static void main(String[] args) {
        db = Database.getInstance();
        Scanner scan = new Scanner(System.in);
        int id=scan.nextInt();
        try {
            PreparedStatement statement = db.connection.prepareStatement("SELECT FriendRecommendation(?) FROM DUAL");
            statement.setInt(1,id);
            ResultSet result=statement.executeQuery();
            while(result.next()){
                String output = result.getString(1);
                System.out.println(output);
            }
        }catch (SQLException e){
            System.out.println(e);
        }

    }
}

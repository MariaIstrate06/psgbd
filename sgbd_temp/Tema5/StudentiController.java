import java.sql.SQLException;
import java.sql.Statement;

public class StudentiController {
    Database database;

    public StudentiController() {
        database = Database.getInstance();
    }

    public void create(String nume, String prenume) {
        try {
            Statement statement = database.connection.createStatement();
            String query =
                    String.format("INSERT INTO studenti (name, prenume) VALUES ('%s', '%s')", nume, prenume);
            int rowsInserted = statement.executeUpdate(query);
            System.out.println(String.format("Number of rows inserted: %d", rowsInserted));
        } catch (SQLException e) {
            System.out.println("Exceptie SQL: "+e);
        }
    }
}

import database.DatabaseInitializer;
import database.DatabaseLoader;

public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.initializeDatabase();

        DatabaseLoader.loadAll();

        System.out.println("Database Ready.");

    }

}
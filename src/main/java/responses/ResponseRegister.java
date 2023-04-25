package responses;

public class ResponseRegister {
    //TODO ResponseRegister
    public boolean success = false;
    public String description = "Error occurred! Please try again.";
    public String password;

    /*
    static Connection connection;

    public void DbConection() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:file:mem:testdb");
        ds.setUser("sa");
        ds.setPassword("pass");
        try {
            connection = ds.getConnection();
        } catch (Exception e) {
            System.err.println("IOException: " + e.getMessage());
        } finally {
            System.out.println("Spojen");
        }
    }
     */
}

/**
 * This code is the JDBC class that process the connection and actions on the PostgreSQL DB.

 *
 * @version 2021.04.09
 */
package sample;

import javafx.collections.ObservableList;

import java.sql.*;

public class JDBCPostgreSQLConnect {
    // instance variables:
    // JDBC URL
    // JDBC user
    // JDBC password
    private final String url ="jdbc:postgresql://192.168.0.175:5432/cdb";
    private final String user= "postgres";
    private final String password= "Aa123456";
    /**
     * Test connect to sql DB
     *
     */
    public void connect(){
        try (Connection connection = DriverManager.getConnection(url,user,password);){ // open connection
            //Connection connection = DriverManager.getConnection(url,user,password);
            if (connection != null){
                System.out.println("connected to PostgreSQL server successfully!");
            }
            else {
                System.out.println("failed to connect PostgreSQL server");
            }
            Statement statement1 = connection.createStatement();
            ResultSet resultSet = statement1.executeQuery("SELECT VERSION()");
            if (resultSet.next()){
                //System.out.println(resultSet.getString(1)); // print the SQL version
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print the hole DB records parameters separated by a comma
     *
     */
    public void printDB(){
        try (Connection connection = DriverManager.getConnection(url,user,password);) { // open connection
            Statement statement1 = connection.createStatement();

            ResultSet resultSet1 = statement1.executeQuery("SELECT * FROM players");
            //System.out.println("players Table:");
            // loop over the rows and print them
            while (resultSet1.next()) {
                int id = resultSet1.getInt("id");
                String first_name = resultSet1.getString("first_name");
                String last_name = resultSet1.getString("last_name");
                String user_name = resultSet1.getString("user_name");
                String user_pass = resultSet1.getString("user_pass");
                int games_won = resultSet1.getInt("games_won");
                int games_played = resultSet1.getInt("games_played");

                //System.out.println(id + "," + first_name + "," + last_name + "," + user_name + "," + user_pass
                        //+ "," + games_won + "," + games_played);

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println();
    }
    /**
     * update an observable list of all players
     *
     */
    public void updateList(ObservableList<TableRow>list){
        try (Connection connection = DriverManager.getConnection(url,user,password);) { // open connection
            Statement statement1 = connection.createStatement();

            ResultSet resultSet1 = statement1.executeQuery("SELECT * FROM players");
            //System.out.println("players Table:");
            // loop over the rows and print them
            while (resultSet1.next()) {
                int id = resultSet1.getInt("id");
                String first_name = resultSet1.getString("first_name");
                String last_name = resultSet1.getString("last_name");
                String user_name = resultSet1.getString("user_name");
                String user_pass = resultSet1.getString("user_pass");
                int games_won = resultSet1.getInt("games_won");
                int games_played = resultSet1.getInt("games_played");
                list.add(new TableRow(user_name,first_name,last_name,games_won,games_played));
                //System.out.println(id + "," + first_name + "," + last_name + "," + user_name + "," + user_pass
                //+ "," + games_won + "," + games_played);

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println();
    }
    /**
     * Insert a record by all it's values
     *
     * @param   sFirst_name   First name.
     * @param   sLast_name   Last name.
     * @param   sUser_name   User name.
     * @param   sUser_pass   User password.
     * @param   sGames_won   Games user won.
     * @param   sGames_played   Games user played.
     */
    public void insertByValue(String sFirst_name,String sLast_name,String sUser_name,String sUser_pass,int sGames_won,int sGames_played) {
        try (Connection connection = DriverManager.getConnection(url,user,password);) { // open connection
            //insert statement
            String insert = "INSERT INTO players"
                    +"(first_name,last_name,user_name,user_pass,games_won,games_played) VALUES "
                    +"(?,?,?,?,?,?);";
            PreparedStatement insertStatement = connection.prepareStatement(insert);

            insertStatement.setString(1,sFirst_name);
            insertStatement.setString(2,sLast_name);
            insertStatement.setString(3,sUser_name);
            insertStatement.setString(4,sUser_pass);
            insertStatement.setInt(5,sGames_won);
            insertStatement.setInt(6,sGames_played);
            insertStatement.executeUpdate();
            //System.out.println("Insert record Completed. ");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Update the number of played games by a user name
     *
     * @param   sUser_name   User name.
     * @param   sGames_played   Games user played.
     */
    public void updateGamesPlayed(String sUser_name, int sGames_played) {
        try (Connection connection = DriverManager.getConnection(url,user,password);) {
            //update statement

            PreparedStatement updateStatment = connection.prepareStatement("UPDATE players SET games_played = ? WHERE user_name = ?");
            updateStatment.setInt(1,sGames_played);
            updateStatment.setString(2,sUser_name);
            updateStatment.executeUpdate();
            //System.out.println("Update game_played Completed. ");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Update the number of won games by a user name
     *
     * @param   sUser_name   User name.
     * @param   sGames_won   Games user won.
     */
    public void updateGamesWon(String sUser_name, int sGames_won) {
        try (Connection connection = DriverManager.getConnection(url,user,password);) {
            //update statement
            PreparedStatement updateStatment = connection.prepareStatement("UPDATE players SET games_won = ? WHERE user_name = ?");
            updateStatment.setInt(1,sGames_won);
            updateStatment.setString(2,sUser_name);
            updateStatment.executeUpdate();
            //System.out.println("Update game_won Completed. ");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Delete a record by it's first name and last name
     *
     * @param   sFirst_name   First name.
     * @param   sLast_name   Last name.
     */
    public void deleteByFullName(String sFirst_name,String  sLast_name) {
        try (Connection connection = DriverManager.getConnection(url,user,password);){
            //delete statement

            PreparedStatement deleteStatment = connection.prepareStatement("DELETE FROM players  WHERE first_name = ? AND last_name = ?");
            deleteStatment.setString(1,sFirst_name);
            deleteStatment.setString(2,sLast_name);

            int rowAffected = deleteStatment.executeUpdate();
            //System.out.println("Update Completed. rows deleted: " + rowAffected);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Delete a record by it's user name
     *
     * @param   sUser_name   First name.
     */
    public void deleteByUser(String sUser_name) {
        try (Connection connection = DriverManager.getConnection(url,user,password);){
            //delete statement

            PreparedStatement deleteStatment = connection.prepareStatement("DELETE FROM players  WHERE user_name = ?");
            deleteStatment.setString(1,sUser_name);

            int rowAffected = deleteStatment.executeUpdate();
            //System.out.println("Update Completed. rows deleted: " + rowAffected);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Check if user login cedentials by it's user and password
     *
     * @param   sUser_name   User name.
     * @param   sUser_pass   User password.
     * @return  boolean      true if this is currect credentials; false otherwise.
     */
    public boolean searchLogin(String sUser_name,String sUser_pass){
        boolean found = false;
        String foundUser = "";
        String foundPass = "";
        try (Connection connection = DriverManager.getConnection(url,user,password);) {
            //search statement

            PreparedStatement searchPlayerStat = connection.prepareStatement("SELECT id,first_name,last_name,user_name,user_pass,games_won,games_played FROM players WHERE user_name = ? AND user_pass = ?");
            searchPlayerStat.setString(1,sUser_name); //first name
            searchPlayerStat.setString(2,sUser_pass); //last name
            ResultSet resultSet = searchPlayerStat.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String user_name = resultSet.getString("user_name");
                String user_pass = resultSet.getString("user_pass");
                int games_won = resultSet.getInt("games_won");
                int games_played = resultSet.getInt("games_played");
                foundUser = user_name;
                foundPass = user_pass;
                //System.out.println("found record: "+id+","+first_name+","+last_name+","+user_name+","+user_pass+","+games_won+","+games_played);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        if (foundUser.equals(sUser_name)  && foundPass .equals(sUser_pass) ){
            //System.out.println("search found login. " + foundUser);
            found = true;
        }
        return found;
    }
    /**
     * Check if user is already being used
     *
     * @param   sUser_name   User name.
     * @return  boolean      true if this user in use; false otherwise.
     */
    public boolean isUserExist(String sUser_name){
        boolean found = false;
        String foundUser = "";
        try (Connection connection = DriverManager.getConnection(url,user,password);) {
            //search statement
            PreparedStatement searchPlayerStat = connection.prepareStatement("SELECT user_name FROM players WHERE user_name = ? ");
            searchPlayerStat.setString(1,sUser_name); //first name
            ResultSet resultSet = searchPlayerStat.executeQuery();
            while (resultSet.next()){
                String user_name = resultSet.getString("user_name");
                foundUser = user_name;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        if (foundUser.equals(sUser_name)){
            //System.out.println("search found user: "+foundUser);
            found = true;
        }
        return found;
    }

    public int getNumOfGames(String sUser_name){
        boolean found = false;
        int games_played = 0;
        try (Connection connection = DriverManager.getConnection(url,user,password);) {
            //search statement
            PreparedStatement searchPlayerStat = connection.prepareStatement("SELECT games_played FROM players WHERE user_name = ? ");
            searchPlayerStat.setString(1,sUser_name); //first name
            ResultSet resultSet = searchPlayerStat.executeQuery();
            while (resultSet.next()){
                games_played = resultSet.getInt("games_played");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return games_played;
    }

    public int getNumOfWins(String sUser_name){
        boolean found = false;
        int games_played = 0;
        try (Connection connection = DriverManager.getConnection(url,user,password);) {
            //search statement
            PreparedStatement searchPlayerStat = connection.prepareStatement("SELECT games_won FROM players WHERE user_name = ? ");
            searchPlayerStat.setString(1,sUser_name); //first name
            ResultSet resultSet = searchPlayerStat.executeQuery();
            while (resultSet.next()){
                games_played = resultSet.getInt("games_won");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return games_played;
    }


    // this is the main program
    public static void main(String[] args) {
        JDBCPostgreSQLConnect sqlConnect = new JDBCPostgreSQLConnect(); // crate instence of the class in order to user it's methods
        sqlConnect.connect();
        sqlConnect.insertByValue("Beni","Fridman","benifr","123456",2,4);
        sqlConnect.insertByValue("Dani","Rotenberg","spike123","password",10,100);
        //sqlConnect.updateGamesPlayed("benifr",1);
        //sqlConnect.updateGamesWon("benifr",1);
        System.out.println("game played Beni "+ sqlConnect.getNumOfGames("benifr"));
        System.out.println("game played Beni "+ sqlConnect.getNumOfGames("spike123"));
        System.out.println("game won Beni "+ sqlConnect.getNumOfWins("benifr"));
        System.out.println("game won Beni "+ sqlConnect.getNumOfWins("spike123"));
        System.out.println("return if login found: " + sqlConnect.searchLogin("benifr","123456"));
        System.out.println("return if login found: " + sqlConnect.searchLogin("benifr","1234567"));
        sqlConnect.deleteByFullName("Beni", "Fridman");
        //sqlConnect.insert();
        //sqlConnect.update();
        //sqlConnect.search();
        //sqlConnect.delete();
        sqlConnect.printDB();
        System.out.println("return if user found: " +sqlConnect.isUserExist("spike123"));
        sqlConnect.deleteByUser("spike123");
        sqlConnect.printDB();
         

    }
}

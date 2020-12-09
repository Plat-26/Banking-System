package banking;

import org.sqlite.SQLiteDataSource;
import java.sql.*;

class BankData {

    SQLiteDataSource dataSource;

    BankData(SQLiteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    void createTable() {

        String createTable = "CREATE TABLE IF NOT EXISTS card(" +
                "    id INTEGER AUTO_INCREMENT," +
                "    number TEXT," +
                "    pin TEXT," +
                "    balance INTEGER DEFAULT 0" +
                ");";

        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement preparedSt = connection.prepareStatement(createTable)) {

                preparedSt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    boolean addNewAccount(String cardNumber, String userPin) {

        int i = -1;

        String updateTable = "INSERT INTO card (number, pin) VALUES (?, ?);";
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement preparedSt = connection.prepareStatement(updateTable)) {

                preparedSt.setString(1, cardNumber);
                preparedSt.setString(2, userPin);

                i = preparedSt.executeUpdate();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return i == 1;
    }

    void userTransfer(String senderNumber, String senderPin, String recipientNumber, int amt) {

        String selectAccountSQL = "SELECT balance FROM card WHERE number = ? AND pin = ?;" ;
        String updateSenderAccountSQL = "UPDATE card SET balance = balance - ? WHERE number = ? AND pin = ?;";
        String updateRecipientAccountSQL = "UPDATE card SET balance = balance + ? WHERE number = ?;";
        int senderBalance = 0;


        //TODO: CHECK IF RECIPIENT ACCOUNT IS VALID
        //TODO: CHECK IF SENDER HAS ENOUGH MONEY
        //TODO: SEND MONEY AND VALIDATE TRANSACTION

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement selectAccount = con.prepareStatement(selectAccountSQL);
                 PreparedStatement updateSenderAccount = con.prepareStatement(updateSenderAccountSQL);
                 PreparedStatement updateRecipientAccount = con.prepareStatement(updateRecipientAccountSQL)) {


                selectAccount.setString(1, senderNumber);
                selectAccount.setString(2, senderPin);
                try (ResultSet senderData = selectAccount.executeQuery()) {

                    while (senderData.next()) {

                        senderBalance = senderData.getInt("balance");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (senderBalance < amt) {
                    System.out.println("\nNot enough money!");
                    con.close();
                    return;
                }

                updateSenderAccount.setInt(1, amt);
                updateSenderAccount.setString(2, senderNumber);
                updateSenderAccount.setString(3, senderPin);
                int x = updateSenderAccount.executeUpdate();

                if (x <= 0) {
                    System.out.println("Unable to complete");
                    con.rollback();
                    con.close();
                    return;
                }

                updateRecipientAccount.setInt(1, amt);
                updateRecipientAccount.setString(2, recipientNumber);
                int y = updateRecipientAccount.executeUpdate();

                if (y <= 0) {
                    System.out.println("Unable to complete2");
                    con.rollback();
                    con.close();
                    return;
                }

                con.commit();
                System.out.println("Success!");


            } catch (SQLException e) {

                    con.rollback();

                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    int getBalance(String cardNumber, String userPin) {

        int balance = -1;

        String selectData = "SELECT * FROM card WHERE number = ? AND pin = ?;" ;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedSt = connection.prepareStatement(selectData)) {

                preparedSt.setString(1, cardNumber);
                preparedSt.setString(2, userPin);

                try (ResultSet userInfo = preparedSt.executeQuery()) {

                    while (userInfo.next()) {

                        balance = userInfo.getInt("balance");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;
    }

    boolean closeAccount(String cardNumber, String userPin) {

        boolean isDeleted = false;

        String deleteUser = "DELETE FROM card WHERE number = ? AND pin = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedSt = connection.prepareStatement(deleteUser)) {

                preparedSt.setString(1, cardNumber);
                preparedSt.setString(2, userPin);

                int x = preparedSt.executeUpdate();
                isDeleted = x > 0;

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    boolean updateBalance(String cardNumber, String userPin, int income) {

        boolean isUpdated = false;

        String updateData = "UPDATE card SET balance = balance + ? WHERE number = ? AND pin = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedSt = connection.prepareStatement(updateData)) {

                preparedSt.setInt(1, income);
                preparedSt.setString(2, cardNumber);
                preparedSt.setString(3, userPin);

                int x = preparedSt.executeUpdate();
                isUpdated = x > 0;

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    boolean isValid(String cardNumber) {

        boolean isValid = false;

        String selectData = "SELECT * FROM card WHERE number = ?;" ;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedSt = connection.prepareStatement(selectData)) {

                preparedSt.setString(1, cardNumber);

                try (ResultSet userInfo = preparedSt.executeQuery()) {

                    while (userInfo.next()) {

                        isValid = true;
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;

    }

    boolean isValid(String cardNumber, String userPin) {

        boolean isValid = false;

        String selectData = "SELECT * FROM card WHERE number = ? AND pin = ?;" ;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedSt = connection.prepareStatement(selectData)) {

                preparedSt.setString(1, cardNumber);
                preparedSt.setString(2, userPin);

                try (ResultSet userInfo = preparedSt.executeQuery()) {

                    while (userInfo.next()) {

                        isValid = true;
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;

    }
}


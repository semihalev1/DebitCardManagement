
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CompanyDbMenagement {

    private static final String connectionString = "jdbc:mysql://localhost:3306/Customer_infos?user=root";
    private static final String connectionString2 = "jdbc:mysql://localhost:3306/Card?user=root";

    public static String GetCustomerName(long id) {

        Connection conn = null;
        ResultSet rs = null;
        DebitCardCustomers customer = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM debit_card_customer WHERE customer_id=" + id;
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                customer = new DebitCardCustomers();
                //**********
                customer.setCustomerId(rs.getLong("customer_id"));
                customer.setCitizenshipId(rs.getString("citizenship_id")); // char(11) => String
                customer.setTaxNumber(rs.getLong("tax_number"));
                customer.setCardFirstName(rs.getString("card_first_name"));
                customer.setCardMiddleName(rs.getString("card_middle_name"));
                customer.setCardSurname(rs.getString("car_surname"));
                customer.setBirthDate(rs.getDate("birth_date"));
                customer.setMotherName(rs.getString("mother_name"));
                customer.setFatherName(rs.getString("father_name"));
                customer.setBirthPlace(rs.getString("birth_place"));
                customer.setRecordStatus(rs.getString("record_status").charAt(0));
            }
            conn.close();

        } catch (SQLException ex) {

            System.out.println(" error:" + ex.getMessage());
        }

        return customer.getCardFirstName() + " " + customer.getCardSurname();

    }

    public static DebitCardCustomers GetCustomer(long id) {

        Connection conn = null;
        ResultSet rs = null;
        DebitCardCustomers customer = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM debit_card_customer WHERE customer_id=" + id;
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                customer = new DebitCardCustomers();
                //**********
                customer.setCustomerId(rs.getLong("customer_id"));
                customer.setCitizenshipId(rs.getString("citizenship_id")); // char(11) => String
                customer.setTaxNumber(rs.getLong("tax_number"));
                customer.setCardFirstName(rs.getString("card_first_name"));
                customer.setCardMiddleName(rs.getString("card_middle_name"));
                customer.setCardSurname(rs.getString("car_surname"));
                customer.setBirthDate(rs.getDate("birth_date"));
                customer.setMotherName(rs.getString("mother_name"));
                customer.setFatherName(rs.getString("father_name"));
                customer.setBirthPlace(rs.getString("birth_place"));
                customer.setRecordStatus(rs.getString("record_status").charAt(0));
            }
            conn.close();

        } catch (SQLException ex) {

            System.out.println(" error:" + ex.getMessage());
        }

        return customer;

    }

    public static boolean AddCustomer(DebitCardCustomers customer) {
        boolean rvalue = true;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(connectionString2);

            // 1. Müşteri zaten var mı kontrol et
            String checkQuery = "SELECT COUNT(*) FROM Card.debit_card_customer WHERE customer_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setLong(1, customer.getCustomerId());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Müşteri zaten kayıtlı
                rs.close();
                checkStmt.close();
                conn.close();
                return false;
            }
            rs.close();
            checkStmt.close();

            // 2. Yeni müşteri ekle
            String query = "INSERT INTO Card.debit_card_customer "
                    + "(customer_id, citizenship_id, tax_number, card_first_name, card_middle_name, "
                    + "car_surname, birth_date, mother_name, father_name, birth_place, record_status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setLong(1, customer.getCustomerId());
            pstmt.setString(2, customer.getCitizenshipId());
            pstmt.setLong(3, customer.getTaxNumber());
            pstmt.setString(4, customer.getCardFirstName());
            pstmt.setString(5, customer.getCardMiddleName());
            pstmt.setString(6, customer.getCardSurname());
            pstmt.setDate(7, customer.getBirthDate());
            pstmt.setString(8, customer.getMotherName());
            pstmt.setString(9, customer.getFatherName());
            pstmt.setString(10, customer.getBirthPlace());
            pstmt.setString(11, String.valueOf(customer.getRecordStatus())); // char → String

            pstmt.executeUpdate();
            pstmt.close();
            conn.close();

        } catch (SQLException ex) {
            rvalue = false;
            System.out.println("SQL Error (AddCustomer): " + ex.getMessage());
        }

        return rvalue;
    }

    public static Long getCustomerIdByCitizenship(String citizenshipId) {
        Long customerId = null;
        String query = "SELECT customer_id FROM debit_card_customer WHERE citizenship_id = ?";
        try (Connection conn = DriverManager.getConnection(connectionString); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, citizenshipId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                customerId = rs.getLong("customer_id");
            }

        } catch (SQLException ex) {
            System.out.println("Hata (getCustomerIdByCitizenship): " + ex.getMessage());
        }
        return customerId;
    }

    public static String getCitizenshipIdByCustomerId(Long customerId) {
        String citizenshipId = null;
        String query = "SELECT citizenship_id FROM debit_card_customer WHERE customer_id = ?";

        try (Connection conn = DriverManager.getConnection(connectionString); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                citizenshipId = rs.getString("citizenship_id");
            }

        } catch (SQLException ex) {
            System.out.println("Hata (getCitizenshipIdByCustomerId): " + ex.getMessage());
        }

        return citizenshipId;
    }

    public static boolean AddCard(DebitCard card) {

        boolean rvalue = true;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "INSERT INTO Card.debit_card "
                    + "(card_uniq_id, card_number, card_emboss, card_branch_code, customer_id, "
                    + "status_code, expiry_date, insert_date, update_date, card_style_code) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, card.getCardUniqId());
            pstmt.setString(2, card.getCardNumber());
            pstmt.setString(3, card.getCardEmboss());
            pstmt.setShort(4, card.getCardBranchCode());
            pstmt.setLong(5, card.getCustomerId());
            pstmt.setString(6, String.valueOf(card.getStatusCode())); // char → String
            pstmt.setDate(7, card.getExpiryDate());
            pstmt.setTimestamp(8, card.getInsertDate());
            pstmt.setTimestamp(9, card.getUpdateDate());
            pstmt.setString(10, card.getCardStyleCode());

            pstmt.executeUpdate();
            conn.close();

        } catch (SQLException ex) {
            rvalue = false;
            System.out.println("SQL Error (AddCard): " + ex.getMessage());
        }

        return rvalue;
    }

    public static boolean UpdateCard(DebitCard card) {

        boolean rvalue = true;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "UPDATE Card.debit_card "
                    + "SET status_code = ?, expiry_date = ?, update_date = ? "
                    + "WHERE card_uniq_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, String.valueOf(card.getStatusCode())); // char → String
            pstmt.setDate(2, card.getExpiryDate()); // java.sql.Date
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // update_date -> now
            pstmt.setString(4, card.getCardUniqId()); // hangi kart güncellenecek?

            pstmt.executeUpdate();
            conn.close();

        } catch (SQLException ex) {
            rvalue = false;
            System.out.println("SQL Error (UpdateCardStatusAndExpiry): " + ex.getMessage());
        }

        return rvalue;
    }

    public static boolean cardNumberExists(String cardNumber) {
        Connection conn = null;
        ResultSet rs = null;
        boolean exists = false;
        String query = "SELECT COUNT(*) AS total FROM debit_card WHERE card_number = ?";

        try {
            conn = DriverManager.getConnection(connectionString2);
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, cardNumber);
            rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("total");
                exists = count > 0;
            }

            pst.close();

        } catch (SQLException ex) {
            System.out.println("Error in cardNumberExists: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return exists;
    }

    public static boolean cardStyleExists(long cardCustomerId, String style) {
        Connection conn = null;
        ResultSet rs = null;
        boolean exists = false;
        String query = "SELECT COUNT(*) AS total FROM debit_card WHERE customer_id = ? AND card_style_code = ?";

        try {
            conn = DriverManager.getConnection(connectionString2);
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setLong(1, cardCustomerId);
            pst.setString(2, style);
            rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("total");
                exists = count > 0;
            }

            pst.close();

        } catch (SQLException ex) {
            System.out.println("Error in cardStyleExists: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return exists;
    }

    public static boolean accountExists(String cardUniqId, int suffix) {
        Connection conn = null;
        ResultSet rs = null;
        boolean exists = false;
        String query = "SELECT COUNT(*) AS total FROM debit_card_accounts WHERE card_uniq_id = ? AND suffix = ?";

        try {
            conn = DriverManager.getConnection(connectionString2);
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, cardUniqId);
            pst.setInt(2, suffix);
            rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("total");
                exists = count > 0;
            }

            pst.close();

        } catch (SQLException ex) {
            System.out.println("Error in accountExists: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return exists;
    }

    public static boolean cardUniqIdExists(String cardUniqId) {
        Connection conn = null;
        ResultSet rs = null;
        boolean exists = false;
        String query = "SELECT COUNT(*) AS total FROM debit_card WHERE card_uniq_id = ?";

        try {
            conn = DriverManager.getConnection(connectionString2);
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, cardUniqId);
            rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("total");
                exists = count > 0;
            }

            pst.close();
        } catch (SQLException ex) {
            System.out.println("Error in cardUniqIdExists: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return exists;
    }

// id'ye göre kart nesnesi çekme 
    public static DebitCard GetCardByCustomerId(long customerId) {
        Connection conn = null;
        ResultSet rs = null;
        DebitCard card = null;

        try {
            conn = DriverManager.getConnection(connectionString2);
            String query = "SELECT * FROM debit_card WHERE customer_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setLong(1, customerId);
            rs = pst.executeQuery();

            if (rs.next()) {
                card = new DebitCard();

                card.setCardUniqId(rs.getString("card_uniq_id"));
                card.setCardNumber(rs.getString("card_number"));
                card.setCardEmboss(rs.getString("card_emboss"));
                card.setCardBranchCode(rs.getShort("card_branch_code"));
                card.setCustomerId(rs.getLong("customer_id"));
                card.setStatusCode(rs.getString("status_code").charAt(0));
                card.setExpiryDate(rs.getDate("expiry_date"));
                card.setInsertDate(rs.getTimestamp("insert_date"));
                card.setUpdateDate(rs.getTimestamp("update_date"));
                card.setCardStyleCode(rs.getString("card_style_code"));
            }

            pst.close();
            conn.close();

        } catch (SQLException ex) {
            System.out.println("Error in GetCardByCustomerId: " + ex.getMessage());
        }

        return card;
    }

    // card_number'a göre kart nesnesi çekme 
    public static DebitCard GetCardByCardNo(String cardNo) {
        Connection conn = null;
        ResultSet rs = null;
        DebitCard card = null;

        try {
            conn = DriverManager.getConnection(connectionString2);
            String query = "SELECT * FROM debit_card WHERE card_number = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, cardNo);
            rs = pst.executeQuery();

            if (rs.next()) {
                card = new DebitCard();

                card.setCardUniqId(rs.getString("card_uniq_id"));
                card.setCardNumber(rs.getString("card_number"));
                card.setCardEmboss(rs.getString("card_emboss"));
                card.setCardBranchCode(rs.getShort("card_branch_code"));
                card.setCustomerId(rs.getLong("customer_id"));
                card.setStatusCode(rs.getString("status_code").charAt(0));
                card.setExpiryDate(rs.getDate("expiry_date"));
                card.setInsertDate(rs.getTimestamp("insert_date"));
                card.setUpdateDate(rs.getTimestamp("update_date"));
                card.setCardStyleCode(rs.getString("card_style_code"));
            }

            pst.close();
            conn.close();

        } catch (SQLException ex) {
            System.out.println("Error in GetCardByCardId: " + ex.getMessage());
        }

        return card;
    }

    public static boolean doesCardNumberExist(String cardNumber, long customerId) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DriverManager.getConnection(connectionString2);
            String query = "SELECT 1 FROM debit_card WHERE card_number = ? AND customer_id = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, cardNumber);
            pst.setLong(2, customerId);
            rs = pst.executeQuery();

            if (rs.next()) {
                exists = true; // kart numarası bulundu
            }

        } catch (SQLException ex) {
            System.out.println("Error in doesCardNumberExist: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return exists;
    }

    public static boolean doesCardNumberExist(String cardNumber) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DriverManager.getConnection(connectionString2);
            String query = "SELECT 1 FROM debit_card WHERE card_number = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, cardNumber);
            rs = pst.executeQuery();

            if (rs.next()) {
                exists = true; // kart numarası bulundu
            }

        } catch (SQLException ex) {
            System.out.println("Error in doesCardNumberExist: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return exists;
    }

    public static boolean AddAddress(DebitCardAddress address) {
        boolean result = true;
        Connection conn = null;

        String insertQuery = "INSERT INTO card.debit_card_address "
                + "(customer_id, address_type, address_line1, address_line2, address_town, address_city, address_country, address_zip, record_status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = DriverManager.getConnection(connectionString2);
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);

            pstmt.setLong(1, address.getCustomerId());
            pstmt.setShort(2, address.getAddressType());
            pstmt.setString(3, address.getAddressLine1());
            pstmt.setString(4, address.getAddressLine2());
            pstmt.setString(5, address.getAddressTown());
            pstmt.setString(6, address.getAddressCity());
            pstmt.setString(7, address.getAddressCountry());
            pstmt.setString(8, address.getAddressZip());
            pstmt.setString(9, String.valueOf(address.getRecordStatus()));

            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException ex) {
            result = false;
            System.out.println("SQL Error (AddAddress): " + ex.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Connection Close Error: " + e.getMessage());
            }
        }

        return result;
    }

    public static DebitCardAddress GetAddress(long customerId, short addressType) {

        Connection conn = null;
        ResultSet rs = null;
        DebitCardAddress address = null;

        try {
            conn = DriverManager.getConnection(connectionString2);
            String query = "SELECT * FROM Card.debit_card_address WHERE customer_id = ? AND address_type = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setLong(1, customerId);
            pstmt.setShort(2, addressType);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                address = new DebitCardAddress();

                address.setCustomerId(rs.getLong("customer_id"));
                address.setAddressType(rs.getShort("address_type"));
                address.setAddressLine1(rs.getString("address_line1"));
                address.setAddressLine2(rs.getString("address_line2"));
                address.setAddressTown(rs.getString("address_town"));
                address.setAddressCity(rs.getString("address_city"));
                address.setAddressCountry(rs.getString("address_country"));
                address.setAddressZip(rs.getString("address_zip"));
                address.setRecordStatus(rs.getString("record_status").charAt(0));
            }
            conn.close();

        } catch (SQLException ex) {
            System.out.println("SQL Error (GetAddress): " + ex.getMessage());
        }

        return address;
    }
    public static DebitCardAddress GetAddressforCustomer(long customerId, short addressType) {

        Connection conn = null;
        ResultSet rs = null;
        DebitCardAddress address = null;

        try {
            conn = DriverManager.getConnection(connectionString);
            String query = "SELECT * FROM Customer_infos.debit_card_address WHERE customer_id = ? AND address_type = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setLong(1, customerId);
            pstmt.setShort(2, addressType);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                address = new DebitCardAddress();

                address.setCustomerId(rs.getLong("customer_id"));
                address.setAddressType(rs.getShort("address_type"));
                address.setAddressLine1(rs.getString("address_line1"));
                address.setAddressLine2(rs.getString("address_line2"));
                address.setAddressTown(rs.getString("address_town"));
                address.setAddressCity(rs.getString("address_city"));
                address.setAddressCountry(rs.getString("address_country"));
                address.setAddressZip(rs.getString("address_zip"));
                address.setRecordStatus(rs.getString("record_status").charAt(0));
            }
            conn.close();

        } catch (SQLException ex) {
            System.out.println("SQL Error (GetAddress): " + ex.getMessage());
        }

        return address;
    }

    public static boolean accountNoExists(long accountNo) {
        Connection conn = null;
        ResultSet rs = null;
        boolean exists = false;
        String query = "SELECT COUNT(*) AS total FROM debit_card_accounts WHERE account_no = ?";

        try {
            conn = DriverManager.getConnection(connectionString2);
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setLong(1, accountNo);
            rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("total");
                exists = count > 0;
            }

            pst.close();
        } catch (SQLException ex) {
            System.out.println("Error in accountNoExists: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return exists;
    }

    public static boolean AddAccount(DebitCardAccounts account) {
        boolean rvalue = true;
        Connection conn = null;
        if (!doesAccountExist(account.getAccountNo())) {
            try {
                conn = DriverManager.getConnection(connectionString2);

                String query = "INSERT INTO Card.debit_card_accounts "
                        + "(card_uniq_id, branch_code, account_no, currency, account_type, "
                        + "update_date, record_status, suffix, customer_id) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, account.getCardUniqId());
                pstmt.setInt(2, account.getBranchCode()); // branch_code artık int
                pstmt.setLong(3, account.getAccountNo());
                pstmt.setString(4, account.getCurrency()); // currency: char(3)
                pstmt.setString(5, String.valueOf(account.getAccountType())); // account_type: char(1)
                pstmt.setTimestamp(6, account.getUpdateDate()); // datetime -> Timestamp
                pstmt.setString(7, String.valueOf(account.getRecordStatus())); // char(1) -> String
                pstmt.setInt(8, account.getSuffix());
                pstmt.setLong(9, account.getCustomerId());

                pstmt.executeUpdate();
                conn.close();

            } catch (SQLException ex) {
                rvalue = false;
                System.out.println("SQL Error (AddAccount): " + ex.getMessage());
            }

            return rvalue;
        } else {
            rvalue = false;
            JOptionPane.showMessageDialog(null, "Hesap zaten bağlı!", "uyarı", JOptionPane.WARNING_MESSAGE);
        }
        return rvalue;
    }

    public static boolean doesAccountExist(long accountNo) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DriverManager.getConnection(connectionString2);
            String query = "SELECT 1 FROM debit_card_accounts WHERE account_no = ?";
            pst = conn.prepareStatement(query);
            pst.setLong(1, accountNo);
            rs = pst.executeQuery();

            if (rs.next()) {
                exists = true; // hesap numarası bulundu
            }

        } catch (SQLException ex) {
            System.out.println("Error in doesAccountExist: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return exists;
    }

    public static boolean doesMainAccountExist(long customerId) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DriverManager.getConnection(connectionString2);
            String query = "SELECT 1 FROM debit_card_accounts WHERE customer_id = ? AND account_type = 'A'";
            pst = conn.prepareStatement(query);
            pst.setLong(1, customerId);
            rs = pst.executeQuery();

            if (rs.next()) {
                exists = true; // hesap bulundu
            }

        } catch (SQLException ex) {
            System.out.println("Error in doesMainAccountExist: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return exists;
    }

    public static boolean AddDebitCardAuthors(
            String cardUniqId,
            boolean ecommersYi,
            boolean ecommersYd,
            boolean mailorderYi,
            boolean mailorderYd,
            boolean isposYi,
            boolean isposYd,
            char recordStatus
    ) {
        boolean rvalue = true;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "INSERT INTO Card.debit_card_authors "
                    + "(card_uniq_id, ecommers_yi, ecommers_yd, mailorder_yi, mailorder_yd, ispos_yi, ispos_yd, record_status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cardUniqId);
            pstmt.setBoolean(2, ecommersYi);
            pstmt.setBoolean(3, ecommersYd);
            pstmt.setBoolean(4, mailorderYi);
            pstmt.setBoolean(5, mailorderYd);
            pstmt.setBoolean(6, isposYi);
            pstmt.setBoolean(7, isposYd);
            pstmt.setString(8, String.valueOf(recordStatus));

            pstmt.executeUpdate();
            conn.close();

        } catch (SQLException ex) {
            rvalue = false;
            System.out.println("SQL Error (AddDebitCardAuthors): " + ex.getMessage());
        }

        return rvalue;
    }

    public static String getCardIdByCardNo(String cardNo) {
        String cardId = null;
        String query = "SELECT card_uniq_id FROM debit_card WHERE card_number = ?";

        try (Connection conn = DriverManager.getConnection(connectionString2); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cardNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cardId = rs.getString("card_uniq_id");
            }

        } catch (SQLException ex) {
            System.out.println("Hata (getCardIdByCardNo): " + ex.getMessage());
        }

        return cardId;
    }

    public static long getCustomerIdByCardNo(String cardNo) {
        long customerId = 0;
        String query = "SELECT customer_id FROM debit_card WHERE card_number = ?";

        try (Connection conn = DriverManager.getConnection(connectionString2); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cardNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customerId = rs.getLong("customer_id");
            }

        } catch (SQLException ex) {
            System.out.println("Hata (getCustomerIdByCardNo): " + ex.getMessage());
        }

        return customerId;
    }

    public static short getBranchCodeByCardNo(String cardNo) {
        short branchCode = 0;
        String query = "SELECT card_branch_code FROM Card.debit_card WHERE card_number = ?";

        try (Connection conn = DriverManager.getConnection(connectionString2); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cardNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                branchCode = rs.getShort("card_branch_code");
            }

        } catch (SQLException ex) {
            System.out.println("Hata (getBranchCodeByCardNo): " + ex.getMessage());
        }

        return branchCode;
    }

    public static Map<String, Object> GetDebitCardAuthors(String cardUniqId) {
        Connection conn = null;
        ResultSet rs = null;
        Map<String, Object> authors = null;

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "SELECT * FROM Card.debit_card_authors WHERE card_uniq_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cardUniqId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                authors = new HashMap<>();
                authors.put("card_uniq_id", rs.getString("card_uniq_id"));
                authors.put("ecommers_yi", rs.getBoolean("ecommers_yi"));
                authors.put("ecommers_yd", rs.getBoolean("ecommers_yd"));
                authors.put("mailorder_yi", rs.getBoolean("mailorder_yi"));
                authors.put("mailorder_yd", rs.getBoolean("mailorder_yd"));
                authors.put("ispos_yi", rs.getBoolean("ispos_yi"));
                authors.put("ispos_yd", rs.getBoolean("ispos_yd"));
                authors.put("record_status", rs.getString("record_status").charAt(0));
            }

            pstmt.close();

        } catch (SQLException ex) {
            System.out.println("SQL Error (GetDebitCardAuthors): " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return authors;
    }

    public static boolean UpdateDebitCardAuthors(
            String cardUniqId,
            boolean ecommersYi,
            boolean ecommersYd,
            boolean mailorderYi,
            boolean mailorderYd,
            boolean isposYi,
            boolean isposYd
    ) {
        boolean rvalue = true;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "UPDATE Card.debit_card_authors SET "
                    + "ecommers_yi = ?, "
                    + "ecommers_yd = ?, "
                    + "mailorder_yi = ?, "
                    + "mailorder_yd = ?, "
                    + "ispos_yi = ?, "
                    + "ispos_yd = ? "
                    + "WHERE card_uniq_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setBoolean(1, ecommersYi);
            pstmt.setBoolean(2, ecommersYd);
            pstmt.setBoolean(3, mailorderYi);
            pstmt.setBoolean(4, mailorderYd);
            pstmt.setBoolean(5, isposYi);
            pstmt.setBoolean(6, isposYd);
            pstmt.setString(7, cardUniqId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                rvalue = false;
                System.out.println("Update failed: No record found with card_uniq_id = " + cardUniqId);
            }

            conn.close();

        } catch (SQLException ex) {
            rvalue = false;
            System.out.println("SQL Error (UpdateDebitCardAuthors): " + ex.getMessage());
        }

        return rvalue;
    }

    // kart status k olduğunda hepsini k yap
    public static boolean UpdateDebitCardStatusAuthors(
            String cardUniqId,
            char status
    ) {
        boolean rvalue = true;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "UPDATE Card.debit_card_authors SET "
                    + "record_status = ?"
                    + "WHERE card_uniq_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, String.valueOf(status));
            pstmt.setString(2, cardUniqId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                rvalue = false;
                System.out.println("Update failed: No record found with card_uniq_id = " + cardUniqId);
            }

            conn.close();

        } catch (SQLException ex) {
            rvalue = false;
            System.out.println("SQL Error (UpdateDebitCardAuthors): " + ex.getMessage());
        }

        return rvalue;
    }

    public static boolean UpdateAccountStatus(
            String cardUniqId,
            char status
    ) {
        boolean rvalue = true;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "UPDATE Card.debit_card_accounts SET "
                    + "record_status = ?"
                    + "WHERE card_uniq_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, String.valueOf(status));
            pstmt.setString(2, cardUniqId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                rvalue = false;
                System.out.println("Update failed: No record found with card_uniq_id = " + cardUniqId);
            }

            conn.close();

        } catch (SQLException ex) {
            rvalue = false;
            System.out.println("SQL Error (UpdateDebitCardAccounts): " + ex.getMessage());
        }

        return rvalue;
    }

    public static List<List<String>> getAccountDetailsMain(long customerId) {
        Connection conn = null;
        ResultSet rs = null;
        List<List<String>> accounts = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "SELECT currency, account_no, suffix FROM debit_card_accounts WHERE customer_id = ? AND currency = 'TRY'";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setLong(1, customerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString("currency"));
                row.add(rs.getString("account_no"));                      // account_no
                row.add(rs.getString("suffix"));                         // currency
                accounts.add(row);
            }

            pstmt.close();

        } catch (SQLException ex) {
            System.out.println("SQL Error (getAccountDetails): " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return accounts;
    }

    public static List<List<String>> getAccountDetails(long customerId) {
        Connection conn = null;
        ResultSet rs = null;
        List<List<String>> accounts = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "SELECT currency, account_no, suffix FROM debit_card_accounts WHERE customer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setLong(1, customerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString("currency"));
                row.add(rs.getString("account_no"));                      // account_no
                row.add(rs.getString("suffix"));                         // currency
                accounts.add(row);
            }

            pstmt.close();

        } catch (SQLException ex) {
            System.out.println("SQL Error (getAccountDetails): " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return accounts;
    }

    public static List<List<String>> getAccountDetailsByCardNo(String card_id) {
        Connection conn = null;
        ResultSet rs = null;
        List<List<String>> accounts = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "SELECT currency, account_no, suffix FROM debit_card_accounts WHERE card_uniq_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, card_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString("currency"));
                row.add(rs.getString("account_no"));                      // account_no
                row.add(rs.getString("suffix"));                         // currency
                accounts.add(row);
            }

            pstmt.close();

        } catch (SQLException ex) {
            System.out.println("SQL Error (getAccountDetails): " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return accounts;
    }

    public static List<List<String>> getAccountDetailsMainByCardNo(String card_id) {
        Connection conn = null;
        ResultSet rs = null;
        List<List<String>> accounts = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "SELECT currency, account_no, suffix FROM Card.debit_card_accounts WHERE card_uniq_id = ? AND currency = 'TRY'";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, card_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString("currency"));
                row.add(rs.getString("account_no"));                      // account_no
                row.add(rs.getString("suffix"));                         // currency
                accounts.add(row);
            }

            pstmt.close();

        } catch (SQLException ex) {
            System.out.println("SQL Error (getAccountDetails): " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return accounts;
    }

    public static List<List<String>> getAccountNumbersMainByCardNo(String card_id, char type) {
        Connection conn = null;
        ResultSet rs = null;
        List<List<String>> accounts = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "SELECT account_no FROM Card.debit_card_accounts WHERE card_uniq_id = ? AND account_type = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, card_id);
            pstmt.setString(2, String.valueOf(type));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString("account_no"));                      // account_no
                accounts.add(row);
            }

            pstmt.close();

        } catch (SQLException ex) {
            System.out.println("SQL Error (getAccountDetails): " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return accounts;
    }

    public static void deleteAllAccountsByCardIdAndType(String cardId, char accountType) {
        String query = "DELETE FROM Card.debit_card_accounts WHERE card_uniq_id = ? AND account_type = ?";
        try (Connection conn = DriverManager.getConnection(connectionString2); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, cardId);
            pstmt.setString(2, String.valueOf(accountType));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Virman hesapları silinirken hata oluştu: " + e.getMessage());
        }
    }
    public static void deleteAllAccountsByAccountNo(long accountNo) {
        String query = "DELETE FROM Card.debit_card_accounts WHERE account_no = ?";
        try (Connection conn = DriverManager.getConnection(connectionString2); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, accountNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Virman hesapları silinirken hata oluştu: " + e.getMessage());
        }
    }

    private static String accountTypeToString(int accountType) {
        switch (accountType) {
            case 1:
                return "Cari Hesap";
            case 2:
                return "Katılma Hesap";
            case 3:
                return "Yatırım Hesap";
            default:
                return "Bilinmeyen Tip";
        }
    }

    public static DebitCardAccounts GetAccountById(long accountNo) {
        Connection conn = null;
        ResultSet rs = null;
        DebitCardAccounts account = null;

        try {
            conn = DriverManager.getConnection(connectionString);
            String query = "SELECT * FROM debit_card_accounts WHERE account_no = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setLong(1, accountNo);
            rs = pst.executeQuery();

            if (rs.next()) {
                account = new DebitCardAccounts();

                account.setCardUniqId(rs.getString("card_uniq_id"));
                account.setAccountNo(rs.getLong("account_no"));
                account.setCurrency(rs.getString("currency"));
                account.setUpdateDate(rs.getTimestamp("update_date"));
                account.setRecordStatus(rs.getString("record_status").charAt(0));
                account.setSuffix(rs.getInt("suffix"));
                account.setCustomerId(rs.getLong("customer_id"));
            }

            pst.close();
            conn.close();

        } catch (SQLException ex) {
            System.out.println("Error in GetAccountById: " + ex.getMessage());
        }

        return account;
    }

    public static void searchByCardIdMain(String cardId, DefaultTableModel tblTableModel) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        tblTableModel.setRowCount(0); // tabloyu temizle

        try {
            conn = DriverManager.getConnection(connectionString2);
            String query = "SELECT * FROM debit_card_accounts WHERE card_uniq_id = ? AND currency = 'TRY'";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cardId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String currency = rs.getString("currency");
                long accountNo = rs.getLong("account_no");
                int suffix = rs.getInt("suffix");

                tblTableModel.addRow(new Object[]{currency, accountNo, suffix});
            }

        } catch (SQLException ex) {
            System.out.println("SQL Error (searchByCardIdMain): " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void searchByCustomerIdMain(long customerId, DefaultTableModel tblTableModel) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        tblTableModel.setRowCount(0); // tabloyu temizle

        try {
            conn = DriverManager.getConnection(connectionString);
            String query = "SELECT * FROM debit_card_accounts WHERE customer_id = ? AND currency = 'TRY'";
            pstmt = conn.prepareStatement(query);
            pstmt.setLong(1, customerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String currency = rs.getString("currency");
                long accountNo = rs.getLong("account_no");
                int suffix = rs.getInt("suffix");

                tblTableModel.addRow(new Object[]{currency, accountNo, suffix});
            }

        } catch (SQLException ex) {
            System.out.println("SQL Error (searchByCardIdMain): " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void searchByCustomerId(long customerId, DefaultTableModel tblTableModel) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        tblTableModel.setRowCount(0); // tabloyu temizle

        try {
            conn = DriverManager.getConnection(connectionString);
            String query = "SELECT * FROM debit_card_accounts WHERE customer_id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setLong(1, customerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String currency = rs.getString("currency");
                long accountNo = rs.getLong("account_no");
                int suffix = rs.getInt("suffix");

                tblTableModel.addRow(new Object[]{currency, accountNo, suffix});
            }

        } catch (SQLException ex) {
            System.out.println("SQL Error (searchByCardIdMain): " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void searchVirmanAccountsByCardIdan(String cardId, DefaultTableModel tblTableModel) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        tblTableModel.setRowCount(0); // tabloyu temizle

        try {
            conn = DriverManager.getConnection(connectionString2);
            String query = "SELECT * FROM debit_card_accounts WHERE card_uniq_id = ? AND account_type = 'V'";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cardId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String currency = rs.getString("currency");
                long accountNo = rs.getLong("account_no");
                int suffix = rs.getInt("suffix");

                tblTableModel.addRow(new Object[]{currency, accountNo, suffix});
            }

        } catch (SQLException ex) {
            System.out.println("SQL Error (searchVirmanAccountsByCardIdan): " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean UpdateAddress(DebitCardAddress address) {

        boolean rvalue = true;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(connectionString2);

            String query = "UPDATE Card.debit_card_address "
                    + "SET address_line1 = ?, address_line2 = ?, address_town = ?, address_city = ?, "
                    + "address_country = ?, address_zip = ?"
                    + "WHERE customer_id = ? AND address_type = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, address.getAddressLine1());
            pstmt.setString(2, address.getAddressLine2());
            pstmt.setString(3, address.getAddressTown());
            pstmt.setString(4, address.getAddressCity());
            pstmt.setString(5, address.getAddressCountry());
            pstmt.setString(6, address.getAddressZip());
            //pstmt.setString(7, String.valueOf(address.getRecordStatus())); // char → String
            pstmt.setLong(7, address.getCustomerId());
            pstmt.setShort(8, address.getAddressType());

            pstmt.executeUpdate();
            conn.close();

        } catch (SQLException ex) {
            rvalue = false;
            System.out.println("SQL Error (UpdateAddress): " + ex.getMessage());
        }

        return rvalue;
    }

    public static List<Long> getAccountNosByType(char type) {
        List<Long> accountNos = new ArrayList<>();
        String query = "SELECT account_no FROM Card.debit_card_accounts WHERE account_type = ?";

        try (Connection conn = DriverManager.getConnection(connectionString2); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, String.valueOf(type));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                accountNos.add(rs.getLong("account_no"));
            }

        } catch (SQLException e) {
            System.out.println("SQL Error (getAccountNosByType): " + e.getMessage());
        }

        return accountNos;
    }
}

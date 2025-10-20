
import java.sql.Date;
import java.sql.Timestamp;

public class DebitCard {
    private String cardUniqId;
    private String cardNumber;
    private String cardEmboss;
    private short cardBranchCode;
    private long customerId;
    private char statusCode;
    private java.sql.Date expiryDate;
    private java.sql.Timestamp insertDate;
    private java.sql.Timestamp updateDate;
    private String cardStyleCode;

    
    public String getCardUniqId() {
        return cardUniqId;
    }

    public void setCardUniqId(String cardUniqId) {
        this.cardUniqId = cardUniqId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardEmboss() {
        return cardEmboss;
    }

    public void setCardEmboss(String cardEmboss) {
        this.cardEmboss = cardEmboss;
    }

    public short getCardBranchCode() {
        return cardBranchCode;
    }

    public void setCardBranchCode(short cardBranchCode) {
        this.cardBranchCode = cardBranchCode;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public char getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(char statusCode) {
        this.statusCode = statusCode;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Timestamp getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getCardStyleCode() {
        return cardStyleCode;
    }

    public void setCardStyleCode(String cardStyleCode) {
        this.cardStyleCode = cardStyleCode;
    }
    
    
}

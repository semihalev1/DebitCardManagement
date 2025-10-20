
import java.sql.Timestamp;

public class DebitCardAccounts {

    private String cardUniqId;
    private short branchCode;
    private long accountNo;
    private String currency;
    private char accountType;
    private int suffix;
    private java.sql.Timestamp updateDate;
    private char recordStatus;
    private long customerId;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public int getSuffix() {
        return suffix;
    }

    public void setSuffix(int suffix) {
        this.suffix = suffix;
    }

    public String getCardUniqId() {
        return cardUniqId;
    }

    public void setCardUniqId(String cardUniqId) {
        this.cardUniqId = cardUniqId;
    }

    public short getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(short branchCode) {
        this.branchCode = branchCode;
    }

    public long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(long accountNo) {
        this.accountNo = accountNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public char getAccountType() {
        return accountType;
    }

    public void setAccountType(char accountType) {
        this.accountType = accountType;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public char getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(char recordStatus) {
        this.recordStatus = recordStatus;
    }
    
}


import java.sql.Date;

public class DebitCardCustomers {
    private long customerId;
    private String citizenshipId;
    private long taxNumber;
    private String cardFirstName;
    private String cardMiddleName;
    private String cardSurname;
    private java.sql.Date birthDate;
    private String motherName;
    private String fatherName;
    private String birthPlace;
    private char recordStatus;


    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCitizenshipId() {
        return citizenshipId;
    }

    public void setCitizenshipId(String citizenshipId) {
        this.citizenshipId = citizenshipId;
    }

    public long getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(long taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getCardFirstName() {
        return cardFirstName;
    }

    public void setCardFirstName(String cardFirstName) {
        this.cardFirstName = cardFirstName;
    }

    public String getCardMiddleName() {
        return cardMiddleName;
    }

    public void setCardMiddleName(String cardMiddleName) {
        this.cardMiddleName = cardMiddleName;
    }

    public String getCardSurname() {
        return cardSurname;
    }

    public void setCardSurname(String cardSurname) {
        this.cardSurname = cardSurname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public char getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(char recordStatus) {
        this.recordStatus = recordStatus;
    }
    
}

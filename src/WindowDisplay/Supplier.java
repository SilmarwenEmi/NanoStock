package WindowDisplay;

/**
 * @author Emilie Laurent
 * This class represent a common supplier that sells furniture to a client
 */
public class Supplier {
    private String supplier;
    private String contactName;
    private String contactPhone;

    /**
     * Construct the supplier object with given information
     * @param supplier: Name of the firm
     * @param contactName: Name of the contact person
     * @param contactPhone: Phone number of the contact person
     */
    public Supplier(String supplier, String contactName, String contactPhone){
        this.supplier = supplier;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
    }

    //Getter and setter methods
    public String getSupplier() {
        return supplier;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }
}

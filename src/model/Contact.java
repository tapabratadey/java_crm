package model;

/**
 * Contacts Class
 * Contact class holds:
 * 1. Contact Name
 * 2. Contact Id
 * 3. Contact Email
 * 4. Setter functions
 * 5. Getter functions
 */

public class Contact {
    private String contactName;
    private int contactId;
    private String contactEmail;


    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}

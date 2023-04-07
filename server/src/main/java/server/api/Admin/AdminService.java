package server.api.Admin;

import commons.utils.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Service
public class AdminService {

    private IDGenerator idGenerator;

    private String password;

    @Autowired
    public AdminService(@Qualifier("randomIDGenerator") IDGenerator idGenerator){
        this.idGenerator = idGenerator;
    }

    /**
     * Method to generate Admin password
     */
    public String generatePassword(){
        this.password = idGenerator.generateID().toString();
        return password;
    }

    /** Resets the server password
     * @return String
     */
    public String resetPassword() {
        generatePassword();
        System.out.println("Password has been reset, it is now:\t" + this.password);
        return this.password;
    }

    /** Getter for the password, generates a new one if the server password is null
     * @return String
     */
    public String retrievePassword() {
        if(password == null){
            generatePassword();
            System.out.println("Admin password is:\t" + this.password);
        }
        return this.password;
    }


    /** Getter for password, for testing purposes only
     * @return String
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Setter for setidGenerator, for testing purposes only
     */
    public void setidGenerator(IDGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}

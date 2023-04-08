package commons.utils;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("Hardcoded")
public class HardcodedIDGenerator implements IDGenerator {
    public String hardcodedID = "HARDCODED";


    /** Sets the hardcoded value used for creating UUIDs */
    public void setHardcodedID(String hardcodedID) {
        this.hardcodedID = hardcodedID;
    }

    /**
     * @return the generated ID
     */
    @Override
    public UUID generateID() {
        return UUID.nameUUIDFromBytes(hardcodedID.getBytes());
    }
}

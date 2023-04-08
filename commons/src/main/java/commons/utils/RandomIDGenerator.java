package commons.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;
@Component("Random")
public class RandomIDGenerator implements IDGenerator {
    @Override
    public UUID generateID() {
        return UUID.randomUUID();
    }
}

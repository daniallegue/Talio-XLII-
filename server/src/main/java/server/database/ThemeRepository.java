package server.database;

import commons.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ThemeRepository extends JpaRepository<Theme, UUID> {
}

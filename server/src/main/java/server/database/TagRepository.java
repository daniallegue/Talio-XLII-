package server.database;

import commons.CardList;
import commons.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {}

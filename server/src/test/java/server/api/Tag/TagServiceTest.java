package server.api.Tag;

import commons.*;
import commons.utils.HardcodedIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import server.api.Board.BoardService;
import server.api.Card.CardService;
import server.api.List.ListService;
import server.database.ListRepository;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    TagRepository tagRepository;
    @InjectMocks
    TagService tagService;

    Tag tag1;
    Tag tag2;

    Tag tag3;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tagService = new TagService(tagRepository);

        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");

        tag1 = new Tag(idGenerator1.generateID(), "Test Tag 1", "#000000");

        HardcodedIDGenerator idGenerator2 = new HardcodedIDGenerator();
        idGenerator2.setHardcodedID("2");

        tag2 = new Tag(idGenerator1.generateID(), "Test Tag 2", "#FFFFFF");

        tag3 = new Tag(idGenerator1.generateID(), null, "#FFFFFF");
    }

    @Test
    void updateTag() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");
        doReturn(tag1).when(tagRepository).save(tag1);

        Result<Tag> result = tagService.updateTag(tag1, idGenerator.generateID());
        assertEquals(Result.SUCCESS.of(tag1), result);
    }
    @Test
    void updateTagFAIL() {
        doReturn(Optional.of(tag1)).when(tagRepository).findById(tag1.tagID);
        doReturn(tag1).when(tagRepository).save(tag1);

        Result<Tag> updatedTag = tagService.updateTag(tag2, tag1.tagID);
        assertEquals(Result.FAILED_UPDATE_TAG, updatedTag);
    }

    @Test
    void createTagFAILNull() {
        Result<Tag> updatedTag = tagService.createTag(tag3);
        assertEquals(Result.OBJECT_ISNULL.of(null), updatedTag);
    }


    @Test
    void createTagFAIL(){
        doThrow(new RuntimeException()).when(tagRepository).save(tag1);

        Result<Tag> result = tagService.createTag(tag1);
        assertEquals(Result.FAILED_ADD_NEW_TAG.of(null), result);
    }

}
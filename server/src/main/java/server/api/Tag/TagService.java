package server.api.Tag;

import commons.Result;
import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

import java.util.UUID;

/**
 * Handles business logic of the Tag endpoints
 */
@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Updates the name and colour of the Tag with id {id},
     * with the data of the given Tag tag.
     */
    public Result<Tag> updateTag(Tag tag, UUID id) {
        try {
            return Result.SUCCESS.of(tagRepository.save(tag));
        }catch (Exception e){
            return Result.FAILED_UPDATE_TAG;
        }
    }

    /**
     * Updates the name and colour of the Tag with id {id},
     * with the data of the given Tag tag.
     */
    public Result<Tag> updateTagFromBoard(Tag tag, UUID id) {
        try {
            return Result.SUCCESS.of(tagRepository.save(tag));
        }catch (Exception e){
            return Result.FAILED_UPDATE_TAG;
        }
    }


    /**
     * Creates tag in the repo and adds it to its specific card
     */
    public Result<Tag> createTag(Tag tag) {
        if (tag.tagTitle == null) {
            return Result.OBJECT_ISNULL.of(null);
        }
        try {
            var tagResult = tagRepository.save(tag);
            return Result.SUCCESS.of(tagResult);
        }catch (Exception e){
            return Result.FAILED_ADD_NEW_TAG;
        }
    }




}

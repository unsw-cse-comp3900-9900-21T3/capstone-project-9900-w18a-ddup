package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.entity.Tag;

import java.util.List;

public interface TagService {
    /**
     * add a new tag
     *
     * @param tag new tag
     */
    void addTag(Tag tag);

    /**
     * patch a tag: update the properties if not null ( partial update )
     *
     * @param id  tag id
     * @param tag new tag info
     */
    void editTag(Long id, Tag tag);

    /**
     * delete a tag with id
     *
     * @param id tag id
     */
    void deleteTag(Long id);

    /**
     * find a tag with its id. if not found, throw {@link dev.shawnking07.ecomm_system_backend.api.error.ResourceNotFoundException ResourceNotFoundException}
     *
     * @param id tag id
     * @return tag name
     */
    String queryTag(Long id);

    /**
     * find all
     *
     * @return all tag names
     */
    List<String> findAllTags();
}

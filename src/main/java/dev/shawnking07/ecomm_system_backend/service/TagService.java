package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.entity.Tag;

import java.util.List;

public interface TagService {
    /**
     * delete a tag with its name
     *
     * @param name tag name
     */
    void deleteTag(String name);

    /**
     * find all
     *
     * @return all tag names
     */
    List<String> findAllTags();

    /**
     * fina all tags with each corresponding products <br>
     * Product must be loaded
     *
     * @return Tags
     */
    List<Tag> findAllTagsWithProducts();

    String tag2String(Tag tag);

    Tag string2Tag(String tag);
}

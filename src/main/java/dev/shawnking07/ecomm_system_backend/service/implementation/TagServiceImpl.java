package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.entity.Tag;
import dev.shawnking07.ecomm_system_backend.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Override
    public void addTag(Tag tag) {

    }

    @Override
    public void editTag(Long id, Tag tag) {

    }

    @Override
    public void deleteTag(Long id) {

    }

    @Override
    public String queryTag(Long id) {
        return null;
    }

    @Override
    public List<String> findAllTags() {
        return null;
    }
}

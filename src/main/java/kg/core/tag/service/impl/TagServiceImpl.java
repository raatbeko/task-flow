package kg.core.tag.service.impl;


import kg.core.base.service.impl.DefaultCrudService;
import kg.core.tag.model.Tag;
import kg.core.tag.repository.TagRepository;
import kg.core.tag.service.TagService;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TagServiceImpl extends DefaultCrudService<Tag, Long> implements TagService {

    TagRepository repository;

    public TagServiceImpl(TagRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAllByProjectId(Long projectId) {
        return repository.findByProjectId(projectId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Tag tag = find(id);
        repository.delete(tag);
    }
}
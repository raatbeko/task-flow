package kg.core.tag.service;

import kg.core.base.service.CrudService;
import kg.core.tag.model.Tag;


import java.util.List;

public interface TagService extends CrudService<Tag, Long> {

    void delete(Long id);

    List<Tag> findAllByProjectId(Long projectId);
}

package kg.core.tag.endpoint;

import jakarta.validation.Valid;
import kg.core.tag.dtos.TagDto;


import java.util.List;

public interface TagEndpoint {

    TagDto get(Long id);

    List<TagDto> getAll(Long projectId);

    TagDto create(@Valid TagDto dto);

    TagDto update(Long id, @Valid TagDto dto);

    void delete(Long id);
}

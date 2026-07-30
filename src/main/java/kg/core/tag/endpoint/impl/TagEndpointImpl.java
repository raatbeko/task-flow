package kg.core.tag.endpoint.impl;

import kg.core.tag.dtos.TagDto;
import kg.core.tag.endpoint.TagEndpoint;
import kg.core.tag.mapper.TagMapper;
import kg.core.tag.model.Tag;
import kg.core.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TagEndpointImpl implements TagEndpoint {

    TagMapper mapper;
    TagService service;

    @Override
    public TagDto get(Long id) {
        Tag tag = service.find(id);
        return mapper.toDto(tag);
    }

    @Override
    public List<TagDto> getAll(Long projectId) {
        List<Tag> tags = service.findAllByProjectId(projectId);
        return mapper.toDtos(tags);
    }

    @Override
    public TagDto create(TagDto dto) {
        Tag tag = mapper.toEntity(dto);
        Tag saved = service.save(tag);
        return mapper.toDto(saved);
    }

    @Override
    public TagDto update(Long id, TagDto dto) {
        Tag tag = service.find(id);
        Tag updated = mapper.update(dto, tag);
        Tag saved = service.save(updated);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        service.delete(id);
    }
}

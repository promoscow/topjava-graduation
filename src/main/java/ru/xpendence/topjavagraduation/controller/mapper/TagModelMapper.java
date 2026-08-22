package ru.xpendence.topjavagraduation.controller.mapper;

import ru.xpendence.topjavagraduation.controller.model.request.TagCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.TagUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.TagResponse;
import ru.xpendence.topjavagraduation.entity.Tag;

public final class TagModelMapper {

    private TagModelMapper() {
    }

    public static Tag toTag(TagCreateRequest request) {
        var tag = new Tag();
        tag.setName(request.name());
        return tag;
    }

    public static Tag toTag(TagUpdateRequest request) {
        var tag = new Tag();
        tag.setId(request.id());
        tag.setName(request.name());
        return tag;
    }

    public static TagResponse toResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}

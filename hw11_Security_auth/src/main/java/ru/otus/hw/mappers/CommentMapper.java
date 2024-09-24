package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.configs.MapStructConfig;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;

@Mapper(config = MapStructConfig.class, uses = BookMapper.class)
public interface CommentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "book", target = "bookDto")
    CommentDto toDto(Comment comment);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "bookDto", target = "book")
    Comment toEntity(CommentDto commentDto);

    List<CommentDto> toListDto(List<Comment> book);
}

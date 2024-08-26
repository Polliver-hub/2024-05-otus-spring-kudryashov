//package ru.otus.hw.mappers;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//import ru.otus.hw.configs.MapStructConfig;
//import ru.otus.hw.dto.CommentDto;
//import ru.otus.hw.models.Comment;
//
//@Mapper(config = MapStructConfig.class, uses = BookMapper.class)
//public interface CommentMapper {
//    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
//
////    @Mapping(source = "text", target = "text")
////    @Mapping(source = "book", target = "bookDto")
//    CommentDto toDto(Comment comment);
//
////    @Mapping(source = "text", target = "text")
////    @Mapping(source = "bookDto", target = "book")
//    Comment toEntity(CommentDto commentDto);
//
//}

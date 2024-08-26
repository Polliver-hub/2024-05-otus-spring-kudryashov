//package ru.otus.hw.mappers;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//import ru.otus.hw.configs.MapStructConfig;
//import ru.otus.hw.dto.BookDto;
//import ru.otus.hw.models.Book;
//
//import java.util.List;
//
//@Mapper(config = MapStructConfig.class, uses = {AuthorMapper.class, GenreMapper.class})
//public interface BookMapper {
//
//    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
//
////    @Mapping(source = "title", target = "title")
////    @Mapping(source = "author", target = "authorDto")
////    @Mapping(source = "genre", target = "genreDto")
//    BookDto toDto(Book book);
//
////    @Mapping(source = "title", target = "title")
////    @Mapping(source = "author", target = "authorDto")
////    @Mapping(source = "genre", target = "genreDto")
//    List<BookDto> toListDto(List<Book> book);
//
////    @Mapping(source = "title", target = "title")
////    @Mapping(source = "authorDto", target = "author")
////    @Mapping(source = "genreDto", target = "genre")
//    Book toEntity(BookDto bookDto);
//}

//package ru.otus.hw.mappers;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//import ru.otus.hw.configs.MapStructConfig;
//import ru.otus.hw.dto.AuthorDto;
//import ru.otus.hw.models.Author;
//
//@Mapper(config = MapStructConfig.class)
//public interface AuthorMapper {
//    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);
//
////    @Mapping(source = "fullName", target = "fullName")
//    AuthorDto toDto(Author author);
//
////    @Mapping(source = "fullName", target = "fullName")
//    Author toEntity(AuthorDto authorDto);
//}

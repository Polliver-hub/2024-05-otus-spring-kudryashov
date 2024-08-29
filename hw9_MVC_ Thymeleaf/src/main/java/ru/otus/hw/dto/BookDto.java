package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private long id;

    @NotBlank(message = "title must be not empty")
    @Size(min = 2, max = 50, message = "The title size is limited to 50 characters")
    private String title;

    @NotNull(message = "Author must be selected")
    private AuthorDto authorDto;

    @NotNull(message = "Author must be selected")
    private GenreDto genreDto;

}

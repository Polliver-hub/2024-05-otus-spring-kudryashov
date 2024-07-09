package ru.otus.hw.dao.dto;

import com.opencsv.bean.AbstractCsvConverter;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Answer;

@Component
public class AnswerCsvConverter extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String value) {
        var valueArr = value.split("%");
        return new Answer(valueArr[0], Boolean.parseBoolean(valueArr[1]));
    }
}
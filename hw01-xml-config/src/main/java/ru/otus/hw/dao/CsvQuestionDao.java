package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (InputStreamReader reader = new InputStreamReader(
                ClassLoader.getSystemResourceAsStream(fileNameProvider.getTestFileName()),
                StandardCharsets.UTF_8)) {
            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build();

            return csvToBean.parse().stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
        } catch (Exception e) {
            throw new QuestionReadException(
                    "Failed to read questions file: " + fileNameProvider.getTestFileName(), e);
        }
    }
}

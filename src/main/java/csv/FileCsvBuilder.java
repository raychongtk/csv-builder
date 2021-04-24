package csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.Optional;

import static csv.config.CsvConfig.DATE_TIME_FORMATTER;
import static csv.config.CsvConfig.DEFAULT_DELIMITER;
import static csv.config.CsvConfig.DEFAULT_TEXT;
import static csv.config.CsvConfig.DOUBLE_QUOTES;
import static csv.config.CsvConfig.NUMBER_AS_STRING_FORMAT;
import static csv.config.CsvConfig.QUOTE;
import static csv.config.CsvConfig.UTF8_BOM;

/**
 * @author raychong
 */
public class FileCsvBuilder implements CsvBuilder {
    private final Logger logger = LoggerFactory.getLogger(FileCsvBuilder.class);
    private final BufferedWriter writer;
    private final String fileName;
    private final String delimiter;
    private final boolean numberAsString; // for applying NUMBER_AS_STRING_FORMAT to data, make excel to interpret value as text
    private boolean shouldAppendDelimiter = false;

    public FileCsvBuilder(String fileName) throws IOException {
        this(fileName, DEFAULT_DELIMITER, false, true);
    }

    public FileCsvBuilder(String fileName, String delimiter, boolean numberAsString, boolean appendBOM) throws IOException {
        this.fileName = fileName;
        this.delimiter = delimiter;
        this.numberAsString = numberAsString;
        writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        if (appendBOM) writer.write(UTF8_BOM);
    }

    @Override
    public CsvBuilder append(String text) {
        String value = Optional.ofNullable(text).orElse(DEFAULT_TEXT);
        String escapedText = value.replace(String.valueOf(QUOTE), DOUBLE_QUOTES);
        String preparedText = numberAsString ? String.format(NUMBER_AS_STRING_FORMAT, escapedText) : escapedText;
        insert(preparedText);
        return this;
    }

    @Override
    public CsvBuilder append(BigDecimal bigDecimal) {
        String preparedText = Optional.ofNullable(bigDecimal).isEmpty() ? DEFAULT_TEXT : bigDecimal.toString();
        insert(preparedText);
        return this;
    }

    @Override
    public CsvBuilder append(ZonedDateTime zonedDateTime) {
        String preparedText = Optional.ofNullable(zonedDateTime).isEmpty() ? DEFAULT_TEXT : zonedDateTime.format(DATE_TIME_FORMATTER);
        insert(preparedText);
        return this;
    }

    @Override
    public CsvBuilder append(Enum<?> enumValue) {
        String preparedText = Optional.ofNullable(enumValue).isEmpty() ? DEFAULT_TEXT : enumValue.name();
        insert(preparedText);
        return this;
    }

    @Override
    public CsvBuilder appendMoney(BigDecimal money) {
        var moneyFormat = new DecimalFormat("###,###.##");
        String preparedText = Optional.ofNullable(money).isEmpty() ? DEFAULT_TEXT : moneyFormat.format(money);
        insert(preparedText);
        return this;
    }

    @Override
    public void appendNextLine() {
        try {
            writer.newLine();
            shouldAppendDelimiter = false;
        } catch (IOException ex) {
            logger.error("cannot write csv file", ex);
        }
    }

    @Override
    public String build() {
        try {
            writer.close();
        } catch (IOException ex) {
            logger.error("cannot write csv file", ex);
        }
        return Paths.get(fileName).toString();
    }

    private void insert(String text) {
        try {
            if (shouldAppendDelimiter) writer.append(delimiter);
            writer.append(QUOTE).append(text).append(QUOTE);
            shouldAppendDelimiter = true;
        } catch (IOException ex) {
            logger.error("cannot write csv file", ex);
        }
    }
}

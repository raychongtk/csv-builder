package csv;

import csv.format.CsvFormatter;
import csv.format.CsvFormatterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Strings;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.Optional;

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
    private final CsvFormatter formatter;
    private boolean shouldAppendDelimiter = false;

    public FileCsvBuilder(String fileName) throws IOException {
        this(fileName, CsvFormatterFactory.create(), true);
    }

    public FileCsvBuilder(String fileName, CsvFormatter formatter, boolean appendBOM) throws IOException {
        this.fileName = fileName;
        this.formatter = formatter;
        writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        if (appendBOM) writer.write(UTF8_BOM);
    }

    @Override
    public CsvBuilder append(String text) {
        String value = Optional.ofNullable(text).orElse(formatter.defaultText());
        String escapedText = value.replace(String.valueOf(QUOTE), DOUBLE_QUOTES);
        String preparedText = formatter.numberAsString() ? Strings.format(NUMBER_AS_STRING_FORMAT, escapedText) : escapedText;
        insert(preparedText);
        return this;
    }

    @Override
    public CsvBuilder append(BigDecimal bigDecimal) {
        String preparedText = Optional.ofNullable(bigDecimal).isEmpty() ? formatter.defaultText() : bigDecimal.toString();
        insert(preparedText);
        return this;
    }

    @Override
    public CsvBuilder append(ZonedDateTime zonedDateTime) {
        String preparedText = Optional.ofNullable(zonedDateTime).isEmpty() ? formatter.defaultText() : zonedDateTime.format(formatter.dateTimeFormat());
        insert(preparedText);
        return this;
    }

    @Override
    public CsvBuilder append(Enum<?> enumValue) {
        String preparedText = Optional.ofNullable(enumValue).isEmpty() ? formatter.defaultText() : enumValue.name();
        insert(preparedText);
        return this;
    }

    @Override
    public CsvBuilder appendMoney(BigDecimal money) {
        String preparedText = Optional.ofNullable(money).isEmpty() ? formatter.defaultText() : formatter.moneyFormat().format(money);
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
            if (shouldAppendDelimiter) writer.append(formatter.delimiter());
            writer.append(QUOTE).append(text).append(QUOTE);
            shouldAppendDelimiter = true;
        } catch (IOException ex) {
            logger.error("cannot write csv file", ex);
        }
    }
}

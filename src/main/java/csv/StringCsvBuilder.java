package csv;

import csv.format.CsvFormatter;
import csv.format.CsvFormatterFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.Optional;

import static csv.config.CsvConfig.DOUBLE_QUOTES;
import static csv.config.CsvConfig.NEW_LINE;
import static csv.config.CsvConfig.NUMBER_AS_STRING_FORMAT;
import static csv.config.CsvConfig.QUOTE;
import static csv.config.CsvConfig.UTF8_BOM;

/**
 * @author raychong
 */
public class StringCsvBuilder implements CsvBuilder {
    private final StringBuilder builder;
    private final CsvFormatter formatter;
    private boolean shouldAppendDelimiter = false;

    public StringCsvBuilder() {
        this(CsvFormatterFactory.create(), true);
    }

    public StringCsvBuilder(CsvFormatter formatter, boolean appendBOM) {
        this.formatter = formatter;
        if (appendBOM) {
            builder = new StringBuilder(UTF8_BOM);
        } else {
            builder = new StringBuilder();
        }
    }

    public CsvBuilder append(String text) {
        String value = Optional.ofNullable(text).orElse(formatter.defaultText);
        String escapedText = value.replace(String.valueOf(QUOTE), DOUBLE_QUOTES);
        String preparedText = formatter.numberAsString ? String.format(NUMBER_AS_STRING_FORMAT, escapedText) : escapedText;
        insert(preparedText);
        return this;
    }

    public CsvBuilder append(BigDecimal bigDecimal) {
        String preparedText = Optional.ofNullable(bigDecimal).isEmpty() ? formatter.defaultText : bigDecimal.toString();
        insert(preparedText);
        return this;
    }

    public CsvBuilder append(ZonedDateTime zonedDateTime) {
        String preparedText = Optional.ofNullable(zonedDateTime).isEmpty() ? formatter.defaultText : zonedDateTime.format(formatter.dateTimeFormatter);
        insert(preparedText);
        return this;
    }

    public CsvBuilder append(Enum<?> enumValue) {
        String preparedText = Optional.ofNullable(enumValue).isEmpty() ? formatter.defaultText : enumValue.name();
        insert(preparedText);
        return this;
    }

    public CsvBuilder appendMoney(BigDecimal money) {
        var moneyFormat = new DecimalFormat("###,###.##");
        String preparedText = Optional.ofNullable(money).isEmpty() ? formatter.defaultText : formatter.moneyFormat.format(money);
        insert(preparedText);
        return this;
    }

    public void appendNextLine() {
        builder.append(NEW_LINE);
        shouldAppendDelimiter = false;
    }

    public String build() {
        return builder.toString();
    }

    private void insert(String text) {
        if (shouldAppendDelimiter) builder.append(formatter.delimiter);
        builder.append(QUOTE).append(text).append(QUOTE);
        shouldAppendDelimiter = true;
    }
}

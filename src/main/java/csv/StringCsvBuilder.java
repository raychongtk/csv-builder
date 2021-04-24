package csv;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.Optional;

import static csv.config.CsvConfig.DATE_TIME_FORMATTER;
import static csv.config.CsvConfig.DEFAULT_DELIMITER;
import static csv.config.CsvConfig.DEFAULT_TEXT;
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
    private final String separator;
    private final boolean numberAsString; // for applying NUMBER_AS_STRING_FORMAT to data, make excel to interpret value as text
    private boolean shouldWriteComma = false;

    public StringCsvBuilder() {
        this(DEFAULT_DELIMITER, false, true);
    }

    public StringCsvBuilder(String separator, boolean numberAsString, boolean appendBOM) {
        this.separator = separator;
        this.numberAsString = numberAsString;
        if (appendBOM) {
            builder = new StringBuilder(UTF8_BOM);
        } else {
            builder = new StringBuilder();
        }
    }

    public CsvBuilder append(String text) {
        String value = Optional.ofNullable(text).orElse(DEFAULT_TEXT);
        String escapedText = value.replace(String.valueOf(QUOTE), DOUBLE_QUOTES);
        String preparedText = numberAsString ? String.format(NUMBER_AS_STRING_FORMAT, escapedText) : escapedText;
        insert(preparedText);
        return this;
    }

    public CsvBuilder append(BigDecimal bigDecimal) {
        String preparedText = Optional.ofNullable(bigDecimal).isEmpty() ? DEFAULT_TEXT : bigDecimal.toString();
        insert(preparedText);
        return this;
    }

    public CsvBuilder append(ZonedDateTime zonedDateTime) {
        String preparedText = Optional.ofNullable(zonedDateTime).isEmpty() ? DEFAULT_TEXT : zonedDateTime.format(DATE_TIME_FORMATTER);
        insert(preparedText);
        return this;
    }

    public CsvBuilder append(Enum<?> enumValue) {
        String preparedText = Optional.ofNullable(enumValue).isEmpty() ? DEFAULT_TEXT : enumValue.name();
        insert(preparedText);
        return this;
    }

    public CsvBuilder appendMoney(BigDecimal money) {
        var moneyFormat = new DecimalFormat("###,###.##");
        String preparedText = Optional.ofNullable(money).isEmpty() ? DEFAULT_TEXT : moneyFormat.format(money);
        insert(preparedText);
        return this;
    }

    public void appendNextLine() {
        builder.append(NEW_LINE);
        shouldWriteComma = false;
    }

    public String build() {
        return builder.toString();
    }

    private void insert(String text) {
        if (shouldWriteComma) builder.append(separator);
        builder.append(QUOTE).append(text).append(QUOTE);
        shouldWriteComma = true;
    }
}

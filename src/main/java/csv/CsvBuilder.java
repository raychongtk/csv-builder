package csv;

import util.ZoneIds;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author raychong
 */
public class CsvBuilder {
    private static final char NEW_LINE = '\n';
    private static final char DELIMITER = ',';
    private static final char QUOTE = '\"';
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneIds.ASIA_SHANGHAI);
    private static final String DEFAULT_TEXT = "-";
    private static final String DOUBLE_QUOTES = "\"\"";
    private static final String NUMBER_AS_STRING_FORMAT = "=\"\"%s\"\"";
    private static final String UTF8_BOM = "\uFEFF"; // UTF-8 Byte Order Mark (BOM) is used to tell excel that this file is encoded by Unicode

    private final StringBuilder builder;
    public boolean numberAsString = true; // for applying NUMBER_AS_STRING_FORMAT to data, make excel to interpret value as text
    private boolean shouldWriteComma = false;

    public CsvBuilder() {
        builder = new StringBuilder(UTF8_BOM);
    }

    public CsvBuilder(boolean appendBOM) {
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

    public byte[] build() {
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void insert(String text) {
        if (shouldWriteComma) builder.append(DELIMITER);
        builder.append(QUOTE).append(text).append(QUOTE);
        shouldWriteComma = true;
    }
}

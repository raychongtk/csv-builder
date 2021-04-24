package csv.format;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import static csv.config.CsvConfig.DATE_TIME_FORMATTER;
import static csv.config.CsvConfig.DEFAULT_DECIMAL_FORMATTER;
import static csv.config.CsvConfig.DEFAULT_DELIMITER;
import static csv.config.CsvConfig.DEFAULT_TEXT;

/**
 * @author raychong
 */
public class CsvFormatterFactory {
    public static CsvFormatter create() {
        var formatter = new CsvFormatter();
        formatter.money(DEFAULT_DECIMAL_FORMATTER);
        formatter.dateTime(DATE_TIME_FORMATTER);
        formatter.delimiter(DEFAULT_DELIMITER);
        formatter.defaultText(DEFAULT_TEXT);
        formatter.numberAsString(false);
        return formatter;
    }

    public static CsvFormatter create(DecimalFormat moneyFormat, DateTimeFormatter dateTimeFormat, String delimiter, String defaultText, boolean numberAsString) {
        var formatter = new CsvFormatter();
        formatter.money(moneyFormat);
        formatter.dateTime(dateTimeFormat);
        formatter.delimiter(delimiter);
        formatter.defaultText(defaultText);
        formatter.numberAsString(numberAsString);
        return formatter;
    }
}

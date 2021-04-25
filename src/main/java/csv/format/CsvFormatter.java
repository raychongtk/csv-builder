package csv.format;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class CsvFormatter {
    private DecimalFormat moneyFormat;
    private DateTimeFormatter dateTimeFormatter;
    private String delimiter;
    private String defaultText;
    private boolean numberAsString; // for applying NUMBER_AS_STRING_FORMAT to data, make excel to interpret value as text

    void money(DecimalFormat format) {
        moneyFormat = format;
    }

    void dateTime(DateTimeFormatter format) {
        dateTimeFormatter = format;
    }

    void delimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    void defaultText(String text) {
        this.defaultText = text;
    }

    void numberAsString(boolean numberAsString) {
        this.numberAsString = numberAsString;
    }

    public DecimalFormat moneyFormat() {
        return moneyFormat;
    }

    public DateTimeFormatter dateTimeFormat() {
        return dateTimeFormatter;
    }

    public String delimiter() {
        return delimiter;
    }

    public String defaultText() {
        return defaultText;
    }

    public boolean numberAsString() {
        return numberAsString;
    }
}

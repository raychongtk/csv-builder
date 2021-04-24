package csv.format;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class CsvFormatter {
    public DecimalFormat moneyFormat;
    public DateTimeFormatter dateTimeFormatter;
    public String delimiter;
    public String defaultText;
    public boolean numberAsString; // for applying NUMBER_AS_STRING_FORMAT to data, make excel to interpret value as text

    public void money(DecimalFormat format) {
        moneyFormat = format;
    }

    public void dateTime(DateTimeFormatter format) {
        dateTimeFormatter = format;
    }

    public void delimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void defaultText(String text) {
        this.defaultText = text;
    }

    public void numberAsString(boolean numberAsString) {
        this.numberAsString = numberAsString;
    }
}

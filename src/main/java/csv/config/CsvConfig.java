package csv.config;

import util.ZoneIds;

import java.time.format.DateTimeFormatter;

/**
 * @author raychong
 */
public class CsvConfig {
    public static final char NEW_LINE = '\n';
    public static final char QUOTE = '\"';
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneIds.ASIA_SHANGHAI);
    public static final String DEFAULT_DELIMITER = ",";
    public static final String DEFAULT_TEXT = "-";
    public static final String DOUBLE_QUOTES = "\"\"";
    public static final String NUMBER_AS_STRING_FORMAT = "=\"\"%s\"\"";
    public static final String UTF8_BOM = "\uFEFF"; // UTF-8 Byte Order Mark (BOM) is used to tell excel that this file is encoded by Unicode
}

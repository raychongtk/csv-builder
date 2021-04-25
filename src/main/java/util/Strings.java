package util;

import org.slf4j.helpers.MessageFormatter;

/**
 * @author raychong
 */
public class Strings {
    public static String format(String pattern, Object... args) {
        return MessageFormatter.arrayFormat(pattern, args).getMessage();
    }
}

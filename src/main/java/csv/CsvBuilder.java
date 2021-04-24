package csv;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author raychong
 */
public interface CsvBuilder {
    CsvBuilder append(String text);

    CsvBuilder append(BigDecimal bigDecimal);

    CsvBuilder append(ZonedDateTime zonedDateTime);

    CsvBuilder append(Enum<?> enumValue);

    CsvBuilder appendMoney(BigDecimal money);

    void appendNextLine();

    String build();
}

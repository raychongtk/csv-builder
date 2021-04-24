package csv;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CsvBuilderTest {
    @Test
    void csv() {
        CsvBuilder builder = new StringCsvBuilder();
        builder.appendMoney(BigDecimal.valueOf(12123123.59));
        builder.appendMoney(BigDecimal.valueOf(123.59));
        builder.appendMoney(BigDecimal.valueOf(12.00));
        builder.appendMoney(BigDecimal.valueOf(12.50));
        builder.appendMoney(BigDecimal.valueOf(12.57));
        builder.appendNextLine();
        assertThat(builder.build()).isEqualTo("\uFEFF\"12,123,123.59\",\"123.59\",\"12\",\"12.5\",\"12.57\"\n");
    }
}

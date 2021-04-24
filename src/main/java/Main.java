import csv.CsvBuilder;
import csv.FileCsvBuilder;
import csv.StringCsvBuilder;
import csv.format.CsvFormatter;
import csv.format.CsvFormatterFactory;
import util.ZoneIds;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author raychong
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // build string in memory
        CsvBuilder builder = new StringCsvBuilder();
        builder.append("abc");
        builder.append("cde");
        builder.appendMoney(BigDecimal.TEN);
        builder.append(ZonedDateTime.now());
        builder.appendNextLine(); // next row
        System.out.println(builder.build());

        // build large file with BufferedWriter
        DecimalFormat moneyFormat = new DecimalFormat("###,###.##");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneIds.ASIA_SHANGHAI);
        CsvFormatter csvFormatter = CsvFormatterFactory.create(moneyFormat, dateTimeFormatter, ",", "N/A", false);
        CsvBuilder fileBuilder = new FileCsvBuilder("test.csv", csvFormatter, true);
        fileBuilder.append("header1");
        fileBuilder.append("header2");
        fileBuilder.append("header3");
        fileBuilder.append("header4");
        fileBuilder.appendNextLine();
        for (int i = 0; i < 100; i++) {
            fileBuilder.append("data1,data2");
            fileBuilder.append("data2");
            fileBuilder.appendMoney(BigDecimal.TEN);
            fileBuilder.append(ZonedDateTime.now());
            fileBuilder.appendNextLine();
        }
        System.out.println(fileBuilder.build()); // file path
    }
}

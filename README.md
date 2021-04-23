# CSV Builder

This project aims to design a lightweight CSV builder for dealing with simple CSV/Excel file.

# Usage

```java
public class Main {
    public static void main(String[] args) {
        var builder = new CsvBuilder(true); // instantiate with UTF BOM
        builder.append("abc");
        builder.append("cde");
        builder.appendMoney(BigDecimal.TEN);
        builder.append(ZonedDateTime.now());
        builder.appendNextLine(); // next row
        System.out.println(new String(builder.build(), StandardCharsets.UTF_8));
    }
}
```

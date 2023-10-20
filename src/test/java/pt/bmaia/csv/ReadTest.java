package pt.bmaia.csv;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.bmaia.csv.reading.parsing.records.RecordParser;
import pt.bmaia.csv.reading.parsing.records.RecordsParserImpl;
import pt.procurainterna.text.charsinput.CharsInputs;

public class ReadTest {

  @Test
  void testUnqualifiedCsv() {
    final String csv = "a;b;c\r\n";

    final RecordParser<List<CharSequence>> recordsParser = new RecordsParserImpl('"', ';', new char[]{'\r', '\n'});

    final List<List<CharSequence>> records = new ArrayList<>();

    recordsParser.parse(CharsInputs.fromCharSequence(csv), records::add);

    final List<CharSequence> firstRecord = records.get(0);

    Assertions.assertEquals("a", firstRecord.get(0).toString());
    Assertions.assertEquals("b", firstRecord.get(1).toString());
    Assertions.assertEquals("c", firstRecord.get(2).toString());
  }

  @Test
  void testQualifiedCsv() {
    final String csv = "\"a\";\"b\";\"c\"\r\n";

    final RecordParser<List<CharSequence>> recordsParser = new RecordsParserImpl('"', ';', new char[]{'\r', '\n'});

    final List<List<CharSequence>> records = new ArrayList<>();

    recordsParser.parse(CharsInputs.fromCharSequence(csv), records::add);

    final List<CharSequence> firstRecord = records.get(0);

    Assertions.assertEquals("a", firstRecord.get(0).toString());
    Assertions.assertEquals("b", firstRecord.get(1).toString());
    Assertions.assertEquals("c", firstRecord.get(2).toString());
  }
  @Test
  void testMixedCsv() {
    final String csv = "a;\"b\";\"c\"\r\n";

    final RecordParser<List<CharSequence>> recordsParser = new RecordsParserImpl('"', ';', new char[]{'\r', '\n'});

    final List<List<CharSequence>> records = new ArrayList<>();

    recordsParser.parse(CharsInputs.fromCharSequence(csv), records::add);

    final List<CharSequence> firstRecord = records.get(0);

    Assertions.assertEquals("a", firstRecord.get(0).toString());
    Assertions.assertEquals("b", firstRecord.get(1).toString());
    Assertions.assertEquals("c", firstRecord.get(2).toString());
  }

}

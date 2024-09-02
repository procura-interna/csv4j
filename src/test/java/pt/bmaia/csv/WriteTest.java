package pt.bmaia.csv;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.bmaia.csv.format.FormatSequencesConfiguration;
import pt.bmaia.csv.format.ImmutableFormatSequencesConfiguration;
import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.bmaia.csv.writing.delimiterappender.DelimiterAppenders;
import pt.bmaia.csv.writing.escaper.StandardEscapeChecker;
import pt.bmaia.csv.writing.recordappender.RecordAppender;
import pt.bmaia.csv.writing.recordappender.RecordAppenders;
import pt.bmaia.csv.writing.recordsappender.RecordsAppender;
import pt.bmaia.csv.writing.recordsappender.RecordsAppenders;
import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.bmaia.csv.writing.valueappender.ValueAppenders;
import pt.bmaia.csv.writing.escaper.Escaper;
import pt.bmaia.csv.writing.escaper.Escapers;
import pt.bmaia.csv.writing.qualifierappender.QualifierAppender;
import pt.bmaia.csv.writing.qualifierappender.QualifierAppenders;
import pt.bmaia.csv.writing.serializing.outputstream.BufferedValuesRecordsToBytesOutput;
import pt.bmaia.csv.writing.serializing.outputstream.RecordsToBytesOutput;
import pt.procurainterna.lang.io.bytesoutput.BytesOutput;
import pt.procurainterna.lang.io.bytesoutput.BytesOutputs;

public class WriteTest {

  @Test
  void test() {
    final List<List<String>> records = records();

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    final BytesOutput bytesOutput = BytesOutputs.fromOutputStream(outputStream);
    implementationToTest().writeInto(records.iterator(), bytesOutput);

    final String expectedString = new String(expectedResult(), StandardCharsets.UTF_8);
    final String resultString = outputStream.toString(StandardCharsets.UTF_8);

    Assertions.assertEquals(expectedString, resultString);
  }

  protected RecordsToBytesOutput<Iterator<List<String>>> implementationToTest() {
    final DelimiterAppender valueDelimiter = DelimiterAppenders.fromChar(';');
    final Escaper qualifierEscaper = Escapers.fromChar('"');
    final QualifierAppender qualifierAppender = QualifierAppenders.fromChar('"');

    final ValueAppender valueAppender =
        ValueAppenders.alwaysQualified(qualifierEscaper, qualifierAppender);

    final RecordAppender<List<String>> recordAppender =
        RecordAppenders.iterableBased(valueAppender, valueDelimiter);

    final RecordsAppender<Iterator<List<String>>> recordsAppender =
        RecordsAppenders.iteratorBased(recordAppender,
            DelimiterAppenders.fromCharArray("\r\n".toCharArray()));

    return new BufferedValuesRecordsToBytesOutput<>(recordsAppender, StandardCharsets.UTF_8);
  }

  protected FormatSequencesConfiguration csvOptions() {
    return new ImmutableFormatSequencesConfiguration("\"", ";", "\n");
  }

  private static List<List<String>> records() {
    return Arrays.asList(Arrays.asList("1", "2", "3"), Arrays.asList("a", "b", "c"),
        Arrays.asList("\"x\"", "\";y;\"", ";\"z\";"));
  }

  private byte[] expectedResult() {
    final String valueSeparator = ";";
    final String recordSeparator = "\r\n";
    final String textQualifier = "\"";

    return (textQualifier + "1" + textQualifier + valueSeparator + textQualifier + "2"
        + textQualifier + valueSeparator + textQualifier + "3" + textQualifier + recordSeparator
        + textQualifier + "a" + textQualifier + valueSeparator + textQualifier + "b" + textQualifier
        + valueSeparator + textQualifier + "c" + textQualifier + recordSeparator + textQualifier
        + "\"\"x\"\"" + textQualifier + valueSeparator + textQualifier + "\"\";y;\"\""
        + textQualifier + valueSeparator + textQualifier + ";\"\"z\"\";" + textQualifier).getBytes(
        StandardCharsets.UTF_8);
  }
}

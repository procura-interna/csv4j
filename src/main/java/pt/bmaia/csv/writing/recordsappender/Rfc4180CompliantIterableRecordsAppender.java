package pt.bmaia.csv.writing.recordsappender;

import java.util.Iterator;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class Rfc4180CompliantIterableRecordsAppender implements
    RecordsAppender<Iterator<Iterable<String>>> {

  private static final char[] RECORD_SEPARATOR = {'\r', '\n'};
  private static final char QUALIFIER = '"';
  private static final char VALUE_SEPARATOR = ',';

  @Override
  public void writeTo(final Iterator<Iterable<String>> records, final CharsOutput charsOutput) {
    while (records.hasNext()){
      final Iterator<String> record = records.next().iterator();

      while (record.hasNext()) {
        final String value = record.next();

        charsOutput.writeChar(QUALIFIER);
        escapeValue(charsOutput, value);
        charsOutput.writeChar(QUALIFIER);

        if (record.hasNext()) {
          charsOutput.writeChar(VALUE_SEPARATOR);
        }
      }

      charsOutput.writeCharArray(RECORD_SEPARATOR);
    }
  }

  private static void escapeValue(final CharsOutput charsOutput, final String value) {
    for (int i = 0, limit = value.length(); i < limit; i++) {
      final char currentChar = value.charAt(i);
      charsOutput.writeChar(currentChar);

      if (currentChar == QUALIFIER) {
        charsOutput.writeChar(QUALIFIER);
      }
    }
  }

}

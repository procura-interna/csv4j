package pt.bmaia.csv.writing.recordsappender;

import pt.bmaia.csv.writing.recordsappender.RecordsAppender;
import pt.procurainterna.text.charsoutput.CharsOutput;

public class Rfc4180CompliantCharMatrixRecordsAppender implements RecordsAppender<char[][][]> {

  private static final char[] RECORD_SEPARATOR = {'\r', '\n'};
  private static final char QUALIFIER = '"';
  private static final char[] QUALIFIER_AND_VALUE_SEPARATOR = {QUALIFIER, ','};

  @Override
  public void writeTo(final char[][][] records, final CharsOutput charsOutput) {
    for (final char[][] record : records) {
      for (int attribute = 0, limit = record.length - 1; attribute < limit; attribute++) {
        final char[] value = record[attribute];

        charsOutput.writeChar(QUALIFIER);
        escapeValue(charsOutput, value);
        charsOutput.writeCharArray(QUALIFIER_AND_VALUE_SEPARATOR);
      }

      charsOutput.writeChar(QUALIFIER);
      escapeValue(charsOutput, record[record.length - 1]);
      charsOutput.writeChar(QUALIFIER);

      charsOutput.writeCharArray(RECORD_SEPARATOR);
    }
  }

  private static void escapeValue(final CharsOutput charsOutput, final char[] value) {
    for (int i = 0, limit = value.length; i < limit; i++) {
      final char currentChar = value[i];
      charsOutput.writeChar(currentChar);

      if (currentChar == QUALIFIER) {
        charsOutput.writeChar(QUALIFIER);
      }
    }
  }

}

package pt.bmaia.csv.writing.recordsappender;

import java.util.Iterator;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class Rfc4180CompliantCharSequenceArraysRecordsAppender implements RecordsAppender<Iterator<CharSequence[]>> {

  private static final char[] RECORD_SEPARATOR = {'\r', '\n'};
  private static final char QUALIFIER = '"';
  private static final char[] QUALIFIER_AND_VALUE_SEPARATOR = {QUALIFIER, ','};

  @Override
  public void writeTo(final Iterator<CharSequence[]> records, final CharsOutput charsOutput) {
    while (records.hasNext()){
      final CharSequence[] record = records.next();

      for (int attribute = 0; attribute < record.length - 1; attribute++) {
        final CharSequence value = record[attribute];

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

  private static void escapeValue(final CharsOutput charsOutput, final CharSequence value) {
    for (int i = 0, limit = value.length(); i < limit; i++) {
      final char currentChar = value.charAt(i);
      charsOutput.writeChar(currentChar);

      if (currentChar == QUALIFIER) {
        charsOutput.writeChar(QUALIFIER);
      }
    }
  }

}

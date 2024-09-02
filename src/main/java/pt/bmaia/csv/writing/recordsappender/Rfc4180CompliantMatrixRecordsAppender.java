package pt.bmaia.csv.writing.recordsappender;


import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class Rfc4180CompliantMatrixRecordsAppender implements RecordsAppender<String[][]> {

  private static final char[] RECORD_SEPARATOR = {'\r', '\n'};
  private static final char QUALIFIER = '"';
  private static final char[] QUALIFIER_AND_VALUE_SEPARATOR = {QUALIFIER, ','};

  @Override
  public void writeTo(final String[][] records, final CharsOutput charsOutput) {
    for (final String[] record : records) {
      for (int attribute = 0; attribute < record.length - 1; attribute++) {
        final String value = record[attribute];

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

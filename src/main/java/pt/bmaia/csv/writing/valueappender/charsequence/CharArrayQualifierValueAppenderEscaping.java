package pt.bmaia.csv.writing.valueappender.charsequence;

import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class CharArrayQualifierValueAppenderEscaping<C extends CharSequence> extends AbstractAlwaysQualifiedValueAppender<C>{

  private final char[] textQualifier;

  public CharArrayQualifierValueAppenderEscaping(final char[] textQualifier) {
    this.textQualifier = textQualifier;
  }

  @Override
  protected void appendValue(final CharsOutput charsOutput, final C value) {
    if (value != null) {
      final int qualifierLength = textQualifier.length;
      final int valueLength = value.length();

      int matchedTextQualifier = 0;
      for (int i = 0; i < valueLength; i++) {
        final char currentChar = value.charAt(i);

        charsOutput.writeChar(currentChar);

        if (currentChar == textQualifier[matchedTextQualifier]) {
          matchedTextQualifier++;

          if (matchedTextQualifier == qualifierLength) {
            appendQualifier(charsOutput);
            matchedTextQualifier = 0;
          }

        } else {
          matchedTextQualifier = 0;
        }
      }
    }
  }

  @Override
  protected void appendQualifier(final CharsOutput charsOutput) {
    charsOutput.writeCharArray(textQualifier);
  }

}

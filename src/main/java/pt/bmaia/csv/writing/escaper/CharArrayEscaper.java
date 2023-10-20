package pt.bmaia.csv.writing.escaper;

import pt.procurainterna.text.charsinput.CharsInput;
import pt.procurainterna.text.charsoutput.CharsOutput;

public class CharArrayEscaper implements Escaper {

  private final char[] sequence;

  public CharArrayEscaper(final char[] sequence) {
    this.sequence = sequence;
  }

  @Override
  public void escape(final CharsInput charsInput, final CharsOutput charsOutput) {
    internalEscape(charsInput, charsOutput);
  }

  private void internalEscape(final CharsInput charsInput, final CharsOutput charsOutput) {
    final int qualifierLength = sequence.length;

    int matchedTextQualifier = 0;
    while (!charsInput.isDone()) {
      final char currentChar = charsInput.readChar();

      charsOutput.writeChar(currentChar);

      if (currentChar == sequence[matchedTextQualifier]) {
        matchedTextQualifier++;

        if (matchedTextQualifier == qualifierLength) {
          charsOutput.writeCharArray(sequence);
          matchedTextQualifier = 0;
        }

      } else {
        matchedTextQualifier = 0;
      }
    }
  }

}

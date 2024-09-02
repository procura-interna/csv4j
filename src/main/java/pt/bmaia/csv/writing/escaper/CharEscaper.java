package pt.bmaia.csv.writing.escaper;

import pt.procurainterna.lang.text.charsinput.CharsInput;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class CharEscaper implements Escaper {

  private final char sequence;

  public CharEscaper(final char sequence) {
    this.sequence = sequence;
  }

  @Override
  public void escape(final CharsInput charsInput, final CharsOutput charsOutput) {
    while (!charsInput.isDone()) {
      final char currentChar = charsInput.readChar();

      charsOutput.writeChar(currentChar);

      if (currentChar == sequence) {
        charsOutput.writeChar(sequence);
      }
    }
  }

}

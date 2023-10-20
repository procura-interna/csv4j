package pt.bmaia.csv.writing.escaper;

import pt.procurainterna.text.charsinput.CharsInput;
import pt.procurainterna.text.charsoutput.CharsOutput;

public class NotEscapingEscaper implements Escaper {

  @Override
  public void escape(final CharsInput charsInput, final CharsOutput charsOutput) {
    while (!charsInput.isDone()) {
      final char currentChar = charsInput.readChar();
      charsOutput.writeChar(currentChar);
    }
  }

}

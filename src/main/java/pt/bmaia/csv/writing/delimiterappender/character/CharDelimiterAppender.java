package pt.bmaia.csv.writing.delimiterappender.character;

import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.procurainterna.text.charsoutput.CharsOutput;

public class CharDelimiterAppender implements DelimiterAppender {

  private final char character;

  public CharDelimiterAppender(final char character) {
    this.character = character;
  }

  @Override
  public void writeTo(final CharsOutput appendable) {
    appendable.writeChar(character);
  }
}

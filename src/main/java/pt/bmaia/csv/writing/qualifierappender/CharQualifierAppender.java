package pt.bmaia.csv.writing.qualifierappender;

import pt.procurainterna.text.charsoutput.CharsOutput;

public class CharQualifierAppender implements QualifierAppender {

  private final char qualifier;

  public CharQualifierAppender(final char qualifier) {
    this.qualifier = qualifier;
  }

  @Override
  public void append(final CharsOutput charsOutput) {
    charsOutput.writeChar(qualifier);
  }

}

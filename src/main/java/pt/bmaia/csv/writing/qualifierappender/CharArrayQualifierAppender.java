package pt.bmaia.csv.writing.qualifierappender;

import pt.procurainterna.text.charsoutput.CharsOutput;

public class CharArrayQualifierAppender implements QualifierAppender {

  private final char[] qualifier;

  public CharArrayQualifierAppender(final char[] qualifier) {
    this.qualifier = qualifier;
  }

  @Override
  public void append(final CharsOutput charsOutput) {
      charsOutput.writeCharArray(qualifier);
  }

}

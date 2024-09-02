package pt.bmaia.csv.writing.qualifierappender;

import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class NoOpQualifierAppender implements QualifierAppender {

  @Override
  public void append(final CharsOutput charsOutput) {
    // Do nothing
  }

}

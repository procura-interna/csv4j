package pt.bmaia.csv.writing.qualifierappender;

import pt.procurainterna.text.charsoutput.CharsOutput;

public class NoOpQualifierAppender implements QualifierAppender {

  @Override
  public void append(final CharsOutput charsOutput) {
    // Do nothing
  }

}

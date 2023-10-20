package pt.bmaia.csv.writing.delimiterappender;


import pt.procurainterna.text.charsoutput.CharsOutput;

public class NoOpDelimiterAppender implements DelimiterAppender {

  @Override
  public void writeTo(final CharsOutput appendable) {
    // Do nothing
  }
}

package pt.bmaia.csv.writing.delimiterappender.chararray;

import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class CharArrayDelimiterAppender implements DelimiterAppender {

  private final char[] delimiter;

  public CharArrayDelimiterAppender(final char[] delimiter) {
    this.delimiter = delimiter;
  }

  @Override
  public void writeTo(final CharsOutput charsOutput) {
    charsOutput.writeCharArray(delimiter);
  }

}

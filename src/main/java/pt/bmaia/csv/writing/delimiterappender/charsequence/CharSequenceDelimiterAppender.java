package pt.bmaia.csv.writing.delimiterappender.charsequence;

import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.procurainterna.text.charsoutput.CharsOutput;

public class CharSequenceDelimiterAppender implements DelimiterAppender {

  private final CharSequence charSequence;

  public CharSequenceDelimiterAppender(final CharSequence charSequence) {
    this.charSequence = charSequence;
  }

  @Override
  public void writeTo(final CharsOutput charsOutput) {
    charsOutput.writeCharSequence(charSequence);
  }

}

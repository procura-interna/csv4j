package pt.bmaia.csv.writing.valueappender.charsequence;

import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class DirectValueAppender<C extends CharSequence> implements ValueAppender<C> {

  @Override
  public void append(final CharsOutput charsOutput, final C value) {
    charsOutput.writeCharSequence(value);
  }

}

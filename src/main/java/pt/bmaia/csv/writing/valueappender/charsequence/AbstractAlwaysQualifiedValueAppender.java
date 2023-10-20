package pt.bmaia.csv.writing.valueappender.charsequence;

import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.procurainterna.text.charsoutput.CharsOutput;

public abstract class AbstractAlwaysQualifiedValueAppender<C extends CharSequence> implements ValueAppender<C> {

  @Override
  public void append(final CharsOutput charsOutput, final C value) {
    print(value, charsOutput);
  }

  private void print(final C value, final CharsOutput charsOutput) {
    appendQualifier(charsOutput);

    appendValue(charsOutput, value);

    appendQualifier(charsOutput);
  }

  protected abstract void appendValue(CharsOutput charsOutput, C value);

  protected abstract void appendQualifier(CharsOutput charsOutput);

}

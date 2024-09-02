package pt.bmaia.csv.writing.valueappender.charsequence;

import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;

/**
 * Implementation of {@link ValueAppender} optimized for qualifier that are only one character.
 */
public class EscapingCharQualifierValueAppender<C extends CharSequence> extends AbstractAlwaysQualifiedValueAppender<C> {

  private final char qualifier;

  public EscapingCharQualifierValueAppender(final char qualifier) {
    this.qualifier = qualifier;
  }

  @Override
  protected void appendValue(final CharsOutput charsOutput, final C value) {
    final int length = value.length();

    for (int i = 0; i < length; i++) {
      final char currentChar = value.charAt(i);

      if (currentChar == qualifier) {
        charsOutput.writeChar(qualifier);
      }

      charsOutput.writeChar(currentChar);
    }
  }

  @Override
  protected void appendQualifier(final CharsOutput charsOutput) {
    charsOutput.writeChar(qualifier);
  }

}

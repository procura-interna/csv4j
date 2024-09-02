package pt.bmaia.csv.writing.valueappender.charsequence;

import pt.bmaia.csv.writing.escaper.EscapeChecker;
import pt.bmaia.csv.writing.escaper.Escaper;
import pt.bmaia.csv.writing.qualifierappender.QualifierAppender;
import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.procurainterna.lang.text.charsinput.CharsInputs;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class QualifyIfEscapedValueAppender<C extends CharSequence> implements ValueAppender<C> {

  private final EscapeChecker escapeChecker;
  private final Escaper escaper;
  private final QualifierAppender qualifierAppender;

  public QualifyIfEscapedValueAppender(final EscapeChecker escapeChecker, final Escaper escaper,
      final QualifierAppender qualifierAppender) {
    this.escapeChecker = escapeChecker;
    this.escaper = escaper;
    this.qualifierAppender = qualifierAppender;
  }

  @Override
  public void append(final CharsOutput charsOutput, final C value) {
    boolean escape = false;
    for (int i = 0, limit = value.length(); i < limit; i++) {
      final char currentChar = value.charAt(i);

      if (escapeChecker.needsToBeEscaped(currentChar)) {
        escape = true;
        break;
      }
    }

    if (escape) {
      qualifierAppender.append(charsOutput);
      escaper.escape(CharsInputs.fromCharSequence(value), charsOutput);
      qualifierAppender.append(charsOutput);

    } else {
      charsOutput.writeCharSequence(value);
    }
  }

}

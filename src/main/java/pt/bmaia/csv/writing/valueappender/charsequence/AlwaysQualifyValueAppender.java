package pt.bmaia.csv.writing.valueappender.charsequence;

import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.bmaia.csv.writing.escaper.Escaper;
import pt.bmaia.csv.writing.qualifierappender.QualifierAppender;
import pt.procurainterna.lang.text.charsinput.CharsInputs;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;

public class AlwaysQualifyValueAppender<C extends CharSequence> implements ValueAppender<C> {

  private final Escaper escaper;
  private final QualifierAppender qualifierAppender;

  public AlwaysQualifyValueAppender(final Escaper escaper,
      final QualifierAppender qualifierAppender) {
    this.escaper = escaper;
    this.qualifierAppender = qualifierAppender;
  }

  @Override
  public void append(final CharsOutput charsOutput, final C value) {
    qualifierAppender.append(charsOutput);
    escaper.escape(CharsInputs.fromCharSequence(value), charsOutput);
    qualifierAppender.append(charsOutput);
  }

}

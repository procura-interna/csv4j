package pt.bmaia.csv.writing.valueappender;

import pt.bmaia.csv.writing.escaper.StandardEscapeChecker;
import pt.bmaia.csv.writing.valueappender.charsequence.AlwaysQualifyValueAppender;
import pt.bmaia.csv.writing.valueappender.charsequence.DirectValueAppender;
import pt.bmaia.csv.writing.valueappender.charsequence.QualifyIfEscapedValueAppender;
import pt.bmaia.csv.writing.escaper.Escaper;
import pt.bmaia.csv.writing.escaper.Escapers;
import pt.bmaia.csv.writing.qualifierappender.QualifierAppender;
import pt.bmaia.csv.writing.qualifierappender.QualifierAppenders;

public final class ValueAppenders {

  private ValueAppenders() {
  }

  public static ValueAppender<CharSequence> direct() {
    return new DirectValueAppender<>();
  }

  public static ValueAppender<CharSequence> noOp() {
    return new NoOpValueAppender<>();
  }

  public static ValueAppender<CharSequence> escapeOnly(final CharSequence textQualifier) {
    final char[] qualifier = textQualifier.toString().toCharArray();

    final Escaper escaper = Escapers.optimizedFromCharArray(qualifier);
    final QualifierAppender qualifierAppender =
        QualifierAppenders.optimizedFromCharArray(qualifier);

    return new AlwaysQualifyValueAppender<>(escaper, qualifierAppender);
  }

  public static ValueAppender<CharSequence> alwaysQualified(final CharSequence textQualifier) {
    final char[] qualifier = textQualifier.toString().toCharArray();

    final Escaper escaper = Escapers.optimizedFromCharArray(qualifier);
    final QualifierAppender qualifierAppender =
        QualifierAppenders.optimizedFromCharArray(qualifier);

    return alwaysQualified(escaper, qualifierAppender);
  }

  public static ValueAppender<CharSequence> alwaysQualified(final Escaper escaper,
      final QualifierAppender qualifierAppender) {
    return new AlwaysQualifyValueAppender<>(escaper, qualifierAppender);
  }

  public static ValueAppender<CharSequence> qualifiedIfEscaped(final CharSequence textQualifier) {
    final char[] qualifier = textQualifier.toString().toCharArray();

    final Escaper escaper = Escapers.optimizedFromCharArray(qualifier);
    final QualifierAppender qualifierAppender =
        QualifierAppenders.optimizedFromCharArray(qualifier);

    return new QualifyIfEscapedValueAppender<>(new StandardEscapeChecker('"'), escaper, qualifierAppender);
  }

  public static ValueAppender<CharSequence> qualifiedIfEscaped(final Escaper escaper,
      final QualifierAppender qualifierAppender) {
    return new QualifyIfEscapedValueAppender<>(new StandardEscapeChecker('"'), escaper, qualifierAppender);
  }

}

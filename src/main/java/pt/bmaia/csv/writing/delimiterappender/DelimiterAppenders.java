package pt.bmaia.csv.writing.delimiterappender;

import pt.bmaia.csv.writing.delimiterappender.character.CharDelimiterAppender;
import pt.bmaia.csv.writing.delimiterappender.chararray.CharArrayDelimiterAppender;
import pt.bmaia.csv.writing.delimiterappender.charsequence.CharSequenceDelimiterAppender;

public final class DelimiterAppenders {

  private static final NoOpDelimiterAppender NO_OP_APPENDER = new NoOpDelimiterAppender();

  private DelimiterAppenders() {
  }

  public static DelimiterAppender fromChar(final char delimiter) {
    return new CharDelimiterAppender(delimiter);
  }

  public static DelimiterAppender fromCharArray(final char[] delimiter) {
    return new CharArrayDelimiterAppender(delimiter);
  }

  public static DelimiterAppender fromCharSequence(final CharSequence delimiter) {
    return new CharSequenceDelimiterAppender(delimiter);
  }

  public static DelimiterAppender noOp() {
    return NO_OP_APPENDER;
  }

  public static DelimiterAppender optimize(final CharSequence charSequence) {
    final DelimiterAppender appender;
    switch (charSequence.length()) {
      case 1:
        appender = new CharDelimiterAppender(charSequence.charAt(0));
        break;

      case 0:
        appender = NO_OP_APPENDER;
        break;

      default:
        appender = new CharArrayDelimiterAppender(charSequence.toString().toCharArray());
        break;
    }

    return appender;
  }

}

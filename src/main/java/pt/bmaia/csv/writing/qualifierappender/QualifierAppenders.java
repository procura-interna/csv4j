package pt.bmaia.csv.writing.qualifierappender;

public final class QualifierAppenders {

  private QualifierAppenders() {
  }

  public static QualifierAppender fromChar(final char qualifier) {
    return new CharQualifierAppender(qualifier);
  }

  public static QualifierAppender fromCharArray(final char[] qualifier) {
    return new CharArrayQualifierAppender(qualifier);
  }

  public static NoOpQualifierAppender noOp() {
    return new NoOpQualifierAppender();
  }

  public static QualifierAppender optimizedFromCharArray(final char[] qualifier) {
    switch (qualifier.length) {
      case 0:
        return noOp();

      case 1:
        return fromChar(qualifier[0]);

      default:
        return fromCharArray(qualifier);
    }
  }

}

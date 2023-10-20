package pt.bmaia.csv.format;

public class ImmutableFormatSequencesConfiguration implements FormatSequencesConfiguration {

  private final CharSequence valueQualifier;
  private final CharSequence valueSeparator;
  private final CharSequence newRecordIndicator;

  public ImmutableFormatSequencesConfiguration(
      final CharSequence valueQualifier, final CharSequence valueSeparator,
      final CharSequence newRecordIndicator) {
    this.valueQualifier = valueQualifier;
    this.valueSeparator = valueSeparator;
    this.newRecordIndicator = newRecordIndicator;
  }

  @Override
  public CharSequence valueDelimiter() {
    return valueQualifier;
  }

  @Override
  public CharSequence valueSeparator() {
    return valueSeparator;
  }

  @Override
  public CharSequence recordSeparator() {
    return newRecordIndicator;
  }

}

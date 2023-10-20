package pt.bmaia.csv.writing.recordappender;

import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.procurainterna.text.charsoutput.CharsOutput;

public class CharSequenceArrayRecordAppender<C extends CharSequence> implements RecordAppender<C[]> {

  protected final ValueAppender<? super CharSequence> valueAppender;
  private final DelimiterAppender valueSeparatorDelimiterAppender;

  public CharSequenceArrayRecordAppender(final ValueAppender<? super CharSequence> valueAppender,
      final DelimiterAppender valueSeparatorDelimiterAppender) {
    this.valueAppender = valueAppender;
    this.valueSeparatorDelimiterAppender = valueSeparatorDelimiterAppender;
  }

  @Override
  public void writeTo(final C[] record, final CharsOutput charsOutput) {
    if (record.length == 0) {
      return;
    }

    final int limit = record.length - 1;
    for (int i = 0; i < limit; i++) {
      final CharSequence charSequence = record[i];

      valueAppender.append(charsOutput, charSequence);
      valueSeparatorDelimiterAppender.writeTo(charsOutput);
    }

    valueAppender.append(charsOutput, record[limit]);
  }

}

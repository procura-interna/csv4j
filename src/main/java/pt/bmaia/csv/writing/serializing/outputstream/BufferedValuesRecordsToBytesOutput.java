package pt.bmaia.csv.writing.serializing.outputstream;

import java.nio.charset.Charset;
import pt.bmaia.csv.writing.recordsappender.RecordsAppender;
import pt.procurainterna.lang.io.bytesoutput.BytesOutput;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;
import pt.procurainterna.lang.text.charsoutput.CharsOutputs;
import pt.procurainterna.lang.text.charsoutput.FlushableCharsOutput;

public class BufferedValuesRecordsToBytesOutput<D> implements RecordsToBytesOutput<D> {

  private static final int DEFAULT_CHAR_BUFFER_SIZE = 8192;

  private final RecordsAppender<D> recordsAppender;
  private final Charset charset;
  private final int charBufferSize;

  public BufferedValuesRecordsToBytesOutput(
      final RecordsAppender<D> recordsAppender, final Charset charset, final int charBufferSize) {
    this.recordsAppender = recordsAppender;
    this.charset = charset;
    this.charBufferSize = charBufferSize;
  }

  public BufferedValuesRecordsToBytesOutput(
      final RecordsAppender<D> recordsAppender, final Charset charset) {
    this(recordsAppender, charset, DEFAULT_CHAR_BUFFER_SIZE);
  }

  @Override
  public void writeInto(final D records, final BytesOutput bytesOutput) {
      final FlushableCharsOutput bufferedOutput = bufferedCharsOutput(bytesOutput);

      recordsAppender.writeTo(records, bufferedOutput);

      bufferedOutput.flush();
  }

  private FlushableCharsOutput bufferedCharsOutput(final BytesOutput bytesOutput) {
    final CharsOutput
        encodedCharsOutput = CharsOutputs.fromEncodedBytesOutput(bytesOutput, charset);

    return CharsOutputs.buffered(encodedCharsOutput, charBufferSize);
  }


}

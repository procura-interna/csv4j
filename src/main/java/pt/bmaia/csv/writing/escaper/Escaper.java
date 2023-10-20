package pt.bmaia.csv.writing.escaper;

import pt.procurainterna.text.charsinput.CharsInput;
import pt.procurainterna.text.charsoutput.CharsOutput;

public interface Escaper {

  void escape(CharsInput charsInput, CharsOutput charsOutput);

}

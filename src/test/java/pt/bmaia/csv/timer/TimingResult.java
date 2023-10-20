package pt.bmaia.csv.timer;

public class TimingResult {

  public final String name;
  public final long milliseconds;


  public TimingResult(final String name, final long milliseconds) {
    this.name = name;
    this.milliseconds = milliseconds;
  }

}

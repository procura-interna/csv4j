package pt.bmaia.csv.timer;

public class TaskTimer {

  public static TimingResult timeTask(final String name, final Runnable runnable, final int rounds) {
    final long start = System.currentTimeMillis();

    for (int i = 0; i < rounds; i++) {
      runnable.run();
    }

    final long durationMilliseconds = System.currentTimeMillis() - start;


    return new TimingResult(name, durationMilliseconds);
  }
}
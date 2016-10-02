package com.htoyama.likit.common;

/**
 * Supporting Design by Contract.
 */
public final class Contract {

  /**
   * Check the given value must be not null.
   * If null, throw {@link NullPointerException} having no message.
   *
   * @throws NullPointerException the given value is null.
   * @see #requireNotNull(Object, String)
   */
  public static <T> T requireNotNull(T value) {
    if (value == null) {
      NullPointerException npe = new NullPointerException("");

      StackTraceElement[] source = npe.getStackTrace();
      StackTraceElement[] updated = new StackTraceElement[source.length - 1];
      System.arraycopy(source, 1, updated, 0, updated.length);

      npe.setStackTrace(updated);
      throw npe;
    }

    return value;
  }

  /**
   * Check the given value must be not null.
   * If null, throw {@link NullPointerException} which has message containing the given valueName.
   *
   * @throws NullPointerException the given value is null.
   */
  public static <T> T requireNotNull(T value, String valueName) {
    if (value == null) {
      NullPointerException npe = new NullPointerException(valueName + " must not be null.");

      StackTraceElement[] source = npe.getStackTrace();
      StackTraceElement[] updated = new StackTraceElement[source.length - 1];
      System.arraycopy(source, 1, updated, 0, updated.length);

      npe.setStackTrace(updated);
      throw npe;
    }

    return value;
  }

  /**
   * Check the given condition is true.
   *
   * @throws IllegalArgumentException the given condition is false.
   */
  public static void require(boolean condition, String message) {
    if (!condition) {
      IllegalArgumentException ire = new IllegalArgumentException(message);

      StackTraceElement[] source = ire.getStackTrace();
      StackTraceElement[] updated = new StackTraceElement[source.length - 1];
      System.arraycopy(source, 1, updated, 0, updated.length);

      ire.setStackTrace(updated);
      throw ire;
    }
  }

  private Contract() {
  }
}

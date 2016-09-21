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
      throw new NullPointerException("");
    }

    return value;
  }

  /**
   * Check the given value must be not null.
   * If null, throw {@link NullPointerException} having the given message.
   *
   * @throws NullPointerException the given value is null.
   */
  public static <T> T requireNotNull(T value, String message) {
    if (value == null) {
      throw new NullPointerException(message);
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
      throw new IllegalArgumentException(message);
    }
  }

  private Contract() {
  }
}

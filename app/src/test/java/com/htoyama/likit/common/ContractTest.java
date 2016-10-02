package com.htoyama.likit.common;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

public class ContractTest {

  @Test
  public void requireNotNull_message() {
    try {
      Contract.requireNotNull(null, "value");
      fail("");
    } catch (NullPointerException e) {
      assertThat(e.getMessage()).isEqualTo("value must not be null.");

      StackTraceElement[] trace = e.getStackTrace();
      assertThat(trace[0].toString()).startsWith(
          getClass().getName() + ".requireNotNull_message(ContractTest.java:");
    }
  }

  @Test public void requireNotNull_innerClass() {
    new Runnable() {
      @Override public void run() {
        try {
          Contract.requireNotNull(null, "value");
          fail("");
        } catch (NullPointerException e) {
          assertThat(e.getMessage()).isEqualTo("value must not be null.");

          StackTraceElement[] trace = e.getStackTrace();
          assertThat(trace[0].toString()).startsWith(
              getClass().getName() + ".run(ContractTest.java:");
        }
      }
    }.run();
  }

  @Test
  public void require() {
    try {
      Contract.require(1 < 0, "error message.");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("error message.");

      StackTraceElement[] trace = e.getStackTrace();
      assertThat(trace[0].toString()).startsWith(
          getClass().getName() + ".require(ContractTest.java:");
    }
  }

  @Test public void require_innerClass() {
    new Runnable() {
      @Override public void run() {
        try {
          Contract.require(1 < 0, "error message.");
          fail();
        } catch (IllegalArgumentException e) {
          assertThat(e.getMessage()).isEqualTo("error message.");

          StackTraceElement[] trace = e.getStackTrace();
          assertThat(trace[0].toString()).startsWith(
              getClass().getName() + ".run(ContractTest.java:");
        }
      }
    }.run();
  }

}
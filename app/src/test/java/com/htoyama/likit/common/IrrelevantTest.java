package com.htoyama.likit.common;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class IrrelevantTest {

  @Test public void equals() {
    Object instance1 = Irrelevant.get();
    Object instance2 = Irrelevant.get();

    assertThat(instance1).isEqualTo(instance2);
  }
}

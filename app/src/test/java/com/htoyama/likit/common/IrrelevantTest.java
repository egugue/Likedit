package com.htoyama.likit.common;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class IrrelevantTest {

  @Test public void equals() {
    Irrelevant instance1 = Irrelevant.get();
    Irrelevant instance2 = Irrelevant.get();

    assertThat(instance1).isEqualTo(instance2);
  }
}

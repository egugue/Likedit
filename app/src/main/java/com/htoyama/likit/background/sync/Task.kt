package com.htoyama.likit.background.sync

/**
 * Representing a task
 */
interface Task {

  /**
   * Execute the task
   *
   * @return an [Throwable] if executing the task is failure or null if executing it is success.
   */
  fun execute(): Throwable?
}
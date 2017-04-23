package com.htoyama.likit.background.sync

import com.htoyama.likit.common.Irrelevant
import com.htoyama.likit.data.prefs.AppSetting
import com.htoyama.likit.testutil.TestSchedulerRule
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TaskExecutorTest {
  @Rule @JvmField val rule = TestSchedulerRule()

  @Mock lateinit var likedPullTask: LikesPullTask
  @Mock lateinit var nonLikesRemoveTask: NonLikesRemoveTask
  @Mock lateinit var appSetting: AppSetting
  lateinit var executer: TaskExecutor

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
    executer = TaskExecutor(likedPullTask, nonLikesRemoveTask, appSetting)
  }

  @Test fun execute_whenAllTasksAreSuccess() {
    whenever(likedPullTask.execute()).thenReturn(Single.just(Irrelevant.get()))
    whenever(nonLikesRemoveTask.execute()).thenReturn(Single.just(Irrelevant))

    executer.execute().test()

    verify(appSetting).setLastSyncedDateAsNow()
  }

  @Test fun execute_whenATaskIsFailed() {
    val failed = Exception()
    whenever(likedPullTask.execute()).thenReturn(Single.error(failed))
    whenever(nonLikesRemoveTask.execute()).thenReturn(Single.just(Irrelevant))

    val test = executer.execute().test()

    verify(appSetting, never()).setLastSyncedDateAsNow()
    test.assertError(failed)
  }

  @Test fun execute_whenRemoveTaskIsFailed() {
    val failed = Exception()
    whenever(likedPullTask.execute()).thenReturn(Single.just(Irrelevant.get()))
    whenever(nonLikesRemoveTask.execute()).thenReturn(Single.error(failed))

    val test = executer.execute().test()

    verify(appSetting, never()).setLastSyncedDateAsNow()
    test.assertError(failed)
  }
}
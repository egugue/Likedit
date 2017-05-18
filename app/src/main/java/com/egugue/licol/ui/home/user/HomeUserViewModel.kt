package com.egugue.licol.ui.home.user

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.egugue.licol.application.user.UserAppService
import com.egugue.licol.common.extensions.observeOnMain
import com.egugue.licol.common.extensions.subscribeOnIo
import com.egugue.licol.domain.user.User
import javax.inject.Inject

/**
 * Created by htoyama on 2017/05/18.
 */
class HomeUserViewModel @Inject constructor(
    private val appService: UserAppService
) : ViewModel() {

  private val page = 1
  val userList: MutableLiveData<List<User>> = MutableLiveData()
  val isLoading: MutableLiveData<Boolean> = MutableLiveData()
  val hasLoadedItems: MutableLiveData<Boolean> = MutableLiveData()

  fun getUser(): LiveData<User> {
    appService.getAllUsers(1, 20)
        .subscribeOnIo()
        .observeOnMain()
        .doOnSubscribe {
          isLoading.postValue(true)
        }
        .doOnEach { isLoading.postValue(false) }
        .subscribe(
            { userList ->
              this.userList.postValue(userList)
            },
            { error ->
              error.printStackTrace()
            }
  }
}

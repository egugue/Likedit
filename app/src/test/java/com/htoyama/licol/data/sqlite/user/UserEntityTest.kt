package com.htoyama.licol.data.sqlite.user

import com.htoyama.licol.data.sqlite.userEntity
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.htoyama.licol.user

class UserEntityTest {

  @Test fun `convert into User`() {
    val entity = userEntity()

    val actual = entity.toUser()

    actual.run {
      assertThat(id).isEqualTo(entity.id)
      assertThat(name).isEqualTo(entity.name)
      assertThat(screenName).isEqualTo(entity.screenName)
      assertThat(avatorUrl).isEqualTo(entity.avatarUrl)
    }
  }

  @Test fun `create from User`() {
    val user = user()

    val actual = UserEntity.from(user)

    actual.run {
      assertThat(id).isEqualTo(user.id)
      assertThat(name).isEqualTo(user.name)
      assertThat(screenName).isEqualTo(user.screenName)
      assertThat(avatarUrl).isEqualTo(user.avatorUrl)
    }
  }
}
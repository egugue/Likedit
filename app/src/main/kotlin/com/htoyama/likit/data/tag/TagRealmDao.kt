package com.htoyama.likit.data.tag

import com.htoyama.likit.domain.tag.Tag
import io.realm.Realm
import java.util.*
import rx.Observable
import javax.inject.Inject

/**
 * A DAO handling Tag [Realm] database.
 */
class TagRealmDao @Inject constructor() {

  /**
   * Retrieve last inserted id.
   *
   * @return id. Return -1 If no [Tag]s exist
   */
  fun lastInsertedId(): Long {
    Realm.getDefaultInstance().use {
      val max: Number? = it.where(RealmTag::class.java).max("id")
      return if (max == null) {
        -1
      } else {
        max.toLong()
      }
    }
  }

  fun selectAll(): Observable<List<Tag>> {
    Realm.getDefaultInstance().use {
      return Observable.fromCallable {

        val results = it.where(RealmTag::class.java)
            .findAll()
        val all = ArrayList<Tag>(results.size)

        for (realmTag in results) {
          all.add(Tag(
              id = realmTag.id,
              name = realmTag.name,
              createdAt = realmTag.createdAt
          ))
        }

        Collections.unmodifiableList(all)
      }
    }
  }

  fun insertOrUpdate(tag: Tag) {
    Realm.getDefaultInstance().use {
      it.executeTransaction {
        val realmTag = RealmTag(
            id = tag.id,
            name = tag.name,
            createdAt = tag.createdAt
        )
        it.insertOrUpdate(realmTag)
      }
    }
  }

  fun delete(tag: Tag) {
    Realm.getDefaultInstance().use {
      val realmTag = it.where(RealmTag::class.java)
          .equalTo("id", tag.id)
          .findFirst()
      it.executeTransaction {
        realmTag.deleteFromRealm()
      }
    }
  }

}
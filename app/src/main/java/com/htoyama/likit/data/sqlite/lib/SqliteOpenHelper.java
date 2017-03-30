package com.htoyama.likit.data.sqlite.lib;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.htoyama.likit.data.sqlite.TagModel;
import com.htoyama.likit.data.sqlite.TweetModel;
import com.htoyama.likit.data.sqlite.UserModel;
import com.htoyama.likit.data.sqlite.entity.TweetEntity;
import com.htoyama.likit.data.sqlite.entity.UserEntity;

public class SqliteOpenHelper extends SQLiteOpenHelper {

  public SqliteOpenHelper(Application app) {
    super(app, "likedit", null, 1);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(TweetModel.CREATE_TABLE);
    db.execSQL(UserModel.CREATE_TABLE);
    db.execSQL(TagModel.CREATE_TABLE);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}

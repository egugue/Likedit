package com.htoyama.likit.data.sqlite.lib;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.htoyama.likit.data.sqlite.TagModel;
import com.htoyama.likit.data.sqlite.TweetModel;
import com.htoyama.likit.data.sqlite.UserModel;

public class SqliteOpenHelper extends SQLiteOpenHelper {

  public SqliteOpenHelper(Application app) {
    super(app, "likedit", null, 1);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(TweetModel.CREATE_TABLE);
    db.execSQL(UserModel.CREATE_TABLE);
    db.execSQL(TagModel.CREATE_TABLE);
  }

  @Override public void onConfigure(SQLiteDatabase db) {
    super.onConfigure(db);
    db.setForeignKeyConstraintsEnabled(true);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}

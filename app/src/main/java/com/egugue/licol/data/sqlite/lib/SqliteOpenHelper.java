package com.egugue.licol.data.sqlite.lib;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.egugue.licol.data.sqlite.LikedTweetModel;
import com.egugue.licol.data.sqlite.TagModel;
import com.egugue.licol.data.sqlite.TweetTagRelationModel;
import com.egugue.licol.data.sqlite.UserModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SqliteOpenHelper extends SQLiteOpenHelper {

  @Inject
  public SqliteOpenHelper(Application app) {
    super(app, "likedit", null, 1);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(LikedTweetModel.CREATE_TABLE);
    db.execSQL(UserModel.CREATE_TABLE);
    db.execSQL(TagModel.CREATE_TABLE);
    db.execSQL(TweetTagRelationModel.CREATE_TABLE);
  }

  @Override public void onConfigure(SQLiteDatabase db) {
    super.onConfigure(db);
    db.setForeignKeyConstraintsEnabled(true);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}

package com.scheng.gymlog.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.scheng.gymlog.database.entities.GymLog;
import java.util.List;

@Dao
public interface GymLogDAO {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(GymLog gymLog);

  @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " ORDER BY date DESC")
  List<GymLog> getAllRecords();

  @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
  List<GymLog> getRecordsByUserId(int loggedInUserId);

  @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
  LiveData<List<GymLog>> getRecordsByUserIdLiveData(int loggedInUserId);
}

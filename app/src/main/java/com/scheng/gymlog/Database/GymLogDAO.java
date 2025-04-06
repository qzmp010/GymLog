package com.scheng.gymlog.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.scheng.gymlog.Database.entities.GymLog;
import java.util.ArrayList;

@Dao
public interface GymLogDAO {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(GymLog gymLog);

  @Query("Select * from " + GymLogDatabase.GYM_LOG_TABLE)
  ArrayList<GymLog> getAllRecords();
}

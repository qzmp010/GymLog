package com.scheng.gymlog.Database;

import android.app.Application;
import com.scheng.gymlog.Database.entities.GymLog;
import com.scheng.gymlog.MainActivity;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import android.util.Log;

public class GymLogRepository {

  private GymLogDAO gymLogDAO;
  private ArrayList<GymLog> allLogs;

  public GymLogRepository(Application application) {
    GymLogDatabase db = GymLogDatabase.getDatabase(application);
    this.gymLogDAO = db.gymLogDAO();
    this.allLogs = this.gymLogDAO.getAllRecords();
  }

  public ArrayList<GymLog> getAllLogs() {
    Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(() ->
        gymLogDAO.getAllRecords()
    );
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      Log.i(MainActivity.TAG, "Problem when getting all GymLogs in the repository");
    }
    return null;
  }

  public void insertGymLog(GymLog gymLog) {
    GymLogDatabase.databaseWriteExecutor.execute(() ->
        gymLogDAO.insert(gymLog));
  }
}

package com.scheng.gymlog.database.entities;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.scheng.gymlog.database.GymLogDatabase;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Sunny Cheng
 * CST 338 HW04: GymLog
 * Apr 15, 2025
 * GymLog entity
 */

@Entity(tableName = GymLogDatabase.GYM_LOG_TABLE)
public class GymLog {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private String exercise;
  private double weight;
  private int reps;
  private LocalDateTime date;

  private int userId;

  public GymLog(String exercise, double weight, int reps, int userId) {
    this.exercise = exercise;
    this.weight = weight;
    this.reps = reps;
    this.userId = userId;
    date = LocalDateTime.now();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getExercise() {
    return exercise;
  }

  public void setExercise(String exercise) {
    this.exercise = exercise;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public int getReps() {
    return reps;
  }

  public void setReps(int reps) {
    this.reps = reps;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @SuppressLint("DefaultLocale")
  @NonNull
  @Override
  public String toString() {
    return String.format("Exercise: %s%nWeight: %s%nReps: %d%nDate: %s", exercise, weight, reps,
        String.format("%1$tD %1$tr", date));
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GymLog gymLog = (GymLog) o;
    return id == gymLog.id && Double.compare(weight, gymLog.weight) == 0
        && reps == gymLog.reps && userId == gymLog.userId && Objects.equals(exercise,
        gymLog.exercise) && Objects.equals(date, gymLog.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, exercise, weight, reps, date, userId);
  }
}

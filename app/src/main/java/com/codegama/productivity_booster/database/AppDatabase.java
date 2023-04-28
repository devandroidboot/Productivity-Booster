package com.codegama.productivity_booster.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.codegama.productivity_booster.dao.task;
import com.codegama.productivity_booster.dao.user;
import com.codegama.productivity_booster.model.Task;
import com.codegama.productivity_booster.model.User;

@Database(entities = {Task.class, User.class}, version = 1, exportSchema = false)
public  abstract class AppDatabase extends RoomDatabase {

    public abstract task dataBaseAction();

    public abstract user userdao();

    private static volatile AppDatabase appDatabase;

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}


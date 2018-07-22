package com.example.deepan.smser;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by deepan on 23/1/18.
 */


@Database(entities = {ReceivedSMS.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract ReceivedSMSDAO rSMSDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "user-database"
            ).allowMainThreadQueries()  // allow queries on the main thread.
            .build();
        }
        return INSTANCE;

    }

}

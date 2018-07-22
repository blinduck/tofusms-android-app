package com.example.deepan.smser;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepan on 23/1/18.
 */


@Dao
public interface ReceivedSMSDAO {
    @Query("SELECT * from ReceivedSMS")
    List<ReceivedSMS> getAll();

    @Query("SELECT * from ReceivedSMS ORDER BY uid DESC LIMIT 10")
    List<ReceivedSMS> last10();

    @Query("SELECT * from ReceivedSMS WHERE uid=:uid")
    ReceivedSMS getById(long uid);

    @Insert
    long add(ReceivedSMS sms);

    @Update
    public void update(ReceivedSMS sms);


}

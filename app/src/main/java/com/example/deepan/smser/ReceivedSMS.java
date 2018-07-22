package com.example.deepan.smser;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.provider.Settings;

import java.io.Serializable;

/**
 * Created by deepan on 23/1/18.
 */


@Entity(tableName = "receivedsms")
public class ReceivedSMS implements Serializable {
    public ReceivedSMS() {
        this.uid = uid;
    }

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name="contact")
    private String contact;

    @ColumnInfo(name="message")
    private String message;

    @ColumnInfo(name= "synced")
    private boolean synced;

    @Ignore
    private String androidId;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

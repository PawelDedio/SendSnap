package com.example.pdedio.sendsnap.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by pawel on 05.03.2017.
 */
@Database(name = SnapDB.DB_NAME, version = SnapDB.DB_VERSION)
public class SnapDB {

    public static final String DB_NAME = "SnapDB";

    public static final int DB_VERSION = 1;
}

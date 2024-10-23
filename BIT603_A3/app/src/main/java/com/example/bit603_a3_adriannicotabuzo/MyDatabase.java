package com.example.bit603_a3_adriannicotabuzo;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Users.class, Inventory.class}, version = 2)
public abstract class MyDatabase extends RoomDatabase {
    public abstract Dao dao();
}

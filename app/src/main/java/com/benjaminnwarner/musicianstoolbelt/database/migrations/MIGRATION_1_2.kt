package com.benjaminnwarner.musicianstoolbelt.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        // Add updated_at to recordings and remove whitespace from local_filename
        database.execSQL("BEGIN")

        database.execSQL("ALTER TABLE recordings RENAME TO old_recordings;")
        database.execSQL("CREATE TABLE recordings (id INTEGER PRIMARY KEY NOT NULL, created_at TEXT NOT NULL, " +
                "updated_at TEXT NOT NULL, name TEXT NOT NULL, local_filename TEXT NOT NULL);")

        database.execSQL("INSERT INTO recordings(id, created_at, updated_at, name, local_filename) " +
                "SELECT id, created_at, created_at, name, ` local_filename` " +
                "FROM old_recordings;")

        database.execSQL("DROP TABLE old_recordings;")
        database.execSQL("COMMIT")



        // Add songs table
        database.execSQL("CREATE TABLE songs (id INTEGER PRIMARY KEY NOT NULL, created_at TEXT NOT NULL, "
                + " updated_at TEXT NOT NULL, name TEXT NOT NULL);"
        )
    }
}

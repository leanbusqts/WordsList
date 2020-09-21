package dev.bulean.wordslist

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class that represents a word in the database
 */
/*
* Each @Entity class represents a SQLite table.
* Annotate your class declaration to indicate that it's an entity.
* You can specify the name of the table if you want it to be different from the name of the class.
*/
@Entity(tableName = "word_table")
data class Word(
    /*
    * Every entity needs a primary key.
    * To keep things simple, each word acts as its own primary key.
    */
    @PrimaryKey
    /*
    * Specifies the name of the column in the table
    * if you want it to be different from the name of the member variable.
    * This names the column "word".
    */
    @ColumnInfo(name = "word")
    val word: String
)

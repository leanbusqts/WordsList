package dev.bulean.wordslist

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object (DAO) for a word.
 * Each method performs a database operation, such as inserting or deleting a word,
 * running a DB query, or deleting all words.
 */
/*
* In the DAO (data access object), you specify SQL queries and associate them with method calls.
* The compiler checks the SQL and generates queries from convenience annotations for common queries.
* Room has coroutines support, allowing your queries to be annotated with the suspend modifier
* and then called from a coroutine or from another suspension function.
* The @Dao annotation identifies it as a DAO class for Room.
*/
@Dao
interface WordDao {
    /*
    * LiveData, a lifecycle library class for data observation.
    * Use a return value of type LiveData in your method description, and Room generates
    * all necessary code to update the LiveData when the database is updated.
    * */
    @Query("SELECT * from word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): LiveData<List<Word>>
    /*
    * The @Insert annotation is a special DAO method annotation where you don't have to
    * provide any SQL!
    * (There are also @Delete and @Update annotations for deleting and updating rows)
    * onConflict = OnConflictStrategy.IGNORE: The selected onConflict strategy
    * ignores a new word if it's exactly the same as one already in the list.
    * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)
    /*
    * @Query requires that you provide a SQL query as a string parameter to the annotation,
    * allowing for complex read queries and other operations.
    * */
    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}
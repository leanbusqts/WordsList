package dev.bulean.wordslist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
/*
* ROOM
* Room is a database layer on top of an SQLite database.
* Room takes care of mundane tasks that you used to handle with an SQLiteOpenHelper.
* Room uses the DAO to issue queries to its database.
* By default, to avoid poor UI performance, Room doesn't allow you to issue queries
* on the main thread. When Room queries return LiveData, the queries are automatically run
* asynchronously on a background thread.
* Room provides compile-time checks of SQLite statements.
* */
/*
* Annotates class to be a Room Database with a table (entity) of the Word class.
* Annotate the class to be a Room database with @Database and use the annotation parameters
* to declare the entities that belong in the database and set the version number.
* Each entity corresponds to a table that will be created in the database.
*/
@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {
    // Connects the database to the DAO.
    abstract fun wordDao(): WordDao
    // Companion object, allows us to add functions on the NoteDatabase class.
    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        /*
         * INSTANCE will keep a reference to any database returned via getDatabase.
         * This will help us avoid repeatedly initializing the database, which is expensive.
         * The value of a volatile variable will never be cached, and all writes and
         * reads will be done to and from the main memory. It means that changes made by one
         * thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null
        /*
         * Helper function to get the database.
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         * This function is threadsafe, and callers should cache the result for multiple database
         * calls to avoid overhead.
         * This is an example of a simple Singleton pattern that takes another Singleton as an
         * argument in Kotlin.
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        /*
        * getDatabase returns the singleton.
        * It'll create the database the first time it's accessed, using Room's database builder
        * to create a RoomDatabase object in the application context.
        * */
        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            /*
             * Multiple threads can ask for the database at the same time,
             * ensure we only initialize it once by using synchronized.
             * Only one thread may enter a synchronized block at a time.
             */
            return INSTANCE ?: synchronized(this) {
                // Create database
                val instance = Room.databaseBuilder(
                                        context.applicationContext,
                                        WordRoomDatabase::class.java,
                                        "word_database")
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    //.addCallback(WordDatabaseCallback(scope))
                    /*
                     * Wipes and rebuilds instead of migrating if no Migration object.
                     * You can learn more about migration with Room in this blog post:
                     * https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                     */
                    .build()
                INSTANCE = instance
                // Return instance; smart cast to be non-null.
                instance
            }
        }

        /*
        * To delete all content and repopulate the database whenever the app is started,
        * you create a RoomDatabase.Callback and override onOpen().
        * Because you cannot do Room database operations on the UI thread,
        * onOpen() launches a coroutine on the IO Dispatcher.
        * */
        /*private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wordDao())
                    }
                }
            }
        }*/

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        /*suspend fun populateDatabase(wordDao: WordDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            wordDao.deleteAll()

            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)
        }*/
    }

}
package dev.bulean.wordslist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Google Codelabs
 * https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#0
 * Use LiveData, ViewModel and Room
 * Entity: Annotated class that describes a database table when working with Room.
 * SQLite database: On device storage.
    The Room persistence library creates and maintains this database for you.
 * DAO: Data access object. A mapping of SQL queries to functions.
    When you use a DAO, you call the methods, and Room takes care of the rest.
 * Room database: Simplifies database work and serves as an access point
    to the underlying SQLite database (hides SQLiteOpenHelper).
    The Room database uses the DAO to issue queries to the SQLite database.
 * Repository: A class that you create that is primarily used to manage multiple data sources.
 * ViewModel: Acts as a communication center between the Repository (data) and the UI.
    The UI no longer needs to worry about the origin of the data.
    ViewModel instances survive Activity/Fragment recreation.
 * LiveData: A data holder class that can be observed.
    Always holds/caches the latest version of data, and notifies its observers when data has changed.
    LiveData is lifecycle aware.
    UI components just observe relevant data and don't stop or resume observation.
    LiveData automatically manages all of this
    since it's aware of the relevant lifecycle status changes while observing.
 * */
/*
* To display the current contents of the database,
* add an observer that observes the LiveData in the ViewModel.
* Whenever the data changes, the onChanged() callback is invoked.
* */
class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private lateinit var wordViewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get a new or existing ViewModel from the ViewModelProvider.
        /*
        * Use ViewModelProvider to associate your ViewModel with your Activity.
        * When your Activity first starts, the ViewModelProviders will create the ViewModel.
        * When the activity is destroyed, for example through a configuration change,
        * the ViewModel persists.
        * When the activity is re-created, the ViewModelProviders return the existing ViewModel.
        * */
        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        wordViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setWords(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let {
                val word = Word(it)
                wordViewModel.insert(word)
                Unit
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
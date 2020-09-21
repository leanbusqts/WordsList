package dev.bulean.wordslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */
/*
* The ViewModel's role is to provide data to the UI and survive configuration changes.
* A ViewModel acts as a communication center between the Repository and the UI.
* You can also use a ViewModel to share data between fragments.
* The ViewModel is part of the lifecycle library.
* */
/*
* A ViewModel holds your app's UI data in a lifecycle-conscious way
* that survives configuration changes. Separating your app's UI data
* from your Activity and Fragment classes lets you better follow the
* single responsibility principle: Your activities and fragments are responsible for
* drawing data to the screen, while your ViewModel can take care of holding and processing all
* the data needed for the UI.
* */
/*
* In the ViewModel, use LiveData for changeable data that the UI will use or display.
* Using LiveData has several benefits:
* - You can put an observer on the data (instead of polling for changes) and only update the
*   the UI when the data actually changes.
* - The Repository and the UI are completely separated by the ViewModel.
* - There are no database calls from the ViewModel (this is all handled in the Repository),
*   making the code more testable.
*/
class WordViewModel(application: Application) : AndroidViewModel(application) {

    // Private member variable to hold a reference to the repository.
    private val repository: WordRepository
    /* Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    * - We can put an observer on the data (instead of polling for changes) and only u pdate the
    *   the UI when the data actually changes.
    * - Repository is completely separated from the UI through the ViewModel.
    */
    // Public LiveData member variable to cache the list of words.
    val allWords: LiveData<List<Word>>
    // Gets a reference to the WordDao from the WordRoomDatabase.
    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    /*
    * In Kotlin, all coroutines run inside a CoroutineScope.
    * A scope controls the lifetime of coroutines through its job.
    * When you cancel the job of a scope, it cancels all coroutines started in that scope.
    * */
    /*
    * Created a wrapper insert() method that calls the Repository's insert() method.
    * In this way, the implementation of insert() is encapsulated from the UI.
    * We don't want insert to block the main thread, so we're launching a new coroutine
    * and calling the repository's insert, which is a suspend function.
    * */
    fun insert(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }
}


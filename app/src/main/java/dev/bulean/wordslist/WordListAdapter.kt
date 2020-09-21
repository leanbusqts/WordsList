package dev.bulean.wordslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
* RecyclerView is a subclass of ViewGroup and is a more resource-efficient way
* to display scrollable lists.
* Instead of creating a View for each item that may or may not be visible on the screen,
* RecyclerView creates a limited number of list items and reuses them for visible content.
* */
/*
* A layout manager. RecyclerView.LayoutManager handles the hierarchy and layout of View elements.
* RecyclerView requires an explicit layout manager to manage the arrangement of list items
* contained within it. This layout could be vertical, horizontal, or a grid.
* An adapter. RecyclerView.Adapter connects your data to the RecyclerView.
* It prepares the data in a RecyclerView.ViewHolder.
* A ViewHolder. Inside your adapter, you will create a ViewHolder that contains the View information
* for displaying one item from the item's layout.
* */
/*
* Android uses adapters (from the Adapter class) to connect data with View items in a list.
* There are many different kinds of adapters available, and you can also write custom adapters.
* To connect data with View items, the adapter needs to know about the View items.
* The adapter uses a ViewHolder that describes a View item and its position within the RecyclerView.
* */
class WordListAdapter internal constructor(context: Context) :
        RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var words = emptyList<Word>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = words[position]
        holder.wordItemView.text = current.word
    }

    override fun getItemCount(): Int = words.size

    internal fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }
}
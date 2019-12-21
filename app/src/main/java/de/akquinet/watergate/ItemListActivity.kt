package de.akquinet.watergate

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import de.akquinet.watergate.datamodel.ArsoData
import de.akquinet.watergate.datamodel.Station
import de.akquinet.watergate.dummy.ArsoContent
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.max


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            //            Toast.makeText(applicationContext, "Loading data...", Toast.LENGTH_SHORT).show()
//            Snackbar.make(view, "Loading data...", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()

//            LoadArsoDataTask(this).execute()
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(item_list)
    }

    override fun onStart() {
        super.onStart()
        LoadArsoDataTask(this).execute()
    }

    override fun onSearchRequested(): Boolean {
        Toast.makeText(applicationContext, "Search requested!", Toast.LENGTH_LONG).show()

        return super.onSearchRequested()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter =
            SimpleItemRecyclerViewAdapter(this, ArsoContent.data?.stations ?: listOf(), twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        internal val stations: List<Station>,
        private val twoPane: Boolean
    ) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>(), Filterable {

        private val stationsFiltered: MutableList<Station> = mutableListOf()

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Station
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putInt(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = stations[position]
            holder.idView.text = item.id.toString()
            holder.contentView.text = "${item.waterbody} – ${item.stationName}"

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
                var alertStage = when (item.waterLevelText) {
                    "visok vodostaj" -> 1
                    "prvi visokovodni vodostaj" -> 2
                    "drugi visokovodni vodostaj" -> 3
                    "tretji visokovodni vodostaj" -> 4
                    else -> 0
                }
                alertStage = when (item.flowRateText) {
                    "velik pretok" -> max(1, alertStage)
                    "prvi visokovodni pretok" -> max(2, alertStage)
                    "drugi visokovodni pretok" -> max(3, alertStage)
                    "tretji visokovodni pretok" -> max(4, alertStage)
                    else -> max(0, alertStage)
                }
                val backgroundColor = when (alertStage) {
                    1 -> 0xFFFFFECB
                    2 -> 0xFFFFFD8E
                    3 -> 0xFFFFAE6E
                    4 -> 0xFFFF7171
                    else -> 0x00FFFFFF
                }
                setBackgroundColor(backgroundColor.toInt())
                invalidate()
            }
        }

        override fun getItemCount() = stations.size

        override fun getFilter(): Filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filtered = stations.filter {
                    it.stationName.contains(constraint, true)
                            || it.waterbody.contains(constraint, true)
                }
                stationsFiltered.retainAll(filtered)
                val filterResults = FilterResults()
                filterResults.values = stationsFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }

    private class LoadArsoDataTask(val activity: ItemListActivity) :
        AsyncTask<String, Void, ArsoData>() {
        override fun doInBackground(vararg params: String): ArsoData {
            return ArsoContent.load()
        }

        override fun onPostExecute(result: ArsoData) {
            super.onPostExecute(result)
            Log.i(
                ItemListActivity::class.qualifiedName,
                "Loaded data with timestamp: " + result.timestampOfProvision
            )
            activity.setupRecyclerView(activity.item_list)
            val timestamp = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault())
                .withLocale(Locale.forLanguageTag("sl_SI"))
                .format(result.timestampOfProvision.toInstant())
            Toast.makeText(
                this.activity.applicationContext,
                "New data loaded, time of provision: $timestamp",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}


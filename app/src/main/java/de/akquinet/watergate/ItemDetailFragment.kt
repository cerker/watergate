package de.akquinet.watergate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.akquinet.watergate.datamodel.Station
import de.akquinet.watergate.dummy.ArsoContent
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: Station? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { bundle ->
            if (bundle.containsKey(ARG_ITEM_ID)) {
                val stationId = bundle.getInt(ARG_ITEM_ID)
                item = ArsoContent.data?.stations?.first { it.id == stationId }
                activity?.toolbar_layout?.title = item?.shortName ?: "Unknown"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        item?.let { station ->
            rootView.detail_waterbody.text = station.waterbody
            rootView.detail_station_name.text = station.stationName
            rootView.detail_flow_rate.text = station.flowRate?.toString() ?: "-"
            rootView.detail_flow_rate_text.text = station.flowRateText ?: "-"
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}

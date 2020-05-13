package ch.epfl.sdp.ui.event.createevent;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.R;

/**
 * Adapter to handle autocomplete requests from geo search
 */
public class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List<GeoSearchResult> mResultList = new ArrayList();

    /**
     * Constructor of the GeoAutoCompleteAdapter
     *
     * @param context
     */
    public GeoAutoCompleteAdapter(Context context) { mContext = context; }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public GeoSearchResult getItem(int index) {
        return mResultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.geo_search_result, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.geo_search_result_text)).setText(getItem(position).getAddress());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List locations = findLocations(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = locations;
                    filterResults.count = locations.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null & results.count > 0) {
                    mResultList = (List<GeoSearchResult>)results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private List<GeoSearchResult> findLocations(Context context, String queryText) {
        List<GeoSearchResult> geoSearchResults = new ArrayList<>();
        Geocoder geocoder = new Geocoder(context, context.getResources().getConfiguration().locale);

        if (!Geocoder.isPresent()) {
            geoSearchResults.add(getDefaultSearchResult(context));
            return geoSearchResults;
        }

        try {
            List<Address> addresses = geocoder.getFromLocationName(queryText, MAX_RESULTS);
            for(int i = 0; i < addresses.size(); i++) {
                Address address = addresses.get(i);
                if(address.getMaxAddressLineIndex() != -1) {
                    geoSearchResults.add(new GeoSearchResult(address));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            geoSearchResults.add(getDefaultSearchResult(context));
            return geoSearchResults;
        }

        return geoSearchResults;
    }

    private GeoSearchResult getDefaultSearchResult(Context context) {
        Address localAddress = new Address(context.getResources().getConfiguration().locale);
        localAddress.setLongitude(10);
        localAddress.setLatitude(10);
        localAddress.setAddressLine(0, "Lausanne, Switzerland");
        return new GeoSearchResult(localAddress);
    }
}

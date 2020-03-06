package ch.epfl.sdp.ui.swipe;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;
import java.util.Date;

import ch.epfl.sdp.CardArrayAdapter;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;

public class SwipeFragment extends Fragment {
    private ArrayList<Event> eventList;
    private ArrayAdapter<Event> arrayAdapter;
    private int i;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.swipe_fragment, container, false);

        eventList = new ArrayList<Event>();

        eventList.add(new Event("OSS-117 Movie watching",
                "We will watch OSS-117: Cairo, Nest of Spies and then we can exchange about why this is the best movie of all times",
                new Date(2021, 1, 16), R.drawable.oss_117));
        eventList.add(new Event("Duck themed party",
                "Bring out your best duck disguises and join us for our amazing party on the lakeside. Swans disguises not allowed",
                new Date(2020, 3, 7), R.drawable.duck));
        eventList.add(new Event("Make Internet great again",
                "At this meeting we will debate on how to make pepe the frog memes great again",
                new Date(2020, 4, 20), R.drawable.frog));
        eventList.add(new Event("Real Fake Party",
                "This is really happening",
                new Date(2020, 11, 10)));

        arrayAdapter = new CardArrayAdapter(getActivity(), R.layout.card, eventList);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SwipeFlingAdapterView flingContainer = getView().findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                eventList.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
            }

            @Override
            public void onRightCardExit(Object dataObject) { }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) { }

            @Override
            public void onScroll(float scrollProgressPercent) {
                SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) getView().findViewById(R.id.frame);
                flingContainer.getSelectedView().findViewById(R.id.deny_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                flingContainer.getSelectedView().findViewById(R.id.accept_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });
        flingContainer.setOnItemClickListener((itemPosition, dataObject) -> {
        });
    }
}

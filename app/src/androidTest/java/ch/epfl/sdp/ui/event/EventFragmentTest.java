package ch.epfl.sdp.ui.event;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;

import com.google.firebase.auth.AuthCredential;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.intent.Intents;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.mocks.MockWeatherFetcher;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.weather.Weather;
import ch.epfl.sdp.weather.WeatherFetcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventFragmentTest {

    private final static String DUMMY_TITLE = "title";
    private final static String DUMMY_DESCRIPTION = "description";
    private final static String DUMMY_REF = "dfghkjh234l5";
    private final static EventBuilder sEventBuilder = new EventBuilder();
    private Event DUMMY_EVENT = sEventBuilder
            .setTitle(DUMMY_TITLE)
            .setDescription(DUMMY_DESCRIPTION)
            .setDate("01/01/2021")
            .setOrganizerRef(DUMMY_REF)
            .build();

    @Mock
    private Database mDatabaseMock;

    @Mock
    private CollectionQuery mCollectionQueryMock;

    @Mock
    private DocumentQuery mDocumentQueryMock;

    @Mock
    private FilterQuery mFilterQueryMock;

    @Mock
    private Authenticator<AuthCredential> mAuthenticatorMock;


    private WeatherFetcher mWeatherFetcherMock = new MockWeatherFetcher();

    private MutableLiveData<Event> mEventsLive = new MutableLiveData<>();

    private MutableLiveData<List<DatabaseObject<Weather>>> mWeatherLiveData = new MutableLiveData<>();

    private LiveData<List<DatabaseObject<ChatMessage>>> mChatLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mDatabaseMock.query(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.collection(anyString())).thenReturn(mCollectionQueryMock);
        when(mDocumentQueryMock.liveData(Event.class)).thenReturn(mEventsLive);
        mEventsLive.postValue(DUMMY_EVENT);

        when(mCollectionQueryMock.orderBy(anyString())).thenReturn(mFilterQueryMock);
        when(mFilterQueryMock.liveData(Weather.class)).thenReturn(mWeatherLiveData);
        mWeatherLiveData.postValue(null);
    }

    @SuppressWarnings("unchecked")
    private void scenario(Bundle bundle) {
        FragmentScenario<EventFragment> scenario = FragmentScenario.launchInContainer(
                EventFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(EventFragment.class, mDatabaseMock, "anyRef", mWeatherFetcherMock)
        );

    }
    @SuppressWarnings("unchecked")
    @Test
    public void EventFragment_CalendarIntent() throws InterruptedException {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");

        scenario(bundle);
        onView(withId(R.id.default_event_layout)).check(matches(isDisplayed()));

        Intents.init();

        Intent resultIntent = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, resultIntent);
        intending(allOf(
                hasType("vnd.android.cursor.item/event"),
                hasExtra(CalendarContract.Events.TITLE, DUMMY_TITLE),
                hasExtra(CalendarContract.Events.EVENT_LOCATION, DUMMY_EVENT.getAddress()),
                hasExtra(CalendarContract.Events.DESCRIPTION, DUMMY_EVENT.getDescription()),
                hasExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, DUMMY_EVENT.getDate().getTime())
        )).respondWith(result);
        onView(withId(R.id.event_detail_calendar_button)).perform(click());
        onView(withId(R.id.default_event_layout)).check(matches(isDisplayed()));
        
        Intents.release();
        Thread.sleep(1000);
    }

    @Test
    public void EventFragment_EventIsLoaded() {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        scenario(bundle);

        onView(withId(R.id.title)).check(matches(withText(containsString(DUMMY_TITLE))));
        onView(withId(R.id.description)).check(matches(withText(containsString(DUMMY_DESCRIPTION))));

        onView(withId(R.id.date)).check(matches(isDisplayed()));
        onView(withId(R.id.address)).check(matches(isDisplayed()));
        onView(withId(R.id.minimap)).check(matches(isDisplayed()));
    }

    @Test
    public void EventFragment_LaunchesChatWithCorrectValues() {
        when(mDatabaseMock.query(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.collection(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.orderBy(anyString())).thenReturn(mFilterQueryMock);
        when(mFilterQueryMock.liveData(ChatMessage.class)).thenReturn(mChatLiveData);
        when(mAuthenticatorMock.getCurrentUser()).thenReturn(new UserInfo("testuid", "testname", "testemail"));
        ServiceProvider.getInstance().setDatabase(mDatabaseMock);
        ServiceProvider.getInstance().setAuthenticator(mAuthenticatorMock);

        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        scenario(bundle);

        onView(withId(R.id.event_detail_chat_button)).perform(click());

        onView(withId(R.id.button_chatbox_send)).check(matches(isDisplayed()));
    }

    @Test
    public void EventFragment_HandlesWeatherCorrectly() {
        String oldWeatherData = "{\"lat\":50.04,\"lon\":10.48,\"timezone\":\"Europe/Berlin\",\"current\":{\"dt\":1589308917,\"sunrise\":1589254552,\"sunset\":1589309586,\"temp\":8.67,\"feels_like\":4.58,\"pressure\":1011,\"humidity\":6,\"dew_point\":-24.37,\"uvi\":6.28,\"clouds\":98,\"wind_speed\":0.45,\"wind_deg\":45,\"wind_gust\":0.89,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}]},\"daily\":[{\"dt\":1589281200,\"sunrise\":1589254552,\"sunset\":1589309586,\"temp\":{\"day\":8.67,\"min\":4.04,\"max\":8.67,\"night\":4.04,\"eve\":8.67,\"morn\":8.67},\"feels_like\":{\"day\":4.03,\"night\":0.95,\"eve\":4.03,\"morn\":4.03},\"pressure\":1011,\"humidity\":6,\"dew_point\":-24.37,\"wind_speed\":1.23,\"wind_deg\":238,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":98,\"uvi\":6.28},{\"dt\":1589367600,\"sunrise\":1589340865,\"sunset\":1589396074,\"temp\":{\"day\":15.55,\"min\":6.62,\"max\":16.22,\"night\":7.26,\"eve\":11.54,\"morn\":6.62},\"feels_like\":{\"day\":12.6,\"night\":4.62,\"eve\":9.62,\"morn\":4},\"pressure\":1011,\"humidity\":40,\"dew_point\":2.18,\"wind_speed\":1.83,\"wind_deg\":59,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":66,\"uvi\":6},{\"dt\":1589454000,\"sunrise\":1589427180,\"sunset\":1589482560,\"temp\":{\"day\":10.43,\"min\":6.72,\"max\":14.21,\"night\":7.97,\"eve\":11.5,\"morn\":6.72},\"feels_like\":{\"day\":7.41,\"night\":2.79,\"eve\":7.73,\"morn\":3.07},\"pressure\":1014,\"humidity\":79,\"dew_point\":7.04,\"wind_speed\":3.3,\"wind_deg\":75,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":99,\"rain\":2.2,\"uvi\":6.24},{\"dt\":1589540400,\"sunrise\":1589513497,\"sunset\":1589569046,\"temp\":{\"day\":14.72,\"min\":3.54,\"max\":14.72,\"night\":3.54,\"eve\":10.89,\"morn\":8.19},\"feels_like\":{\"day\":9.59,\"night\":0.85,\"eve\":8.36,\"morn\":2.39},\"pressure\":1015,\"humidity\":52,\"dew_point\":5.18,\"wind_speed\":5.71,\"wind_deg\":18,\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"clouds\":35,\"uvi\":6.58},{\"dt\":1589626800,\"sunrise\":1589599816,\"sunset\":1589655531,\"temp\":{\"day\":15.24,\"min\":5.32,\"max\":15.8,\"night\":5.32,\"eve\":12.41,\"morn\":7.17},\"feels_like\":{\"day\":12.34,\"night\":2.77,\"eve\":10.15,\"morn\":4.59},\"pressure\":1023,\"humidity\":45,\"dew_point\":3.47,\"wind_speed\":2.09,\"wind_deg\":328,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":0,\"uvi\":6.3},{\"dt\":1589713200,\"sunrise\":1589686137,\"sunset\":1589742014,\"temp\":{\"day\":18.1,\"min\":7.31,\"max\":18.37,\"night\":7.31,\"eve\":14.2,\"morn\":9},\"feels_like\":{\"day\":15.83,\"night\":4.66,\"eve\":11.88,\"morn\":6.86},\"pressure\":1025,\"humidity\":43,\"dew_point\":5.59,\"wind_speed\":1.73,\"wind_deg\":344,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":6,\"uvi\":6.6},{\"dt\":1589799600,\"sunrise\":1589772460,\"sunset\":1589828497,\"temp\":{\"day\":21.1,\"min\":9.49,\"max\":21.1,\"night\":9.49,\"eve\":17.04,\"morn\":11.56},\"feels_like\":{\"day\":18.8,\"night\":7.96,\"eve\":15.58,\"morn\":9.49},\"pressure\":1021,\"humidity\":44,\"dew_point\":8.7,\"wind_speed\":2.75,\"wind_deg\":344,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":0,\"uvi\":6.97},{\"dt\":1589886000,\"sunrise\":1589858785,\"sunset\":1589914979,\"temp\":{\"day\":18.98,\"min\":6.56,\"max\":18.98,\"night\":6.56,\"eve\":14.63,\"morn\":13.07},\"feels_like\":{\"day\":15.65,\"night\":4.01,\"eve\":12.03,\"morn\":11.74},\"pressure\":1023,\"humidity\":52,\"dew_point\":9.1,\"wind_speed\":4.41,\"wind_deg\":340,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"clouds\":18,\"uvi\":6.84}]}";
        String newWeatherData = "{\"lat\":46.5,\"lon\":6.5,\"timezone\":\"Europe/Zurich\",\"current\":{\"dt\":1589329262,\"sunrise\":1589342544,\"sunset\":1589396305,\"temp\":7.93,\"feels_like\":4.73,\"pressure\":1012,\"humidity\":89,\"dew_point\":6.23,\"uvi\":7.2,\"clouds\":94,\"wind_speed\":3.33,\"wind_deg\":356,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}]},\"daily\":[{\"dt\":1589367600,\"sunrise\":1589342544,\"sunset\":1589396305,\"temp\":{\"day\":16.77,\"min\":9.55,\"max\":17.62,\"night\":13.31,\"eve\":14.97,\"morn\":9.55},\"feels_like\":{\"day\":12.36,\"night\":12.89,\"eve\":12.96,\"morn\":7.14},\"pressure\":1005,\"humidity\":72,\"dew_point\":11.7,\"wind_speed\":7.06,\"wind_deg\":26,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":85,\"rain\":10.88,\"uvi\":7.2},{\"dt\":1589454000,\"sunrise\":1589428871,\"sunset\":1589482780,\"temp\":{\"day\":16.86,\"min\":11.66,\"max\":16.86,\"night\":11.66,\"eve\":13.64,\"morn\":13.12},\"feels_like\":{\"day\":14.1,\"night\":4.8,\"eve\":7.21,\"morn\":13.09},\"pressure\":1009,\"humidity\":77,\"dew_point\":12.88,\"wind_speed\":5.18,\"wind_deg\":43,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":66,\"rain\":5.76,\"uvi\":7.16},{\"dt\":1589540400,\"sunrise\":1589515199,\"sunset\":1589569254,\"temp\":{\"day\":11.7,\"min\":9.23,\"max\":13.1,\"night\":9.23,\"eve\":11.96,\"morn\":10.89},\"feels_like\":{\"day\":9.81,\"night\":3.97,\"eve\":5.98,\"morn\":8},\"pressure\":1013,\"humidity\":83,\"dew_point\":8.95,\"wind_speed\":2.35,\"wind_deg\":18,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":100,\"rain\":1.81,\"uvi\":6.84},{\"dt\":1589626800,\"sunrise\":1589601530,\"sunset\":1589655727,\"temp\":{\"day\":14.76,\"min\":9.46,\"max\":17.11,\"night\":12.46,\"eve\":16.28,\"morn\":9.46},\"feels_like\":{\"day\":10.86,\"night\":8.45,\"eve\":13.44,\"morn\":4.57},\"pressure\":1019,\"humidity\":69,\"dew_point\":9.28,\"wind_speed\":5.31,\"wind_deg\":37,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":81,\"uvi\":6.88},{\"dt\":1589713200,\"sunrise\":1589687862,\"sunset\":1589742200,\"temp\":{\"day\":17.85,\"min\":11.38,\"max\":19.58,\"night\":13.38,\"eve\":18.03,\"morn\":11.38},\"feels_like\":{\"day\":14.97,\"night\":10.78,\"eve\":14.69,\"morn\":7.67},\"pressure\":1019,\"humidity\":69,\"dew_point\":12.21,\"wind_speed\":5.04,\"wind_deg\":39,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":75,\"uvi\":7.49},{\"dt\":1589799600,\"sunrise\":1589774197,\"sunset\":1589828672,\"temp\":{\"day\":18.83,\"min\":13.31,\"max\":20.78,\"night\":13.68,\"eve\":18.98,\"morn\":13.31},\"feels_like\":{\"day\":17.28,\"night\":10.54,\"eve\":16.7,\"morn\":10.71},\"pressure\":1018,\"humidity\":63,\"dew_point\":11.71,\"wind_speed\":2.94,\"wind_deg\":55,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":92,\"uvi\":8},{\"dt\":1589886000,\"sunrise\":1589860533,\"sunset\":1589915142,\"temp\":{\"day\":20.12,\"min\":14.07,\"max\":20.78,\"night\":14.07,\"eve\":18.88,\"morn\":14.18},\"feels_like\":{\"day\":15.44,\"night\":11.41,\"eve\":16.26,\"morn\":10.41},\"pressure\":1018,\"humidity\":55,\"dew_point\":10.94,\"wind_speed\":7.06,\"wind_deg\":31,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":1,\"uvi\":7.76},{\"dt\":1589972400,\"sunrise\":1589946871,\"sunset\":1590001612,\"temp\":{\"day\":18.28,\"min\":13.38,\"max\":18.28,\"night\":18.28,\"eve\":18.28,\"morn\":13.38},\"feels_like\":{\"day\":17.74,\"night\":17.74,\"eve\":17.74,\"morn\":10.97},\"pressure\":1015,\"humidity\":68,\"dew_point\":12.41,\"wind_speed\":1.78,\"wind_deg\":128,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":3,\"uvi\":7.78}]}";

        Weather oldWeather = new Weather(oldWeatherData);
        Weather newWeather = new Weather(newWeatherData);

        Event DUMMY_EVENT_2 = sEventBuilder
                .setTitle(DUMMY_TITLE)
                .setDescription(DUMMY_DESCRIPTION)
                .setDate("16/05/2020")
                .setOrganizerRef(DUMMY_REF)
                .build();

        int closestDay = oldWeather.getClosestDay(DUMMY_EVENT_2.getDate());

        mEventsLive.postValue(DUMMY_EVENT_2);


        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        scenario(bundle);

        List<DatabaseObject<Weather>>  weatherList = new ArrayList<>();
        weatherList.add(new DatabaseObject<>("a", oldWeather));
        mWeatherLiveData.postValue(weatherList);

        String oldWeatherType = (String) oldWeather.getWeather(closestDay).get("main");
        onView(withId(R.id.weatherType)).check(matches(withText(oldWeatherType)));

        weatherList.add(new DatabaseObject<>("b", newWeather));
        mWeatherLiveData.postValue(weatherList);

        String newWeatherType = (String) newWeather.getWeather(closestDay).get("main");
        onView(withId(R.id.weatherType)).check(matches(withText(newWeatherType)));
    }

}

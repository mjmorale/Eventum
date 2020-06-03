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

import androidx.test.espresso.matcher.ViewMatchers;
import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
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
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
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
            .setDate("01/01/2021 00:00")
            .setOrganizerRef(DUMMY_REF)
            .build();

    @Mock
    private Database mDatabaseMock;

    @Mock
    private CollectionQuery mEventCollectionQueryMock;

    @Mock
    private CollectionQuery mUserCollectionQueryMock;

    @Mock
    private CollectionQuery mWeatherCollectionQueryMock;

    @Mock
    private CollectionQuery mChatCollectionQueryMock;

    @Mock
    private DocumentQuery mEventDocumentQueryMock;

    @Mock
    private DocumentQuery mUserDocumentQueryMock;

    @Mock
    private DocumentQuery mOrganizerDocumentQueryMock;

    @Mock
    private FilterQuery mWeatherFilterQueryMock;

    @Mock
    private FilterQuery mChatFilterQueryMock;

    @Mock
    private Authenticator<AuthCredential> mAuthenticatorMock;

    private WeatherFetcher mWeatherFetcherMock = new MockWeatherFetcher();

    private MutableLiveData<Event> mEventsLive = new MutableLiveData<>();

    private MutableLiveData<DatabaseObject<User>> mOrganizerLive = new MutableLiveData<>();

    private MutableLiveData<User> mUserLive = new MutableLiveData<>();

    private MutableLiveData<List<DatabaseObject<Weather>>> mWeatherLiveData = new MutableLiveData<>();

    private LiveData<List<DatabaseObject<ChatMessage>>> mChatLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mAuthenticatorMock.getCurrentUser()).thenReturn(new UserInfo("testuid", "testname", "testemail"));
        when(mDatabaseMock.query(eq("events"))).thenReturn(mEventCollectionQueryMock);
        when(mEventCollectionQueryMock.document(anyString())).thenReturn(mEventDocumentQueryMock);
        when(mEventDocumentQueryMock.liveData(Event.class)).thenReturn(mEventsLive);
        when(mDatabaseMock.query(eq("users"))).thenReturn(mUserCollectionQueryMock);
        when(mEventDocumentQueryMock.collection(anyString())).thenReturn(mWeatherCollectionQueryMock);
        when(mWeatherCollectionQueryMock.orderBy(anyString())).thenReturn(mWeatherFilterQueryMock);
        when(mWeatherFilterQueryMock.liveData(Weather.class)).thenReturn(mWeatherLiveData);
        when(mEventDocumentQueryMock.collection(eq("messages"))).thenReturn(mChatCollectionQueryMock);
        when(mChatCollectionQueryMock.orderBy(anyString())).thenReturn(mChatFilterQueryMock);
        when(mChatFilterQueryMock.liveData(ChatMessage.class)).thenReturn(mChatLiveData);
        mWeatherLiveData.postValue(null);
        mEventsLive.postValue(DUMMY_EVENT);
    }

    private void setupAttendee(List<DatabaseObject<User>> attendees) {
        doAnswer(invocation -> {
            List<String> userIds = new ArrayList<>();
            for(DatabaseObject<User> user: attendees) {
                userIds.add(user.getId());
            }
            ((Query.OnQueryCompleteCallback<Object>) invocation.getArgument(1)).onQueryComplete(QueryResult.success(userIds));
            return null;
        }).when(mEventDocumentQueryMock).getField(eq("attendees"), any());
        doAnswer(invocation -> {
            ((Query.OnQueryCompleteCallback<List<DatabaseObject<User>>>) invocation.getArgument(1)).onQueryComplete(QueryResult.success(attendees));
            return null;
        }).when(mUserCollectionQueryMock).get(eq(User.class), any());
    }

    private void setupOrganizer(DatabaseObject<User> user) {
        doAnswer(invocation -> {
            ((Query.OnQueryCompleteCallback<Object>) invocation.getArgument(1)).onQueryComplete(QueryResult.success(user.getId()));
            return null;
        }).when(mEventDocumentQueryMock).getField(eq("organizer"), any());
        when(mUserCollectionQueryMock.document(eq(user.getId()))).thenReturn(mOrganizerDocumentQueryMock);
        when(mOrganizerDocumentQueryMock.liveData(User.class)).thenReturn(mUserLive);
        mUserLive.postValue(user.getObject());
        mOrganizerLive.postValue(user);
    }

    @SuppressWarnings("unchecked")
    private void scenario(Bundle bundle) {
        FragmentScenario<EventFragment> scenario = FragmentScenario.launchInContainer(
                EventFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(EventFragment.class, mDatabaseMock, "anyRef", mAuthenticatorMock, mWeatherFetcherMock)
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
        String oldWeatherData = "{\"lat\":46.5,\"lon\":6.5,\"timezone\":\"Europe/Zurich\",\"current\":{\"dt\":1589329262,\"sunrise\":1589342544,\"sunset\":1589396305,\"temp\":7.93,\"feels_like\":4.73,\"pressure\":1012,\"humidity\":89,\"dew_point\":6.23,\"uvi\":7.2,\"clouds\":94,\"wind_speed\":3.33,\"wind_deg\":356,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}]},\"daily\":[{\"dt\":1589367600,\"sunrise\":1589342544,\"sunset\":1589396305,\"temp\":{\"day\":16.77,\"min\":9.55,\"max\":17.62,\"night\":13.31,\"eve\":14.97,\"morn\":9.55},\"feels_like\":{\"day\":12.36,\"night\":12.89,\"eve\":12.96,\"morn\":7.14},\"pressure\":1005,\"humidity\":72,\"dew_point\":11.7,\"wind_speed\":7.06,\"wind_deg\":26,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":85,\"rain\":10.88,\"uvi\":7.2},{\"dt\":1589454000,\"sunrise\":1589428871,\"sunset\":1589482780,\"temp\":{\"day\":16.86,\"min\":11.66,\"max\":16.86,\"night\":11.66,\"eve\":13.64,\"morn\":13.12},\"feels_like\":{\"day\":14.1,\"night\":4.8,\"eve\":7.21,\"morn\":13.09},\"pressure\":1009,\"humidity\":77,\"dew_point\":12.88,\"wind_speed\":5.18,\"wind_deg\":43,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":66,\"rain\":5.76,\"uvi\":7.16},{\"dt\":1589540400,\"sunrise\":1589515199,\"sunset\":1589569254,\"temp\":{\"day\":11.7,\"min\":9.23,\"max\":13.1,\"night\":9.23,\"eve\":11.96,\"morn\":10.89},\"feels_like\":{\"day\":9.81,\"night\":3.97,\"eve\":5.98,\"morn\":8},\"pressure\":1013,\"humidity\":83,\"dew_point\":8.95,\"wind_speed\":2.35,\"wind_deg\":18,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":100,\"rain\":1.81,\"uvi\":6.84},{\"dt\":1589626800,\"sunrise\":1589601530,\"sunset\":1589655727,\"temp\":{\"day\":14.76,\"min\":9.46,\"max\":17.11,\"night\":12.46,\"eve\":16.28,\"morn\":9.46},\"feels_like\":{\"day\":10.86,\"night\":8.45,\"eve\":13.44,\"morn\":4.57},\"pressure\":1019,\"humidity\":69,\"dew_point\":9.28,\"wind_speed\":5.31,\"wind_deg\":37,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":81,\"uvi\":6.88},{\"dt\":1589713200,\"sunrise\":1589687862,\"sunset\":1589742200,\"temp\":{\"day\":17.85,\"min\":11.38,\"max\":19.58,\"night\":13.38,\"eve\":18.03,\"morn\":11.38},\"feels_like\":{\"day\":14.97,\"night\":10.78,\"eve\":14.69,\"morn\":7.67},\"pressure\":1019,\"humidity\":69,\"dew_point\":12.21,\"wind_speed\":5.04,\"wind_deg\":39,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":75,\"uvi\":7.49},{\"dt\":1589799600,\"sunrise\":1589774197,\"sunset\":1589828672,\"temp\":{\"day\":18.83,\"min\":13.31,\"max\":20.78,\"night\":13.68,\"eve\":18.98,\"morn\":13.31},\"feels_like\":{\"day\":17.28,\"night\":10.54,\"eve\":16.7,\"morn\":10.71},\"pressure\":1018,\"humidity\":63,\"dew_point\":11.71,\"wind_speed\":2.94,\"wind_deg\":55,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":92,\"uvi\":8},{\"dt\":1589886000,\"sunrise\":1589860533,\"sunset\":1589915142,\"temp\":{\"day\":20.12,\"min\":14.07,\"max\":20.78,\"night\":14.07,\"eve\":18.88,\"morn\":14.18},\"feels_like\":{\"day\":15.44,\"night\":11.41,\"eve\":16.26,\"morn\":10.41},\"pressure\":1018,\"humidity\":55,\"dew_point\":10.94,\"wind_speed\":7.06,\"wind_deg\":31,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":1,\"uvi\":7.76},{\"dt\":1589972400,\"sunrise\":1589946871,\"sunset\":1590001612,\"temp\":{\"day\":18.28,\"min\":13.38,\"max\":18.28,\"night\":18.28,\"eve\":18.28,\"morn\":13.38},\"feels_like\":{\"day\":17.74,\"night\":17.74,\"eve\":17.74,\"morn\":10.97},\"pressure\":1015,\"humidity\":68,\"dew_point\":12.41,\"wind_speed\":1.78,\"wind_deg\":128,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":3,\"uvi\":7.78}]}";
        String newWeatherData = "{\"lat\":60.99,\"lon\":30.9,\"timezone\":\"Europe/Moscow\",\"current\":{\"dt\":1589386993,\"sunrise\":1589332524,\"sunset\":1589394613,\"temp\":8.89,\"feels_like\":4.79,\"pressure\":997,\"humidity\":67,\"dew_point\":3.1,\"uvi\":3.64,\"clouds\":72,\"wind_speed\":3.74,\"wind_deg\":168,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}]},\"daily\":[{\"dt\":1589360400,\"sunrise\":1589332524,\"sunset\":1589394613,\"temp\":{\"day\":8.89,\"min\":5.18,\"max\":8.89,\"night\":5.18,\"eve\":8.89,\"morn\":8.89},\"feels_like\":{\"day\":4.64,\"night\":1.97,\"eve\":4.64,\"morn\":4.64},\"pressure\":997,\"humidity\":67,\"dew_point\":3.1,\"wind_speed\":3.95,\"wind_deg\":163,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":72,\"uvi\":3.64},{\"dt\":1589446800,\"sunrise\":1589418773,\"sunset\":1589481166,\"temp\":{\"day\":3.92,\"min\":2.47,\"max\":5.01,\"night\":3.58,\"eve\":4.95,\"morn\":2.47},\"feels_like\":{\"day\":1.18,\"night\":-3.3,\"eve\":-0.7,\"morn\":-2.23},\"pressure\":1004,\"humidity\":81,\"dew_point\":1.12,\"wind_speed\":1.28,\"wind_deg\":253,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":99,\"rain\":4.16,\"uvi\":3.54},{\"dt\":1589533200,\"sunrise\":1589505023,\"sunset\":1589567718,\"temp\":{\"day\":4.21,\"min\":2.45,\"max\":5.69,\"night\":3.72,\"eve\":5.69,\"morn\":2.45},\"feels_like\":{\"day\":-0.55,\"night\":-3.1,\"eve\":0.06,\"morn\":-4.35},\"pressure\":1006,\"humidity\":71,\"dew_point\":-2.14,\"wind_speed\":3.85,\"wind_deg\":176,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":100,\"rain\":1.18,\"uvi\":3.79},{\"dt\":1589619600,\"sunrise\":1589591276,\"sunset\":1589654269,\"temp\":{\"day\":4.24,\"min\":3.1,\"max\":5.32,\"night\":4.6,\"eve\":5.06,\"morn\":3.1},\"feels_like\":{\"day\":-2.01,\"night\":1.77,\"eve\":-1.68,\"morn\":-1.79},\"pressure\":1005,\"humidity\":77,\"dew_point\":0.66,\"wind_speed\":6.21,\"wind_deg\":150,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":100,\"uvi\":3.87},{\"dt\":1589706000,\"sunrise\":1589677531,\"sunset\":1589740819,\"temp\":{\"day\":4.11,\"min\":2.95,\"max\":6.73,\"night\":5.31,\"eve\":5.68,\"morn\":2.95},\"feels_like\":{\"day\":-0.35,\"night\":-0.96,\"eve\":0.09,\"morn\":-4.42},\"pressure\":1009,\"humidity\":83,\"dew_point\":1.54,\"wind_speed\":3.86,\"wind_deg\":298,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":75,\"rain\":0.39,\"uvi\":3.57},{\"dt\":1589792400,\"sunrise\":1589763788,\"sunset\":1589827368,\"temp\":{\"day\":5.74,\"min\":4.54,\"max\":6.8,\"night\":5.37,\"eve\":6.8,\"morn\":4.54},\"feels_like\":{\"day\":-3.01,\"night\":-0.41,\"eve\":-1.03,\"morn\":-2.82},\"pressure\":1003,\"humidity\":89,\"dew_point\":4.17,\"wind_speed\":10.64,\"wind_deg\":189,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":100,\"rain\":5.9,\"uvi\":3.65},{\"dt\":1589878800,\"sunrise\":1589850048,\"sunset\":1589913915,\"temp\":{\"day\":5.83,\"min\":4.44,\"max\":6.62,\"night\":6.62,\"eve\":6.28,\"morn\":4.51},\"feels_like\":{\"day\":2.78,\"night\":1.27,\"eve\":1.54,\"morn\":0.12},\"pressure\":1006,\"humidity\":79,\"dew_point\":2.53,\"wind_speed\":2.08,\"wind_deg\":122,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":92,\"uvi\":3.88},{\"dt\":1589965200,\"sunrise\":1589936310,\"sunset\":1590000460,\"temp\":{\"day\":5.92,\"min\":4.04,\"max\":6.75,\"night\":6.29,\"eve\":6.57,\"morn\":4.04},\"feels_like\":{\"day\":-0.04,\"night\":3.18,\"eve\":3.42,\"morn\":-3.25},\"pressure\":1016,\"humidity\":69,\"dew_point\":0.68,\"wind_speed\":5.82,\"wind_deg\":327,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":0,\"uvi\":3.76}]}";

        Weather oldWeather = new Weather(oldWeatherData);
        Weather newWeather = new Weather(newWeatherData);

        Event DUMMY_EVENT_2 = sEventBuilder
                .setTitle(DUMMY_TITLE)
                .setDescription(DUMMY_DESCRIPTION)
                .setDate("16/05/2020 00:00")
                .setOrganizerRef(DUMMY_REF)
                .build();

        mEventsLive.postValue(DUMMY_EVENT_2);

        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        scenario(bundle);

        List<DatabaseObject<Weather>>  weatherList = new ArrayList<>();

        weatherList.add(new DatabaseObject<>("a", oldWeather));
        mWeatherLiveData.postValue(weatherList);

        String oldWeatherType = (String) oldWeather.getWeather(oldWeather.getClosestDay(DUMMY_EVENT_2.getDate())).get("main");
        onView(withId(R.id.weatherType)).check(matches(withText(oldWeatherType)));

        weatherList.add(new DatabaseObject<>("b", newWeather));
        mWeatherLiveData.postValue(weatherList);

        String newWeatherType = (String) newWeather.getWeather(newWeather.getClosestDay(DUMMY_EVENT_2.getDate())).get("main");
        onView(withId(R.id.weatherType)).check(matches(withText(newWeatherType)));
    }

    @Test
    public void EventFragment_DisplayEmptyMessageIfNoAttendee() {
        setupAttendee(new ArrayList<>());

        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        scenario(bundle);

        onView(withId(R.id.event_detail_no_attendees_msg)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.event_detail_attendee_list_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.event_detail_attendee_count)).check(matches(withText(containsString("(0)"))));
    }

    @Test
    public void EventFragment_DisplayListOfAttendees() {
        List<DatabaseObject<User>> attendees = new ArrayList<>();
        attendees.add(new DatabaseObject<>("id", new User("testname", "testemail")));
        setupAttendee(attendees);

        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        scenario(bundle);

        onView(withId(R.id.event_detail_no_attendees_msg)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.event_detail_attendee_list_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.event_detail_attendee_count)).check(matches(withText(containsString("(1)"))));
        onView(withText("testname")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void EventFragment_DisplaysOrganizer() {
        DatabaseObject<User> organizer = new DatabaseObject<>("id", new User("testname", "testemail"));
        setupOrganizer(organizer);

        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        scenario(bundle);

        onView(withId(R.id.organizer_name)).check(matches(withText(containsString(organizer.getObject().getName()))));
    }

}

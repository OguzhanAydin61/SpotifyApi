package oguzhan.spotify.webapi.android;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;

import org.fest.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import oguzhan.spotify.webapi.android.models.Album;
import oguzhan.spotify.webapi.android.models.AlbumSimple;
import oguzhan.spotify.webapi.android.models.Albums;
import oguzhan.spotify.webapi.android.models.AlbumsPager;
import oguzhan.spotify.webapi.android.models.Artist;
import oguzhan.spotify.webapi.android.models.ArtistSimple;
import oguzhan.spotify.webapi.android.models.Artists;
import oguzhan.spotify.webapi.android.models.ArtistsPager;
import oguzhan.spotify.webapi.android.models.AudioFeaturesTrack;
import oguzhan.spotify.webapi.android.models.AudioFeaturesTracks;
import oguzhan.spotify.webapi.android.models.CategoriesPager;
import oguzhan.spotify.webapi.android.models.Category;
import oguzhan.spotify.webapi.android.models.Copyright;
import oguzhan.spotify.webapi.android.models.ErrorDetails;
import oguzhan.spotify.webapi.android.models.ErrorResponse;
import oguzhan.spotify.webapi.android.models.FeaturedPlaylists;
import oguzhan.spotify.webapi.android.models.Followers;
import oguzhan.spotify.webapi.android.models.Image;
import oguzhan.spotify.webapi.android.models.LinkedTrack;
import oguzhan.spotify.webapi.android.models.NewReleases;
import oguzhan.spotify.webapi.android.models.Playlist;
import oguzhan.spotify.webapi.android.models.PlaylistFollowPrivacy;
import oguzhan.spotify.webapi.android.models.PlaylistSimple;
import oguzhan.spotify.webapi.android.models.PlaylistTrack;
import oguzhan.spotify.webapi.android.models.PlaylistTracksInformation;
import oguzhan.spotify.webapi.android.models.PlaylistsPager;
import oguzhan.spotify.webapi.android.models.Recommendations;
import oguzhan.spotify.webapi.android.models.Result;
import oguzhan.spotify.webapi.android.models.SavedTrack;
import oguzhan.spotify.webapi.android.models.Seed;
import oguzhan.spotify.webapi.android.models.SeedsGenres;
import oguzhan.spotify.webapi.android.models.SnapshotId;
import oguzhan.spotify.webapi.android.models.Track;
import oguzhan.spotify.webapi.android.models.TrackSimple;
import oguzhan.spotify.webapi.android.models.TrackToRemove;
import oguzhan.spotify.webapi.android.models.TrackToRemoveWithPosition;
import oguzhan.spotify.webapi.android.models.Tracks;
import oguzhan.spotify.webapi.android.models.TracksPager;
import oguzhan.spotify.webapi.android.models.TracksToRemove;
import oguzhan.spotify.webapi.android.models.TracksToRemoveWithPosition;
import oguzhan.spotify.webapi.android.models.UserPrivate;
import oguzhan.spotify.webapi.android.models.UserPublic;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ParcelableModelsTest {

    @Test
    public void allParcelables() throws IllegalAccessException, InstantiationException, NoSuchFieldException {

        ModelPopulator populator = new ModelPopulator("CREATOR", "$jacocoData");

        for (Class<? extends Parcelable> modelClass : getModelClasses()) {

            Parcelable instance = populator.populateWithRandomValues(modelClass);

            testSingleParcelable(instance);
            testParcelableArray(instance);

            /* Trick to increase code coverage */
            instance.describeContents();
            ((Parcelable.Creator<?>) modelClass.getField("CREATOR").get(null)).newArray(13);
        }
    }

    ArrayList<Class<? extends Parcelable>> getModelClasses() {
        return Lists.newArrayList(
                Album.class,
                Albums.class,
                AlbumSimple.class,
                AlbumsPager.class,
                Artist.class,
                Artists.class,
                ArtistSimple.class,
                ArtistsPager.class,
                AudioFeaturesTrack.class,
                AudioFeaturesTracks.class,
                CategoriesPager.class,
                Category.class,
                Copyright.class,
                ErrorDetails.class,
                ErrorResponse.class,
                FeaturedPlaylists.class,
                Followers.class,
                Image.class,
                LinkedTrack.class,
                NewReleases.class,
                Playlist.class,
                PlaylistFollowPrivacy.class,
                PlaylistSimple.class,
                PlaylistsPager.class,
                PlaylistTrack.class,
                PlaylistTracksInformation.class,
                Recommendations.class,
                Result.class,
                Seed.class,
                SeedsGenres.class,
                SavedTrack.class,
                SnapshotId.class,
                Track.class,
                Tracks.class,
                TrackSimple.class,
                TracksPager.class,
                TrackToRemove.class,
                TrackToRemoveWithPosition.class,
                TracksToRemove.class,
                TracksToRemoveWithPosition.class,
                UserPrivate.class,
                UserPublic.class
        );
    }

    <T extends Parcelable> void testSingleParcelable(T underTest) {

        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(underTest, 0);
        parcel.setDataPosition(0);
        T fromParcel = parcel.readParcelable(underTest.getClass().getClassLoader());

        ModelAssert.assertThat(fromParcel).isEqualByComparingFields(underTest);
    }

    <T extends Parcelable> void testParcelableArray(T underTest) {

        Parcel parcel = Parcel.obtain();
        parcel.writeParcelableArray(Arrays.array(underTest, underTest), 0);
        parcel.setDataPosition(0);
        Parcelable[] fromParcel = parcel.readParcelableArray(underTest.getClass().getClassLoader());

        assertThat(fromParcel).hasSize(2);
        assertThat(fromParcel[0]).isEqualsToByComparingFields(underTest);
        assertThat(fromParcel[1]).isEqualsToByComparingFields(underTest);
    }

    @Test
    public void artistsAreGoodParcelableCitizen() {
        String body = TestUtils.readTestData("artists.json");
        Artists fixture = new GsonBuilder().create().fromJson(body, Artists.class);
        testSingleParcelable(fixture);
    }

    @Test
    public void albumsAreGoodParcelableCitizen() {
        String body = TestUtils.readTestData("albums.json");
        Albums fixture = new GsonBuilder().create().fromJson(body, Albums.class);
        testSingleParcelable(fixture);
    }

    @Test
    public void tracksAreGoodParcelableCitizen() {
        String body = TestUtils.readTestData("tracks.json");
        Tracks fixture = new GsonBuilder().create().fromJson(body, Tracks.class);
        testSingleParcelable(fixture);
    }

    @Test
    public void usersAreGoodParcelableCitizens() {
        String body = TestUtils.readTestData("user.json");
        UserPublic userPublic = new GsonBuilder().create().fromJson(body, UserPublic.class);
        testSingleParcelable(userPublic);

        body = TestUtils.readTestData("current-user.json");
        UserPrivate userPrivate = new GsonBuilder().create().fromJson(body, UserPrivate.class);
        testSingleParcelable(userPrivate);
    }

}
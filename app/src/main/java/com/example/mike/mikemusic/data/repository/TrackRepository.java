package com.example.mike.mikemusic.data.repository;

import android.support.annotation.NonNull;

import com.example.mike.mikemusic.data.model.Playlist;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.data.source.TrackDatasource;
import com.example.mike.mikemusic.data.source.local.TrackLocalDataSource;
import com.example.mike.mikemusic.data.source.remote.TrackRemoteDataSource;

import java.util.List;

import io.reactivex.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ThienNA on 01/08/2018.
 */

public class TrackRepository implements
        TrackDatasource.RemoteDataSource, TrackDatasource.LocalDataSource {

    private static TrackRepository sInstance;

    @NonNull
    private TrackDatasource.RemoteDataSource mRemoteDataSource;
    private TrackDatasource.LocalDataSource mLocalDataSource;

    private TrackRepository(@NonNull TrackDatasource.RemoteDataSource remoteDataSource,
                            @NonNull TrackDatasource.LocalDataSource localDataSource) {
        mRemoteDataSource = checkNotNull(remoteDataSource);
        mLocalDataSource = checkNotNull(localDataSource);
    }

    public static synchronized TrackRepository getInstance(
            TrackDatasource.RemoteDataSource remoteDataSource,
            TrackDatasource.LocalDataSource localDataSource) {
        if (sInstance == null) {
            sInstance = new TrackRepository(remoteDataSource, localDataSource);
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
        TrackRemoteDataSource.destroyInstance();
        TrackLocalDataSource.destroyInstance();
    }

    @Override
    public Observable<List<Track>> getTracksByGenre(String genrePath, int offset) {
        return mRemoteDataSource.getTracksByGenre(genrePath, offset);
    }

    @Override
    public Observable<List<Track>> getTracksLocal() {
        return mLocalDataSource.getTracksLocal();
    }

    @Override
    public Observable<List<Track>> searchTrack(String query, int offset) {
        return mRemoteDataSource.searchTrack(query, offset);
    }

    @Override
    public boolean deleteTrack(Track track) {
        return mLocalDataSource != null && mLocalDataSource.deleteTrack(track);
    }

    @Override
    public void addTracksToPlaylist(int playlistId, TrackDatasource.OnHandleDatabaseListener listener, Track... tracks) {
        if (mLocalDataSource == null) return;
        mLocalDataSource.addTracksToPlaylist(playlistId, listener, tracks);
    }

    @Override
    public void addTracksToNewPlaylist(String newPlaylistName, TrackDatasource.OnHandleDatabaseListener listener, Track... tracks) {
        if (mLocalDataSource == null) return;
        mLocalDataSource.addTracksToNewPlaylist(newPlaylistName, listener, tracks);
    }

    @Override
    public List<Playlist> getPlaylist() {
        if (mLocalDataSource == null) return null;
        return mLocalDataSource.getPlaylist();
    }

    @Override
    public List<Playlist> getDetailPlaylist() {
        if (mLocalDataSource == null) return null;
        return mLocalDataSource.getDetailPlaylist();
    }
}

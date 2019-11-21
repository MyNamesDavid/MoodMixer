package com.example.moodmixer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.moodmixer.dummy.DummyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnPlaylistFragmentInteractionListener}
 * interface.
 */
public class PlaylistFragment extends Fragment  {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnPlaylistFragmentInteractionListener mListener;
    FloatingActionButton floatingActionButtonPlaylist;
    OnPlaylistFragmentInteractionListener callback;

    private RecyclerView.Adapter mAdapter;
    private ArrayList<Playlists> mPlaylists;

    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlaylistFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PlaylistFragment newInstance(int columnCount) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_list, container, false);

        //Code for Floating action button
      /*  FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floating_action_button_playlist_fragment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            // specify an adapter (see also next example)
            mPlaylists = PlaylistSingleton.get(getContext()).getPlaylist();
            mAdapter = new MyPlaylistRecyclerViewAdapter(mPlaylists,mListener,context);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setAdapter(new MyPlaylistRecyclerViewAdapter(Playlists.createPlaylist(10), mListener, context));

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlaylistFragmentInteractionListener) {
            mListener = (OnPlaylistFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPlaylistFragmentInteractionListener {
        // TODO: Update argument type and name

        void onPlaylistFragmentInteraction(Playlists item);
    }

    public void setOnPlaylistFragmentInteractionListener(OnPlaylistFragmentInteractionListener callback){
        this.callback = callback;
    }

    //Code for Floating action button
   /* floatingActionButtonPlaylist =
            (FloatingActionButton) findViewById(R.id.floating_action_button_playlist);
        floatingActionButtonPlaylist.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Handles the click

        }
    });*/

    private void updateUI() {
        PlaylistSingleton playlistSingleton = PlaylistSingleton.get(getActivity());
        ArrayList<Playlists> playlists = playlistSingleton.getPlaylist();
        if (mAdapter == null) {
            mAdapter = new MyPlaylistRecyclerViewAdapter(playlists, mListener, getContext());
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


}

package com.example.lux.popmovieskv.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lux.popmovieskv.R;
import com.example.lux.popmovieskv.activities.MovieDetail;
import com.example.lux.popmovieskv.adapters.FavMoviesAdapter;
import com.example.lux.popmovieskv.database.DatabaseClient;
import com.example.lux.popmovieskv.listeners.OnFavClick;

import com.example.lux.popmovieskv.models.FavMovie;

import java.util.List;


import static com.example.lux.popmovieskv.activities.MovieDetail.CHECK;
import static com.example.lux.popmovieskv.activities.MovieDetail.MOVIE_ID;

public class Fav_Movies_Fragment extends Fragment  {

    public RecyclerView moviesRecyclerView;
    private FavMoviesAdapter favMoviesAdapter;
    private List<FavMovie> favMovies;


    public Fav_Movies_Fragment() {
        // Required empty public constructor
    }


    public static Fav_Movies_Fragment newInstance() {
        Fav_Movies_Fragment fragment = new Fav_Movies_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFavMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav__movies_,container,false);
        moviesRecyclerView = view.findViewById(R.id.movies_list);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        moviesRecyclerView.setAdapter(favMoviesAdapter);
        getActivity().setTitle(getString(R.string.favorite_movies));
        return view;
    }


    private void getFavMovies(){
        class GetFavMovies extends AsyncTask<Void, Void, List<FavMovie>>{
            @Override
            protected List<FavMovie> doInBackground(Void... voids) {
                List<FavMovie> favMovies = DatabaseClient
                        .getInstance(getActivity().getApplicationContext())
                        .getMovieDatabase()
                        .favMovieDao()
                        .getFavMovies();
                return favMovies;
            }

            @Override
            protected void onPostExecute(List<FavMovie> favMovies) {
                super.onPostExecute(favMovies);
                FavMoviesAdapter adapter = new FavMoviesAdapter(getContext(),favMovies, favClick);
                moviesRecyclerView.setAdapter(adapter);
            }

        }
        GetFavMovies gfv = new GetFavMovies();
        gfv.execute();
    }

    OnFavClick favClick = new OnFavClick() {
        @Override
        public void OnClick(FavMovie favMovie) {
            Intent intent = new Intent(getActivity(), MovieDetail.class );
            intent.putExtra(CHECK,false);
            intent.putExtra(MOVIE_ID, favMovie.getId());
            startActivity(intent);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

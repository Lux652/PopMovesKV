package com.example.lux.popmovieskv.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lux.popmovieskv.R;
import com.example.lux.popmovieskv.activities.MovieDetail;
import com.example.lux.popmovieskv.adapters.MoviesAdapter;
import com.example.lux.popmovieskv.listeners.OnMoviesClick;
import com.example.lux.popmovieskv.models.Movie;
import com.example.lux.popmovieskv.models.MoviesResponse;
import com.example.lux.popmovieskv.network.RetrofitManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.lux.popmovieskv.activities.MovieDetail.CHECK;
import static com.example.lux.popmovieskv.activities.MovieDetail.MOVIE_ID;

public class Pop_Movies_Fragment extends Fragment implements Callback<MoviesResponse> {

    public static final String API_KEY = "9aac4d1664cdcf018243622a66c90bf9";
    public RecyclerView moviesRecyclerView;
    private MoviesAdapter moviesAdapter;


    public Pop_Movies_Fragment() {
        // Required empty public constructor
    }


    public static Pop_Movies_Fragment newInstance() {
        Pop_Movies_Fragment fragment = new Pop_Movies_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Call<MoviesResponse> moviesResponseCall = RetrofitManager.getInstance().service().getPopularMovies(API_KEY);
        moviesResponseCall.enqueue(Pop_Movies_Fragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pop__movies_,container,false);
        moviesRecyclerView = view.findViewById(R.id.movies_list);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().setTitle(getString(R.string.now_playing_movies));
        return view;
    }

    @Override
    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
        MoviesResponse moviesResponse = response.body();
        if(response.isSuccessful() && moviesResponse!=null){
            initializeRecyclerView(moviesResponse.getResults());
        }
        else{
            Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<MoviesResponse> call, Throwable t) {
        Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

    }

    void initializeRecyclerView(List<Movie> movies){
        moviesAdapter = new MoviesAdapter(movies, mClick);
        moviesRecyclerView.setAdapter(moviesAdapter);
    }

    OnMoviesClick mClick = new OnMoviesClick() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(getActivity(), MovieDetail.class );
            intent.putExtra(CHECK,true);
            intent.putExtra(MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };
}

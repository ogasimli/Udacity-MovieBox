package org.ogasimli.MovieBox;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import org.ogasimli.MovieBox.fragments.DetailFragment;
import org.ogasimli.MovieBox.fragments.MovieFragment;
import org.ogasimli.MovieBox.objects.MovieList;


public class MainActivity extends AppCompatActivity
        implements MovieFragment.MovieActionListener {

    private static final String DETAIL_FRAGMENT_TAG = "DFT";

    private static final String MOVIE_FRAGMENT_TAG = "MFT";

    public static String PACKAGE_NAME;

    private boolean isDualPane;

    private DetailFragment mDetailFragment;

    private MovieFragment mMoviesFragment;

    private FrameLayout mDetailContainer;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set Window.FEATURE_ACTIVITY_TRANSITIONSin order to enable transition effect
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initToolbar();

        //Get package name to use within intents
        PACKAGE_NAME = getApplicationContext().getPackageName();

        mDetailContainer = (FrameLayout) findViewById(R.id.detail_container);

        isDualPane = (mDetailContainer != null);

        if (savedInstanceState == null) {
            mMoviesFragment = MovieFragment.getInstance(isDualPane);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movie_container, mMoviesFragment, MOVIE_FRAGMENT_TAG)
                    .commit();
        } else {
            mMoviesFragment = (MovieFragment) getSupportFragmentManager().
                    findFragmentByTag(MOVIE_FRAGMENT_TAG);
            mMoviesFragment.setDualPane(isDualPane);
        }
    }

    @Override
    public void onMovieSelected(MovieList.Movie movie, boolean isFavorite, View view) {
        if (!isDualPane) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailFragment.MOVIE, movie);
            intent.putExtra(DetailFragment.FAVORITE, isFavorite);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this,
                                view, "poster");
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else {
            mDetailFragment = DetailFragment.getInstance(movie, isFavorite);
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.detail_container, mDetailFragment, DETAIL_FRAGMENT_TAG).
                    commitAllowingStateLoss();
        }
    }

    /*Initialize Toolbar*/
    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }
}

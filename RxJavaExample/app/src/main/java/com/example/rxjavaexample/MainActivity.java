package com.example.rxjavaexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    //ui
    private RecyclerView recyclerView;

    // vars
    private CompositeDisposable disposables = new CompositeDisposable();
    private RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);

        initRecyclerView();

        getPostObservable()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Post, ObservableSource<Post>>() {
                    @Override
                    public ObservableSource<Post> apply(Post post) throws Exception {
                        return getCommentsObservable(post);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Post post) {
                        updatePost(post);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "errorr: " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    private void updatePost(Post post) {
        adapter.updatePost(post);
    }


    private Observable<Post> getCommentsObservable(final Post post) {
        return ServiceGenerator.getRequestApi()
                .getComments(post.getId())
                .map(new Function<List<Comment>, Post>() {
                    @Override
                    public Post apply(List<Comment> comments) throws Exception {
                        int delay = ((new Random()).nextInt(5) + 1) * 1000;
                        Thread.sleep(delay);
                        Log.d(TAG, "sleeping thread: " + Thread.currentThread().getName() + " for: "
                                + String.valueOf(delay) + " ms ");
                        return post;
                    }
                });
    }

    private Observable<Post> getPostObservable() {
        return ServiceGenerator.getRequestApi()
                .getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<Post>, ObservableSource<Post>>() {
                    @Override
                    public ObservableSource<Post> apply(List<Post> posts) throws Exception {
                        adapter.setPosts(posts);
                        return Observable.fromIterable(posts)
                                .subscribeOn(Schedulers.io());
                    }
                });
    }


    private void initRecyclerView() {
        adapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}

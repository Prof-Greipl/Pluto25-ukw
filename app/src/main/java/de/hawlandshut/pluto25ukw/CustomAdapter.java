package de.hawlandshut.pluto25ukw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.CircularArray;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hawlandshut.pluto25ukw.model.Post;

public class CustomAdapter extends RecyclerView.Adapter{
    public ArrayList<Post> mPostList = new ArrayList<Post>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view, parent, false);
        return new CustomViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = mPostList.get( position );
        ((CustomViewHolder) holder).mLine1.setText( post.email);
        ((CustomViewHolder) holder).mLine2.setText( post.body);
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}

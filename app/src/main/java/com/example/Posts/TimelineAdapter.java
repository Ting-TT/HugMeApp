package com.example.Posts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.hugme.R;

import java.util.List;

/**
 * @author: Xitong Liu
 */

/**
 * This class implements a timeline adapter.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {
    private final Context ctx;
    private final List<PostAll> dataset;
    private final OnPostListener onPostListener;

    public TimelineAdapter(Context ctx, List<PostAll> dataset, OnPostListener onPostListener) {
        this.ctx = ctx;
        this.dataset = dataset;
        this.onPostListener = onPostListener;
    }

    @NonNull
    @Override
    public TimelineAdapter.TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.timeline_post, parent, false);
        return new TimelineViewHolder(view, onPostListener, dataset);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineAdapter.TimelineViewHolder holder, int position) {
        holder.getPostPetSpecies().setText(dataset.get(position).getPe().getPetSpecies());
        holder.getPostPetBreed().setText(dataset.get(position).getPe().getPetBreed());

        Glide.with(ctx).load(dataset.get(position).getPe().getImageList().get(0)).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(holder.getPostImage());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    /**
     * This is a helper subclass for input setting.
     */
    public static class TimelineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView postImage;
        private final TextView postPetSpecies;
        private final TextView postPetBreed;

        OnPostListener onPostListener;
        List<PostAll> posts;

        public TimelineViewHolder(@NonNull View itemView, OnPostListener onPostListener, List<PostAll> posts) {
            super(itemView);
            postImage = (ImageView) itemView.findViewById(R.id.imagePost);
            postPetSpecies = (TextView) itemView.findViewById(R.id.petSpeciesPost);
            postPetBreed = (TextView) itemView.findViewById(R.id.petBreedPost);

            this.onPostListener = onPostListener;
            this.posts = posts;
            itemView.setOnClickListener(this);
        }

        public ImageView getPostImage() {
            return postImage;
        }

        public TextView getPostPetSpecies() {
            return postPetSpecies;
        }

        public TextView getPostPetBreed() {
            return postPetBreed;
        }

        @Override
        public void onClick(View view) {
            onPostListener.postListener(getAdapterPosition(), posts);
        }
    }

    /**
     * This is a helper interface for onclick on recyclerview objects and support intent put extra
     * object<PostAll>.
     */
    public interface OnPostListener {
        void postListener(int position, List<PostAll> posts);
    }
}

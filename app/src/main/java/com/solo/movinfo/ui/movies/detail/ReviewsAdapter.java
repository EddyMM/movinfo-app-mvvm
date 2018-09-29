package com.solo.movinfo.ui.movies.detail;


import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solo.movinfo.R;
import com.solo.movinfo.data.model.Review;

public class ReviewsAdapter extends PagedListAdapter<Review, ReviewsAdapter.ReviewsViewHolder> {

    private static DiffUtil.ItemCallback<Review> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Review>() {

                @Override
                public boolean areItemsTheSame(Review oldItem, Review newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(Review oldItem, Review newItem) {
                    return oldItem.getAuthor().equals(newItem.getAuthor()) &&
                            oldItem.getContent().equals(newItem.getContent());
                }
            };

    private Context mContext;

    ReviewsAdapter(Context context) {
        super(DIFF_CALLBACK);

        mContext = context;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
            int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_review_item_view, parent,
                false);

        return new ReviewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        ReviewsViewHolder(View itemView) {
            super(itemView);
        }

        void bind(Review item) {
            TextView authorTextView = itemView.findViewById(R.id.authorTextView);
            TextView contentTextView = itemView.findViewById(R.id.reviewContentTextView);

            authorTextView.setText(item.getAuthor());
            contentTextView.setText(item.getContent());
        }
    }
}

package com.solo.movinfo.ui.movies.detail;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solo.movinfo.R;
import com.solo.movinfo.data.model.Video;
import com.solo.movinfo.utils.Constants;

import java.util.List;

import timber.log.Timber;

class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {
    private List<Video> mVideoList;
    private Context mContext;

    VideosAdapter(List<Video> videos, Context context) {
        mVideoList = videos;
        mContext = context;
    }

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
            int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_trailer_item_view, parent,
                false);

        return new VideosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, int position) {
        holder.bind(mVideoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mVideoList == null ? 0 : mVideoList.size();
    }

    void submitVideos(List<Video> videos) {
        mVideoList = videos;
        notifyDataSetChanged();
    }

    class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        VideosViewHolder(View itemView) {
            super(itemView);
            Group videoGroup = itemView.findViewById(R.id.videoGroup);

            for (int id : videoGroup.getReferencedIds()) {
                itemView.findViewById(id).setOnClickListener(this);
            }
        }

        void bind(Video video) {
            if (video != null) {
                TextView videoNameTextView = itemView.findViewById(R.id.videoNameTextView);
                videoNameTextView.setText(video.getName());

                TextView videoTypeTextView = itemView.findViewById(R.id.videoTypeTextView);
                videoTypeTextView.setText(video.getType());
            }
        }

        @Override
        public void onClick(View view) {
            Video video = mVideoList.get(getAdapterPosition());

            String videoUrl = Constants.YOU_TUBE_BASE_WATCH_URL + "?"
                    + Constants.YOU_TUBE_BASE_WATCH_URL_VIDEO_KEY + "=" + video.getKey();

            if (view.getId() == R.id.shareVideoImageView) {
                Timber.d("Share YouTube URL");
                shareVideoUrl(videoUrl, video.getName());
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                mContext.startActivity(Intent.createChooser(intent, "Watch video using"));
            }
        }

        private void shareVideoUrl(String url, String title) {
            String mimeType = Constants.PLAIN_TEXT_MIME_TYPE;

            ShareCompat.IntentBuilder
                    .from((MoviesDetailActivity) mContext)
                    .setType(mimeType)
                    .setChooserTitle(title)
                    .setText(url)
                    .startChooser();
        }
    }
}

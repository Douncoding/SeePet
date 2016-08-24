package com.douncoding.seepet.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.douncoding.seepet.Constant;
import com.douncoding.seepet.R;
import com.douncoding.seepet.net.ApiConnection;
import com.douncoding.seepet.view.component.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MediaActivity extends BaseActivity {
    private static final String TAG = MediaActivity.class.getSimpleName();
    private static final String MEDIA_URL = "http://" + Constant.APP_SERVER_IP + "/media";

    GetMediaListTask mediaListTask;

    RecyclerView mMediaListView;
    MediaItemAdapter mItemAdapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MediaActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("재생목록");
        }

        this.setupMediaListView();
        this.mediaListTask = new GetMediaListTask(onResponseCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaListTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mediaListTask.isCancelled()) {
            mediaListTask.cancel(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_media, menu);

        Drawable drawable = menu.findItem(R.id.action_pause).getIcon();
        if (drawable != null) {
            int color = ContextCompat.getColor(this, android.R.color.white);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            menu.findItem(R.id.action_pause).setIcon(drawable);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_pause) {
            new MediaStopTask().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupMediaListView() {
        mMediaListView = (RecyclerView)findViewById(R.id.media_list);
        mItemAdapter = new MediaItemAdapter();

        mMediaListView.setLayoutManager(new LinearLayoutManager(this));
        mMediaListView.addItemDecoration(new DividerItemDecoration(this));
        mMediaListView.setAdapter(mItemAdapter);
    }

    DefaultCallback onResponseCallback = new DefaultCallback() {
        @Override
        public void onCallback(List<String> mediaList) {
            mItemAdapter.setItemCollection(mediaList);
        }
    };

    class MediaPlayTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String url = MEDIA_URL + "/play/" + params[0];
            try {
                String response = ApiConnection.createGET(url).requestSyncCall();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    class MediaStopTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String url = MEDIA_URL + "/stop";
            try {
                ApiConnection.createGET(url).requestSyncCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class GetMediaListTask extends AsyncTask<Void, Void, String> {
        DefaultCallback callback = null;

        public GetMediaListTask(DefaultCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (isCancelled())
                return null;

            try {
                return ApiConnection.createGET(MEDIA_URL).requestSyncCall();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null) return;

            try {
                List<String> mediaList = new ArrayList<>();
                JSONArray array = new JSONArray(s);
                for (int i = 0; i <array.length(); i++) {
                    mediaList.add((String)array.get(i));
                }

                Log.d(TAG, "수신결과:" + s);
                Log.i(TAG, "전송받은 영상개수:" + mediaList.size());
                if (callback != null) {
                    callback.onCallback(mediaList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private interface DefaultCallback {
        void onCallback(List<String> mediaList);
    }

    // 목록 중 하나 선택 - 영상재생
    private OnItemClickedListener onItemClickedListener = new OnItemClickedListener() {
        @Override
        public void onClicked(String name) {
            new MediaPlayTask().execute(name);
        }
    };

    private class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.ViewHolder> {
        List<String> itemList;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_media, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final String item = itemList.get(position);

            if (item != null) {
                holder.mNameText.setText(item);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickedListener != null)
                            onItemClickedListener.onClicked(item);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return itemList == null ? 0 : itemList.size();
        }

        public void setItemCollection(List<String> itemCollection) {
            this.itemList = itemCollection;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mNameText;
            public ViewHolder(View itemView) {
                super(itemView);
                mNameText = (TextView)itemView.findViewById(R.id.name_txt);
            }
        }
    }

    private interface OnItemClickedListener {
        void onClicked(String name);
    }
}

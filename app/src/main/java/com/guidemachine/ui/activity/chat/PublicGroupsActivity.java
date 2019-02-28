package com.guidemachine.ui.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class PublicGroupsActivity extends BaseActivity {
    private ProgressBar pb;
    private ListView listView;
    private GroupsAdapter adapter;
    private List<EMGroupInfo> groupsList;
    private boolean isLoading;
    private boolean isFirstLoading = true;
    private boolean hasMoreData = true;
    private String cursor;
    private final int pagesize = 20;
    private LinearLayout footLoadingLayout;
    private ProgressBar footLoadingPB;
    private TextView footLoadingText;
    private Button searchBtn;
    @Override
    protected int setRootViewId() {
        return R.layout.activity_public_groups;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {

        pb = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.list);
        groupsList = new ArrayList<EMGroupInfo>();
        searchBtn = (Button) findViewById(R.id.btn_search);

        View footView = getLayoutInflater().inflate(R.layout.em_listview_footer_view, listView, false);
        footLoadingLayout = (LinearLayout) footView.findViewById(R.id.loading_layout);
        footLoadingPB = (ProgressBar)footView.findViewById(R.id.loading_bar);
        footLoadingText = (TextView) footView.findViewById(R.id.loading_text);
        listView.addFooterView(footView, null, false);
        footLoadingLayout.setVisibility(View.GONE);

        loadAndShowData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(PublicGroupsActivity.this, GroupSimpleDetailActivity.class).
                        putExtra("groupinfo", adapter.getItem(position)));
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    if(listView.getCount() != 0){
                        int lasPos = view.getLastVisiblePosition();
                        if(hasMoreData && !isLoading && lasPos == listView.getCount()-1){
                            loadAndShowData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    public void search(View view){
        startActivity(new Intent(this, PublicGroupsSeachActivity.class));
    }

    private void loadAndShowData(){
        new Thread(new Runnable() {

            public void run() {
                try {
                    isLoading = true;
                    final EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(pagesize, cursor);
                    final List<EMGroupInfo> returnGroups = result.getData();
                    runOnUiThread(new Runnable() {

                        public void run() {
                            searchBtn.setVisibility(View.VISIBLE);
                            groupsList.addAll(returnGroups);
                            if(returnGroups.size() != 0){
                                cursor = result.getCursor();
                                if(returnGroups.size() == pagesize)
                                    footLoadingLayout.setVisibility(View.VISIBLE);
                            }
                            if(isFirstLoading){
                                pb.setVisibility(View.INVISIBLE);
                                isFirstLoading = false;
                                adapter = new GroupsAdapter(PublicGroupsActivity.this, 1, groupsList);
                                listView.setAdapter(adapter);
                            }else{
                                if(returnGroups.size() < pagesize){
                                    hasMoreData = false;
                                    footLoadingLayout.setVisibility(View.VISIBLE);
                                    footLoadingPB.setVisibility(View.GONE);
                                    footLoadingText.setText("No more data");
                                }
                                adapter.notifyDataSetChanged();
                            }
                            isLoading = false;
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            isLoading = false;
                            pb.setVisibility(View.INVISIBLE);
                            footLoadingLayout.setVisibility(View.GONE);
                            Toast.makeText(PublicGroupsActivity.this, "load failed, please check your network or try it later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
    /**
     * adapter
     *
     */
    private class GroupsAdapter extends ArrayAdapter<EMGroupInfo> {

        private LayoutInflater inflater;

        public GroupsAdapter(Context context, int res, List<EMGroupInfo> groups) {
            super(context, res, groups);
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.em_row_group, parent, false);
            }

            ((TextView) convertView.findViewById(R.id.name)).setText(getItem(position).getGroupName());

            return convertView;
        }
    }

    public void back(View view){
        finish();
    }
}
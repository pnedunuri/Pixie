package com.pixieproto.bluephish.pixieproto;


import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ContextList extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 16;
    private CardViewAdapter mAdapter;
    public static ContextList me;

    private ArrayList<String> metadataToVal = null;
    private ArrayList<String> mItems = null;

    private IntentHandlerListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.activity_context_list);
        String extractedData = getIntent().getStringExtra("ExtractedData");
        extractedData = "{\"ExtractedData\":[{\"metadata\":\"email\",\"data\":[\"kasd@asdf.com\",\"kjgasd@kasd.com\"]},{\"metadata\":\"phonenumber\",\"data\":[6692515281]},{\"metadata\":\"address\",\"data\":[\"527 Walker Dr\"]},{\"metadata\":\"url\",\"data\":[\"www.google.com\",\"www.facebook.com\",\"www.gmail.com\"]},{\"metadata\":\"completetext\",\"data\":\"this is the compelte text extracted.\"}]}";

        mItems = new ArrayList<String>();
        try {
            JSONObject extractedJSON = new JSONObject(extractedData);
            JSONArray jsonArray = extractedJSON.getJSONArray("ExtractedData");
            metadataToVal = new ArrayList<String>();
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject object = jsonArray.getJSONObject(index);
                String metaInfo = object.getString("metadata");

                JSONArray dataArray = object.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        mItems.add(metaInfo);
                        metadataToVal.add(dataArray.getString(i));
                    }
                } else {
                    String val = object.optString("data");
                    if (val != null && !val.equals("null")) {
                        mItems.add(metaInfo);
                        metadataToVal.add(val);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        populateList(0);

//        SwipeableRecyclerViewTouchListener swipeTouchListener =
//                new SwipeableRecyclerViewTouchListener(recyclerView,
//                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
//                            @Override
//                            public boolean canSwipeLeft(int position) {
//                                return true;
//                            }
//
//                            @Override
//                            public boolean canSwipeRight(int position) {
//                                return true;
//                            }
//
//                            @Override
//                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
//                                for (int position : reverseSortedPositions) {
//                                    Log.d("TAG", "Position:::: " + position);
//                                    mAdapter.removeItemAtPosition(position);
//                                }
//                            }
//
//                            @Override
//                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
//                                for (int position : reverseSortedPositions) {
//                                    mAdapter.removeItemAtPosition(position);
//                                }
//                            }
//                        });
//
//        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    private void populateList(int position) {
        position = (position >= mItems.size()) ? (mItems.size() - 1) : position;
        mAdapter = new CardViewAdapter(mItems);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.scrollToPosition(position);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public boolean requestPermissions(IntentHandlerListener listener, String... permissions) {
        this.listener = listener;

        ArrayList<String> requestPermissions = new ArrayList<String>();
        for (int index = 0; index < permissions.length; index++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[index]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions.add(permissions[index]);
            }
        }

        if (requestPermissions.size() > 0) {
            // go for it
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), REQUEST_PERMISSIONS);

            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // check if permissions granted
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listener.processHandlerAfterPermission();
            }
        }
    }

    /**
     * A simple adapter that loads a CardView layout with one TextView and two Buttons, and
     * listens to clicks on the Buttons or on the CardView
     */
    public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
        private List<String> cards;

        public CardViewAdapter(List<String> cards) {
            this.cards = cards;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_layout, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            if (viewHolder.isLayoutInitialized) {
                return;
            }

            String metadata = cards.get(i);
            viewHolder.title.setText(metadata.toUpperCase());
            String extractedVal = metadataToVal.get(i);
            viewHolder.extractedData.setText(extractedVal);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

            ArrayList<String> buttons = Constants.MAP_METADATA_TO_BUTTONS.get(metadata.toUpperCase());

            if (buttons != null) {
                for (int j = 0; j < buttons.size(); j++) {
                    final Button btnTag = new Button(ContextList.this);
                    String text = buttons.get(j);
                    btnTag.setLayoutParams(params);
                    btnTag.setText(text);
                    IntentHandler intentHandler = new IntentHandler(metadata.toUpperCase(), text.toUpperCase(), extractedVal);
                    btnTag.setTag(intentHandler);

                    viewHolder.flowLayout.addView(btnTag);

                    btnTag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IntentHandler handler = (IntentHandler) v.getTag();
                            handler.handleRequest();
                        }
                    });
                }
            }

            viewHolder.isLayoutInitialized = true;
        }

        @Override
        public int getItemCount() {
            return cards == null ? 0 : cards.size();
        }

        public void removeItemAtPosition(int position) {
            mItems.remove(position);
            metadataToVal.remove(position);
            me.populateList(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title;
            private TextView extractedData;
            private FlowLayout flowLayout;
            private Button closeCardButton;
            private boolean isLayoutInitialized;

            public ViewHolder(View itemView) {
                super(itemView);
                isLayoutInitialized = false;

                title = (TextView) itemView.findViewById(R.id.card_view_title);
                extractedData = (TextView) itemView.findViewById(R.id.extractedData);
                closeCardButton = (Button) itemView.findViewById(R.id.closeCard);
                closeCardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItemAtPosition(getAdapterPosition());
                    }
                });

                flowLayout = (FlowLayout) itemView.findViewById(R.id.layout_tags);
            }
        }
    }
}

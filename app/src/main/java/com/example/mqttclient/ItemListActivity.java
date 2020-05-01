package com.example.mqttclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mqttclient.dummy.DummyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements MQTTService.IGetMessageCallBack {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;


    private ImageButton InforButton;
    private ImageButton MachineButton;
    private ImageButton PatientButton;



    private MyServiceConnection serviceConnection;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_list);

        InforButton = (ImageButton)findViewById(R.id.inforbutton) ;
        MachineButton = (ImageButton)findViewById(R.id.machinemanager) ;
        PatientButton = (ImageButton)findViewById(R.id.painfomanager) ;

        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(ItemListActivity.this);
        Intent intent = new Intent(this, MQTTService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                MQTTService.publish("测试一下子");

            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        InforButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InforFragment inforfragment = new InforFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, inforfragment)
                        .commit();

            }
        });
        PatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewCaseFragment newcasefragment = new NewCaseFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, newcasefragment)
                        .commit();

            }
        });
        MachineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StateFragment statefragment = new StateFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, statefragment)
                        .commit();

            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
    }

//    @Override
    public void setMessage(String message) {
//
////        textView.setText(message);
//        mqttService = serviceConnection.getMqttService();
//        mqttService.toCreateNotification(message);
//        AlertDialog alertDialog1 = new Builder(this)
//                .setTitle("MQTT报文")//标题
//                .setMessage(message)//内容
//                .setIcon(R.mipmap.ic_launcher)//图标
//                .create();
//        alertDialog1.show();
//
//
   }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {//监听按下哪个界面
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    switch (item.id){
                        case "4":StatisticFragment statisticFragment = new StatisticFragment();
                            statisticFragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, statisticFragment)
                                    .commit();
                            break;
                        case "2":EvaluateFragment evaluateFragment = new EvaluateFragment();
                            evaluateFragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, evaluateFragment)
                                .commit();
                            break;
                        case "3":TrainFragment trainfragment = new TrainFragment();
                            trainfragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, trainfragment)
                                    .commit();
                            break;
                        case "1":StateFragment statefragment = new StateFragment();
                            statefragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, statefragment)
                                    .commit();
                            Log.d("test","成功");
                            break;

                    }
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
//            holder.mIdView.setText(mValues.get(position).id);

            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
//            final TextView mIdView;
            final TextView mContentView;
            //final ImageView mImageView;
            ViewHolder(View view) {
                super(view);
                //mImageView = (ImageView) view.findViewById(R.id.imageView);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}

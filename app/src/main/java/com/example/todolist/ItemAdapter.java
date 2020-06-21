package com.example.todolist;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DataModel> arrayList;

    public ItemAdapter(Context context, ArrayList<DataModel> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.row_item, null);
        final TextView titleTextView = convertView.findViewById(R.id.title);
        final TextView dateTextView = convertView.findViewById(R.id.dateTitle);
        final TextView timeTextView = convertView.findViewById(R.id.timeTitle);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        checkBox.setTag(position);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos=(int)buttonView.getTag();
                if(isChecked)
                {
                    titleTextView.setPaintFlags(titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    dateTextView.setPaintFlags(dateTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    timeTextView.setPaintFlags(timeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                }
                else
                {
                    titleTextView.setPaintFlags(titleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    dateTextView.setPaintFlags(dateTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    timeTextView.setPaintFlags(timeTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                }
            }
        });
        final ImageView delImageView = convertView.findViewById(R.id.delete);
        delImageView.setTag(position);

        final View finalConvertView = convertView;
        //On delete icon click remove item from list and database
        delImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (int) v.getTag();
                Animation animSlideRight = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
                animSlideRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Fires when animation starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // ...
                        deleteItem(pos);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // ...
                    }
                });
                finalConvertView.startAnimation(animSlideRight);
            }
        });

        DataModel dataModel = arrayList.get(position);
        titleTextView.setText(dataModel.getTitle());
        dateTextView.setText(dataModel.getDate());
        timeTextView.setText(dataModel.getTime());
        String check= dataModel.getCheck();
        if(check.matches("true"))
        {

            checkBox.setChecked(true);
        }
        else
        {
            checkBox.setChecked(false);
        }
        return convertView;

    }

    //Remove item from list and database
    public void deleteItem(int position) {
        deleteItemFromDb(arrayList.get(position).getTitle(), arrayList.get(position).getDate(), arrayList.get(position).getTime());
        arrayList.remove(position);
        notifyDataSetChanged();
    }
    public void updateItem(int position,String  check){
        updateItemInDb(arrayList.get(position).getTitle(), arrayList.get(position).getDate(), arrayList.get(position).getTime(),check);
        notifyDataSetChanged();

    }
    public void updateItemInDb(String name,String date, String time, String check)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        try {
            databaseHelper.update(name,date,time,check);
            toastMsg("Done Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            toastMsg("Something went wrong");
        }
    }

    //Delete item from database
    public void deleteItemFromDb(String name, String date,String time) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        try {
            databaseHelper.deleteData(name, date, time);
            toastMsg("Deleted Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            toastMsg("Something went wrong");
        }
    }

    //Create and call toast messages when necessary
    public void toastMsg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}

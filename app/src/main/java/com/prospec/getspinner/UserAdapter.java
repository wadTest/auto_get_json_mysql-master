package com.prospec.getspinner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private List<User> userList;//list array
    private List<User> tempList;
    private List<User> suggestionList;


    public UserAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        userList = objects;
        tempList = new ArrayList<>(userList);
        suggestionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_column, parent, false);

        TextView textView = convertView.findViewById(R.id.simple_text);

        User user = userList.get(position);

//        ตำบล อำเภอ จังหวัด รหัสไปรษณีย์
        textView.setText(user.getName() + "  " + user.getTambon() + "  " + user.getProvince() + "  " + user.getCode());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return userFilter;
    }

//    ตัวกรองผู้ใช้
    Filter userFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            User user = (User) resultValue;

            return user.getName() + "  " + user.getTambon() + "  " + user.getProvince() + "  " + user.getCode();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                suggestionList.clear();
                constraint = constraint.toString().trim().toLowerCase();

                for (User user : tempList) {

//                    ตรงนี้ที่เชื่อมโยงกัน (ต.อ.จ.ร.)
                    if (user.getName().toLowerCase().contains(constraint)
                            || user.getTambon().toLowerCase().contains(constraint)
                            || user.getProvince().toLowerCase().contains(constraint)
                            || user.getCode().toLowerCase().contains(constraint)) {

                        suggestionList.add(user);
                    }
                }

                filterResults.count = suggestionList.size();
                filterResults.values = suggestionList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<User> uList = (ArrayList<User>) results.values;

            if (results != null && results.count > 0) {
                clear();
                for (User u : uList) {
                    add(u);
                    notifyDataSetChanged();
                }
            }

        }
    };
}
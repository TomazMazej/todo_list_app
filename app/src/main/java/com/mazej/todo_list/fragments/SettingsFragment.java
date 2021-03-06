package com.mazej.todo_list.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mazej.todo_list.ApplicationTodoList;
import com.mazej.todo_list.R;
import com.mazej.todo_list.database.TodoListAPI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mazej.todo_list.activities.MainActivity.toolbar;

public class SettingsFragment extends Fragment {

    private SharedPreferences sp;
    private ApplicationTodoList app;

    private Switch sw;
    private TextView userTextView;
    private TextView counter;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        app = (ApplicationTodoList) getActivity().getApplication();

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Settings");

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        userTextView = view.findViewById(R.id.usernameTV);
        sw = view.findViewById(R.id.Notifications_switch);
        counter = view.findViewById(R.id.list_count);

        counter.setText("My TODO Lists: " + app.listCounter);

        return view;
    }
}
package com.mazej.todo_list.adapters;

import static com.mazej.todo_list.database.TodoListAPI.retrofit;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.mazej.todo_list.ApplicationTodoList;
import com.mazej.todo_list.R;
import com.mazej.todo_list.database.PutTask;
import com.mazej.todo_list.database.TodoListAPI;
import com.mazej.todo_list.objects.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAdapter extends ArrayAdapter<Task> {

    private TodoListAPI todoListAPI;

    private final Context mContext;
    private final int mResource;
    private LayoutInflater inflater;

    private String name;
    private String description;

    private TextView tvName;
    private TextView tvDate;
    public CheckBox simpleCheckBox;

    public ArrayList<Task> tasksList;
    private final String todoListId;

    private final ApplicationTodoList app;

    public TaskAdapter(Context context, int resource, ArrayList<Task> objects, String todoListId, ApplicationTodoList app) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        this.tasksList = objects;
        this.todoListId = todoListId;
        this.app = app;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Task task = getItem(position);
        name = getItem(position).getName();
        description = getItem(position).getDescription();

        DateTimeFormatter inputParser = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDate date = LocalDate.parse(getItem(position).getDueDate(), inputParser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
        String output = date.format(formatter);

        inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        tvName = (TextView) convertView.findViewById(R.id.nameText);
        tvDate = (TextView) convertView.findViewById(R.id.dateText);
        simpleCheckBox = (CheckBox) convertView.findViewById(R.id.simpleCheckBox);

        String temp = name + "\n" + description;
        SpannableString ss1=  new SpannableString(temp);
        ss1.setSpan(new RelativeSizeSpan(0.75f),name.length(),temp.length(), 0);

        tvName.setText(ss1);
        tvDate.setText(output);
        if(task.isCompleted()) {
            simpleCheckBox.setChecked(true);
            tvName.setPaintFlags(tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // On checkbox change
        simpleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                String name = task.getName();
                String description = task.getDescription();
                String date = task.getDueDate();
                // Po??ljemo zahtevo za spremembo opravila
                PutTask list = new PutTask(ApplicationTodoList.idAPP, task.getId(), name, description, date,
                        !task.isCompleted());
                todoListAPI = retrofit.create(TodoListAPI.class);
                Call<PutTask> call = todoListAPI.putTask(list, ApplicationTodoList.idAPP, todoListId, task.getId());

                call.enqueue(new Callback<PutTask>() {
                    @Override
                    public void onResponse(Call<PutTask> call, Response<PutTask> response) {
                        if (!response.isSuccessful()) { // ??e zahteva ni uspe??na
                            System.out.println("Response: CheckboxPut neuspesno!");
                            System.out.println(date);
                        }
                        else {
                            System.out.println("Response: CheckboxPut uspe??no!");

                            for (Task task : app.theList) {
                                if (task.getName().equals(name) && task.getDescription().equals(description)) {
                                    task.setCompleted(!task.isCompleted());
                                    TaskAdapter.super.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<PutTask> call, Throwable t) {
                        System.out.println("No response: CheckboxPut neuspe??no!");
                        System.out.println(t);
                    }
                });
            }
        });
        return convertView;
    }
}

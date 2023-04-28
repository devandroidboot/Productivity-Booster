package com.codegama.productivity_booster.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codegama.productivity_booster.adapter.TaskAdapter;
import com.codegama.productivity_booster.bottomSheetFragment.CreateTaskBottomSheetFragment;
import com.codegama.productivity_booster.database.DatabaseClient;
import com.codegama.productivity_booster.model.Task;
import com.codegama.productivity_booster.R;

import java.util.ArrayList;
import java.util.List;


public class TaskFragment extends Fragment implements CreateTaskBottomSheetFragment.setRefreshListener {

    private RecyclerView taskRecycler;
    private TaskAdapter taskAdapter;
    private List<Task> tasks = new ArrayList<>();
    private ImageView noDataImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tasks, container, false);

        taskRecycler = rootView.findViewById(R.id.taskRecycler);
        noDataImage = rootView.findViewById(R.id.noDataImage);

        setUpAdapter();



        //recharge
        //Glide.with(getContext()).load(R.drawable.oie).into(noDataImage);





        getSavedTasks();

        return rootView;
    }


    public void setUpAdapter() {
        taskAdapter = new TaskAdapter(this, tasks, this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskRecycler.setAdapter(taskAdapter);
    }


    public void getSavedTasks() {
        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                tasks = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllTasksList();
                return tasks;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                if (tasks.isEmpty()) {
                    //Glide.with(getContext()).load(R.drawable.newtask).into(noDataImage);
                }
                noDataImage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    @Override
    public void refresh() {
        getSavedTasks();
    }
}

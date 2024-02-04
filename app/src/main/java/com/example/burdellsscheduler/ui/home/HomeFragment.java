package com.example.burdellsscheduler.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.burdellsscheduler.Assignments;
import com.example.burdellsscheduler.Classes;
import com.example.burdellsscheduler.Events;
import com.example.burdellsscheduler.R;
import com.example.burdellsscheduler.Todos;
import com.example.burdellsscheduler.databinding.FragmentHomeBinding;
import com.example.burdellsscheduler.myclasses;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public ArrayList<Events> calendar_events = new ArrayList<>();
    final String time_format = "YYYY-MM-DD";
    private LocalDateTime date;
    private FragmentHomeBinding binding;
    private RecyclerView calendar_recycleView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            calendar_events = (ArrayList<Events>) savedInstanceState.getSerializable("MyCalendarEvents");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("MyCalendarEvents", (Serializable) calendar_events);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View calendarView = inflater.inflate(R.layout.calendar_layout,container, false);
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return calendarView;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        date = LocalDateTime.now();
        calendar_recycleView = view.findViewById(R.id.calendar_event_list);
        CalendarAdapter calendarAdapter = new CalendarAdapter();
        calendar_recycleView.setAdapter(calendarAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration mDivider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        calendar_recycleView.addItemDecoration(mDivider);
        calendar_recycleView.setLayoutManager(layoutManager);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(150);
        itemAnimator.setRemoveDuration(150);
        TextView date_represent = view.findViewById(R.id.calendar_date);
        renewTime(date_represent);
        Button prev_week_button = view.findViewById(R.id.prev_week_button);
        Button next_week_button = view.findViewById(R.id.next_week_button);
        Button mon_button = view.findViewById(R.id.mon_button);
        Button tues_button = view.findViewById(R.id.tues_button);
        Button wed_button = view.findViewById(R.id.wed_button);
        Button thur_button = view.findViewById(R.id.thu_button);
        Button fri_button = view.findViewById(R.id.fri_button);
        Button sat_button = view.findViewById(R.id.sat_button);
        Button sun_button = view.findViewById(R.id.sun_button);
        DayOfWeek today = date.getDayOfWeek();
        //setting the color of the chose button to be background in gold color
        switch (today) {
            case MONDAY:
                mon_button.setBackgroundColor(getResources().getColor(R.color.tech_gold));
                break;

            case TUESDAY:
                tues_button.setBackgroundColor(getResources().getColor(R.color.tech_gold));
                break;

            case WEDNESDAY:
                wed_button.setBackgroundColor(getResources().getColor(R.color.tech_gold));
                break;

            case THURSDAY:
                thur_button.setBackgroundColor(getResources().getColor(R.color.tech_gold));
                break;

            case FRIDAY:
                fri_button.setBackgroundColor(getResources().getColor(R.color.tech_gold));
                break;
        }
        prev_week_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = date.minusWeeks(1);
               renewTime(date_represent);
            }
        });
        next_week_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = date.plusWeeks(1);
                renewTime(date_represent);
            }
        });
        Todos testingToso = new Todos("aaa",LocalDateTime.now(), new Classes("cs1332"));
        calendarAdapter.notifyItemInserted(calendarAdapter.getItemCount());
        calendarAdapter.notifyItemChanged(calendarAdapter.getItemCount()-1);
    }

    private void renewTime(TextView date_represent) {
        date_represent.setText(date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
        @NonNull
        @Override
        public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_layout, parent, false);
            CalendarViewHolder calendarViewHolder = new CalendarViewHolder(view);
            return calendarViewHolder;
        }

        @Override
        public int getItemCount() {
            return calendar_events.size();
        }
        @Override
        public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
            Events events = calendar_events.get(position);
            holder.calendar_event_title.setText("Title: " + events.getLabel());
            holder.calendar_event_date.setText("Date: " + events.getTime().getMonthValue() + "-" + events.getTime().getDayOfMonth());
            holder.calendar_event_time.setText("Times: " + events.getTime().getHour() + ":" + events.getTime().getMinute());
            holder.calendar_event_class.setText("Associated class: " + events.getClass());
        }
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView calendar_event_title;
        TextView calendar_event_date;
        TextView calendar_event_time;
        TextView calendar_event_class;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            calendar_event_title = itemView.findViewById(R.id.calendar_event_title);
            calendar_event_date = itemView.findViewById(R.id.calendar_event_date);
            calendar_event_time = itemView.findViewById(R.id.calendar_event_time);
            calendar_event_class = itemView.findViewById(R.id.calendar_event_class);
        }
    }
}

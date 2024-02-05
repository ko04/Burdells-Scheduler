    package com.example.burdellsscheduler.ui.home;

    import android.content.res.ColorStateList;
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
    import com.example.burdellsscheduler.Exams;
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

        public static ArrayList<Events> allEvents = new ArrayList<>();
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
                allEvents = (ArrayList<Events>) savedInstanceState.getSerializable("AllEvents");
            }
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putSerializable("MyCalendarEvents", (Serializable) calendar_events);
            outState.putSerializable("AllEvents",(Serializable) allEvents);
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
            changingDateInList(calendarAdapter);
            Button prev_week_button = view.findViewById(R.id.prev_week_button);
            Button next_week_button = view.findViewById(R.id.next_week_button);
            Button mon_button = view.findViewById(R.id.mon_button);
            Button tues_button = view.findViewById(R.id.tues_button);
            Button wed_button = view.findViewById(R.id.wed_button);
            Button thur_button = view.findViewById(R.id.thu_button);
            Button fri_button = view.findViewById(R.id.fri_button);
            Button sat_button = view.findViewById(R.id.sat_button);
            Button sun_button = view.findViewById(R.id.sun_button);
            ArrayList<Button> arr = new ArrayList<>();
            arr.add(mon_button);
            arr.add(tues_button);
            arr.add(wed_button);
            arr.add(thur_button);
            arr.add(fri_button);
            arr.add(sat_button);
            arr.add(sun_button);
            switch (date.getDayOfWeek()) {
                case MONDAY: {
                    setTint(mon_button, arr);
                    break;
                }
                case TUESDAY: {
                    setTint(tues_button, arr);
                    break;
                }
                case WEDNESDAY: {
                    setTint(wed_button, arr);
                    break;
                }
                case THURSDAY: {
                    setTint(thur_button, arr);
                    break;
                }
                case FRIDAY: {
                    setTint(fri_button, arr);
                    break;
                }
                case SATURDAY: {
                    setTint(sat_button, arr);
                    break;
                }
                case SUNDAY: {
                    setTint(sun_button, arr);
                    break;
                }
            }
            prev_week_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    date = date.minusWeeks(1);
                   renewTime(date_represent);
                   changingDateInList(calendarAdapter);
                }
            });
            next_week_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    date = date.plusWeeks(1);
                    renewTime(date_represent);
                    changingDateInList(calendarAdapter);
                }
            });
            mon_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTint(mon_button, arr);
                    setDayOfWeek(DayOfWeek.MONDAY);
                    renewTime(date_represent);
                    changingDateInList(calendarAdapter);
                }
            });
            tues_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTint(tues_button,arr);
                    setDayOfWeek(DayOfWeek.TUESDAY);
                    renewTime(date_represent);
                    changingDateInList(calendarAdapter);
                }
            });
            wed_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTint(wed_button,arr);
                    setDayOfWeek(DayOfWeek.WEDNESDAY);
                    renewTime(date_represent);
                    changingDateInList(calendarAdapter);
                }
            });
            thur_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTint(thur_button,arr);
                    setDayOfWeek(DayOfWeek.THURSDAY);
                    renewTime(date_represent);
                    changingDateInList(calendarAdapter);
                }
            });
            fri_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTint(fri_button,arr);
                    setDayOfWeek(DayOfWeek.FRIDAY);
                    renewTime(date_represent);
                    changingDateInList(calendarAdapter);
                }
            });
            sat_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTint(sat_button,arr);
                    setDayOfWeek(DayOfWeek.SATURDAY);
                    renewTime(date_represent);
                    changingDateInList(calendarAdapter);
                }
            });
            sun_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTint(sun_button,arr);
                    setDayOfWeek(DayOfWeek.SUNDAY);
                    renewTime(date_represent);
                    changingDateInList(calendarAdapter);
                }
            });

        }
        private void setTint(Button button, ArrayList<Button> arr) {
            ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.tech_gold));
            ColorStateList colorStateList2 = ColorStateList.valueOf(getResources().getColor(R.color.light_tech_gold));
            for (int i = 0; i < arr.size(); i++) {
                arr.get(i).setBackgroundTintList(colorStateList);
            }
            button.setBackgroundTintList(colorStateList2);
        }

        private void renewTime(TextView date_represent) {
            date_represent.setText(date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth());
        }

        private void setDayOfWeek(DayOfWeek day) {
            if (!date.getDayOfWeek().equals(day)) {
                date = date.plusDays(day.getValue() - date.getDayOfWeek().getValue());
            }
        }

        private void changingDateInList(CalendarAdapter adapter) {
            for (int i = 0; i < calendar_events.size(); i++) {
                adapter.notifyItemRemoved(i);
            }
            calendar_events.clear();
            for (Events events :
                    allEvents) {
                if (events.getTime().getYear() == date.getYear() && events.getTime().getMonthValue() == date.getMonthValue() && events.getTime().getDayOfMonth() == date.getDayOfMonth()){
                    calendar_events.add(events);
                }
            }
            for (int i = 0; i < calendar_events.size(); i++) {
                adapter.notifyItemChanged(i);
            }
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_calendar,parent, false);
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
                holder.calendar_event_date.setText("Date: " + events.getTime().getMonth() + "-" + events.getTime().getDayOfMonth());
                holder.calendar_event_time.setText("Times: " + formalizeTime(events));
                if (events.getClass() == Assignments.class)
                holder.calendar_event_class.setText("Associated class: " + ((Assignments) events).getAssociatedClass().getClassName());
                if (events.getClass() == Exams.class) {
                    holder.calendar_event_class.setText("Associated class: " + ((Exams) events).getAssociatedClass().getClassName());
                }
                if (events.getClass() == Todos.class) {
                    holder.calendar_event_class.setText("Associated class: " + ((Todos) events).getAssociatedClass().getClassName());
                }
            }
        }

        private String formalizeTime(Events events) {
            return String.format("%02d:%02d",events.getTime().getHour(),events.getTime().getMinute());
        }
        class CalendarViewHolder extends RecyclerView.ViewHolder {
            TextView calendar_event_title;
            TextView calendar_event_date;
            TextView calendar_event_time;
            TextView calendar_event_class;

            public CalendarViewHolder(@NonNull View itemView) {
                super(itemView);
                System.out.println(itemView);
                calendar_event_title = itemView.findViewById(R.id.calendar_event_title);
                calendar_event_date = itemView.findViewById(R.id.calendar_event_date);
                calendar_event_time = itemView.findViewById(R.id.calendar_event_time);
                calendar_event_class = itemView.findViewById(R.id.calendar_event_class);
            }
        }
    }

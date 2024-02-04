package com.example.burdellsscheduler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.burdellsscheduler.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class mytodos extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter ;
    private List<Todos> mNewsList;



    public mytodos() {
        // Required empty public constructor
    }
    public static myclasses newInstance(String param1, String param2) {
        myclasses fragment = new myclasses();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsList = new ArrayList<>();
        if (savedInstanceState != null) {
            mNewsList = (List<Todos>) savedInstanceState.getSerializable("MyTodosList");
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("MyTodosList", (Serializable) mNewsList);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getActivity().findViewById(R.id.recyclerviewTodo);
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration mDivider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        mRecyclerView.setLayoutManager(layoutManager);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(150);
        itemAnimator.setRemoveDuration(150);
        mRecyclerView.setItemAnimator(itemAnimator);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            //deletion with only a swipe
//                Classes deletedCourse = mNewsList.get(viewHolder.getAdapterPosition());
//                int position = viewHolder.getAdapterPosition();
//                mNewsList.remove(viewHolder.getAdapterPosition());
//                mMyAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                Snackbar.make(mRecyclerView, "Deleted: " + deletedCourse.getClassName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mNewsList.add(position, deletedCourse);
//                        mMyAdapter.notifyItemInserted(position);
//                    }
//                }).show();

                //try to add an deletion button when user swipe the new class to the left(new feature)
                Todos deletedTodos = mNewsList.get(viewHolder.getAdapterPosition());
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                TextView textView = new TextView(getContext());
                textView.setText("Are you sure you want to delete todo: " + deletedTodos.getLabel());
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
                textView.setTypeface(face);
                textView.setTextSize(24);
                textView.setPadding(64, 64, 32, 32);
                textView.setTextColor(getResources().getColor(R.color.tech_gold));
                alertDialog.setCustomTitle(textView);
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                linearLayout.setPadding(64, 0, width - 964, 0);
                ScrollView scrollView = new ScrollView(getContext());
                scrollView.addView(linearLayout);
                alertDialog.setView(scrollView);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                        mMyAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Events a = mNewsList.remove(viewHolder.getAdapterPosition());
                        HomeFragment.allEvents.remove(a);
                        mMyAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                });
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.tech_gold));

            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton more = getActivity().findViewById(R.id.addButtonTodo);
        more.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
        more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.NavigationView).create();
                TextView textView = new TextView(getContext());
                textView.setText("Add your todo");
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
                textView.setTypeface(face);
                textView.setTextSize(32);
                textView.setPadding(64, 64, 0, 32);
                textView.setTextColor(getResources().getColor(R.color.tech_gold));
                alertDialog.setCustomTitle(textView);
                alertDialog.setMessage("Please enter your title, due date, due time, and associated class below.");
                LinearLayout linearLayout = new LinearLayout(getContext());
                EditText name = createEditText("Title");

                //old ways to do the time input
//              EditText time = createEditText("Meeting Times");

                LayoutInflater inflater = LayoutInflater.from(getContext());
                View timePickerView = inflater.inflate(R.layout.time_picker, null);
                TimePicker timePicker = timePickerView.findViewById(R.id.timePicker);
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(12);
                timePicker.setCurrentMinute(00);
                setTypefaceToTimePicker(timePicker, face);
                View dateView = inflater.inflate(R.layout.date_picker, null);
                DatePicker datePicker = dateView.findViewById(R.id.datePicker);
                setTypefaceToTimePicker(datePicker, face);
                datePicker.init(LocalDateTime.now().getYear(),(LocalDateTime.now().getMonthValue() + 11) % 12, LocalDateTime.now().getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        // handle the date change
                    }
                });
                EditText associatedClass = createEditText("Associated class");
                linearLayout.addView(name);
                linearLayout.addView(datePicker);
                linearLayout.addView(timePicker);
                linearLayout.addView(associatedClass);

                LinearLayout linearLayout1 = new LinearLayout(getContext());
                linearLayout1.setOrientation(LinearLayout.HORIZONTAL);

                CheckBox checkBox = new CheckBox(getContext());
                checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                checkBox.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf"));
                checkBox.setChecked(false);

                linearLayout1.addView(checkBox);

                TextView textView1 = new TextView(getContext());
                int[] notifyTime = new int[]{20};
                textView1.setText(String.format("Notify me %d minutes before due time.", notifyTime[0]));
                linearLayout1.addView(textView1);

                Spinner spinner = new Spinner(getContext());
                spinner.setLayoutMode(Spinner.MODE_DROPDOWN);
                spinner.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));


                Integer[] timeOptions = new Integer[]{1, 5, 10, 15, 20, 30, 60, 120, 180, 240, 300, 360, 720, 1440};
                ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, timeOptions){

                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        Typeface externalFont=Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
                        ((TextView) v).setTypeface(externalFont);

                        return v;
                    }


                    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                        View v =super.getDropDownView(position, convertView, parent);

                        Typeface externalFont=Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
                        ((TextView) v).setTypeface(externalFont);
                        v.setBackgroundColor(getResources().getColor(R.color.tech_gold));

                        return v;
                    }
                };
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setSelection(Arrays.asList(timeOptions).indexOf(10));


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        notifyTime[0] = (Integer) parent.getItemAtPosition(position);
                        textView1.setText(String.format("Notify me %d minutes before due time.", notifyTime[0]));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing
                    }
                });

                linearLayout1.addView(spinner);
                linearLayout.addView(linearLayout1);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                linearLayout.setPadding(64, 0, width - 964, 0);
                ScrollView scrollView = new ScrollView(getContext());
                scrollView.addView(linearLayout);
                alertDialog.setView(scrollView);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Confirm", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        LocalDateTime time = LocalDateTime.of(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
                        Todos addingNewTodo = new Todos(name.getText().toString(), time, new Classes(associatedClass.getText().toString()));
                        mNewsList.add(new Todos(name.getText().toString(), time, new Classes(associatedClass.getText().toString())));
                        HomeFragment.allEvents.add(addingNewTodo);
                        mMyAdapter.notifyItemInserted(mMyAdapter.getItemCount());
                        if (checkBox.isChecked()) {
                            List<Events> a = new ArrayList<>();
                            for (int i = 0; i < mNewsList.size(); i++) {
                                a.add(mNewsList.get(i));
                            }
                            NotificationScheduler.scheduleNotifications(getContext(), a, "To-do coming: ", notifyTime[0]);
                        }
                        /*
                        if (getActivity().findViewById(R.id.radioButtonClass).isSelected()) {
                            for (int i = 1; i < mNewsList.size(); i++) {
                                for (int j = 0; j < mNewsList.size() - i; j++) {
                                    if (mNewsList.get(j).compareClass(mNewsList.get(j + 1)) > 0) {
                                        Assignments temp = mNewsList.get(j);
                                        mNewsList.set(j, mNewsList.get(j + 1));
                                        mNewsList.set(j + 1, temp);
                                    }
                                }

                            }
                            for (int i = 0; i < mNewsList.size(); i++) {
                                mMyAdapter.notifyItemChanged(i);
                            }
                        }
                        if (getActivity().findViewById(R.id.radioButtonDate).isSelected()) {
                            for (int i = 1; i < mNewsList.size(); i++) {
                                for (int j = 0; j < mNewsList.size() - i; j++) {
                                    if (mNewsList.get(j).compareDate(mNewsList.get(j + 1))) {
                                        Assignments temp = mNewsList.get(j);
                                        mNewsList.set(j, mNewsList.get(j + 1));
                                        mNewsList.set(j + 1, temp);
                                    }
                                }

                            }
                            for (int i = 0; i < mNewsList.size(); i++) {
                                mMyAdapter.notifyItemChanged(i);
                            }
                        }
                        */
                        mNewsList.sort(new Comparator<Todos>() {
                            @Override
                            public int compare(Todos o1, Todos o2) {
                                return o1.compareTo(o2);
                            }
                        });
                        for (int i = 0; i < mNewsList.size(); i++) {
                            mMyAdapter.notifyItemChanged(i);
                        }
                    }
                });
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.tech_gold));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.tech_gold));
            }
        });
        /*
        RadioButton sortByClass = getActivity().findViewById(R.id.radioButtonClass);
        RadioButton sortByDate = getActivity().findViewById(R.id.radioButtonDate);
        sortByDate.setSelected(false);
        sortByClass.setSelected(false);
        sortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i < mNewsList.size(); i++) {
                    for (int j = 0; j < mNewsList.size() - i; j++) {
                        if (mNewsList.get(j).compareDate(mNewsList.get(j + 1))) {
                            Assignments temp = mNewsList.get(j);
                            mNewsList.set(j, mNewsList.get(j + 1));
                            mNewsList.set(j + 1, temp);
                        }
                    }

                }
                for (int i = 0; i < mNewsList.size(); i++) {
                    mMyAdapter.notifyItemChanged(i);
                }
                sortByClass.setSelected(false);
                sortByDate.setSelected(true);
            }
        });
        sortByClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i < mNewsList.size(); i++) {
                    for (int j = 0; j < mNewsList.size() - i; j++) {
                        if (mNewsList.get(j).compareClass(mNewsList.get(j + 1)) > 0) {
                            Assignments temp = mNewsList.get(j);
                            mNewsList.set(j, mNewsList.get(j + 1));
                            mNewsList.set(j + 1, temp);
                        }
                    }

                }
                for (int i = 0; i < mNewsList.size(); i++) {
                    mMyAdapter.notifyItemChanged(i);
                }
                sortByClass.setSelected(true);
                sortByDate.setSelected(false);
            }
        });
        */
    }

    private CheckBox createCheckBox(String hint) {
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setChecked(false);
        checkBox.setHint(hint);
        return checkBox;
    }

    private String getDays(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            return "1";
        } else {
            return "0";
        }
    }
    private EditText createEditText(String nameString) {
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.tech_gold));
        final EditText name = new EditText(getContext());
        name.setHint(nameString);
        name.setPadding(0,32, 0, 32);
        name.setTypeface(face);
        name.setMinWidth(900);
        name.setBackgroundTintList(colorStateList);
        final CharSequence rememberHint = name.getHint();
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && TextUtils.isEmpty(name.getText())) {
                    name.setHint(rememberHint);
                } else {
                    name.setHint("");
                }
            }
        });
        return name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mytodos, container, false);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {
        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //View view = View.inflate(getActivity(), R.layout.item_mylist, null);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_todos,parent, false);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }
        private EditText createEditText(String nameString) {
            Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
            ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.tech_gold));
            final EditText name = new EditText(getContext());
            name.setHint(nameString);
            name.setPadding(0,32, 0, 32);
            name.setTypeface(face);
            name.setMinWidth(900);
            name.setBackgroundTintList(colorStateList);
            final CharSequence rememberHint = name.getHint();
            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b && TextUtils.isEmpty(name.getText())) {
                        name.setHint(rememberHint);
                    } else {
                        name.setHint("");
                    }
                }
            });
            return name;
        }
        private String addZero(int a) {
            if (a / 10 == 0) {
                return "0" + a;
            }
            return "" + a;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            Todos news = mNewsList.get(position);
            holder.mTitleTv.setText("Title: "+news.getLabel());
            holder.mTitleContent.setText("Due Date: " + news.getTime().getMonth().toString().substring(0,1) + news.getTime().getMonth().toString().substring(1).toLowerCase() + " " + news.getTime().getDayOfMonth() + " "+ news.getTime().getYear());

            holder.m2.setText("Due Time: " + addZero(news.getTime().getHour())+":" + addZero(news.getTime().getMinute()));
            holder.m3.setText("Associated Class: " + news.getAssociatedClass().getClassName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.NavigationView).create();
                    TextView textView = new TextView(getContext());
                    textView.setText("Edit your todo");
                    Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
                    textView.setTypeface(face);
                    textView.setTextSize(32);
                    textView.setPadding(64, 64, 0, 32);
                    textView.setTextColor(getResources().getColor(R.color.tech_gold));
                    alertDialog.setCustomTitle(textView);
                    alertDialog.setMessage("Please enter your title, due date, due time, and associated class below.");
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    EditText name = createEditText(news.getLabel());

                    //old ways to do the time input
//              EditText time = createEditText("Meeting Times");

                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View timePickerView = inflater.inflate(R.layout.time_picker, null);
                    TimePicker timePicker = timePickerView.findViewById(R.id.timePicker);
                    timePicker.setIs24HourView(true);
                    timePicker.setCurrentHour(12);
                    timePicker.setCurrentMinute(00);
                    setTypefaceToTimePicker(timePicker, face);
                    View dateView = inflater.inflate(R.layout.date_picker, null);
                    DatePicker datePicker = dateView.findViewById(R.id.datePicker);
                    setTypefaceToTimePicker(datePicker, face);
                    datePicker.init(news.getTime().getYear(),(news.getTime().getMonthValue() + 11) % 12, news.getTime().getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            // handle the date change
                        }
                    });
                    EditText associatedClass = createEditText(news.getAssociatedClass().getClassName());
                    linearLayout.addView(name);
                    linearLayout.addView(datePicker);
                    linearLayout.addView(timePicker);
                    linearLayout.addView(associatedClass);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int width = displayMetrics.widthPixels;
                    linearLayout.setPadding(64, 0, width - 964, 0);
                    ScrollView scrollView = new ScrollView(getContext());
                    scrollView.addView(linearLayout);
                    alertDialog.setView(scrollView);
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Confirm", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            LocalDateTime time = LocalDateTime.of(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
                            if (!name.getText().toString().isEmpty()) news.setLabel(name.getText().toString());
                            news.setTime(time);
                            if (!associatedClass.getText().toString().isEmpty()) {
                                news.setAssociatedClass(new Classes(associatedClass.getText().toString()));
                            }
                            /*
                            mMyAdapter.notifyItemChanged(position);
                            if (getActivity().findViewById(R.id.radioButtonClass).isSelected()) {
                                for (int i = 1; i < mNewsList.size(); i++) {
                                    for (int j = 0; j < mNewsList.size() - i; j++) {
                                        if (mNewsList.get(j).compareClass(mNewsList.get(j + 1)) > 0) {
                                            Assignments temp = mNewsList.get(j);
                                            mNewsList.set(j, mNewsList.get(j + 1));
                                            mNewsList.set(j + 1, temp);
                                        }
                                    }

                                }
                                for (int i = 0; i < mNewsList.size(); i++) {
                                    mMyAdapter.notifyItemChanged(i);
                                }
                            }
                            if (getActivity().findViewById(R.id.radioButtonDate).isSelected()) {
                                for (int i = 1; i < mNewsList.size(); i++) {
                                    for (int j = 0; j < mNewsList.size() - i; j++) {
                                        if (mNewsList.get(j).compareDate(mNewsList.get(j + 1))) {
                                            Assignments temp = mNewsList.get(j);
                                            mNewsList.set(j, mNewsList.get(j + 1));
                                            mNewsList.set(j + 1, temp);
                                        }
                                    }

                                }
                                for (int i = 0; i < mNewsList.size(); i++) {
                                    mMyAdapter.notifyItemChanged(i);
                                }
                            }
                            */
                            mNewsList.sort(new Comparator<Todos>() {
                                @Override
                                public int compare(Todos o1, Todos o2) {
                                    return o1.compareTo(o2);
                                }
                            });
                            for (int i = 0; i < mNewsList.size(); i++) {
                                mMyAdapter.notifyItemChanged(i);
                            }
                        }
                    });
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.tech_gold));
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.tech_gold));
                }
            });
        }
        @Override
        public int getItemCount() {
            return mNewsList.size();
        }

        private boolean[] getDaysArr(CheckBox m, CheckBox t, CheckBox w, CheckBox r, CheckBox f) {
            return new boolean[]{m.isChecked(), t.isChecked(), w.isChecked(), r.isChecked(), f.isChecked()};
        }
    }
    class MyViewHoder extends RecyclerView.ViewHolder {
        TextView mTitleTv;
        TextView mTitleContent;

        TextView m2;
        TextView m3;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.textViewC);
            mTitleContent = itemView.findViewById(R.id.textViewC2);

            m2 = itemView.findViewById(R.id.textViewC3);
            m3 = itemView.findViewById(R.id.textViewC4);
        }
    }

    private void setTypefaceToTimePicker(TimePicker timePicker, Typeface typeface) {
        setViewGroupTypeface(timePicker, typeface);
    }
    private void setTypefaceToTimePicker(DatePicker timePicker, Typeface typeface) {
        setViewGroupTypeface(timePicker, typeface);
    }

    private void setViewGroupTypeface(ViewGroup viewGroup, Typeface typeface) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(typeface);
            } else if (view instanceof ViewGroup) {
                setViewGroupTypeface((ViewGroup) view, typeface);
            } else  if (view instanceof NumberPicker) {
                setNumberPickerDividerColor((NumberPicker) view, getResources().getColor(R.color.tech_gold));
            }
        }
    }
    private void setNumberPickerDividerColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                Field dividerField = NumberPicker.class.getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(color);
                dividerField.set(numberPicker, colorDrawable);
                numberPicker.invalidate();
            } catch (NoSuchFieldException | IllegalAccessException e) {

            }
        }
    }

}
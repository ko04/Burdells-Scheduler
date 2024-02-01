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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class is written as a foundation and template for other fragments.
 * My classes implements a recycler view to keep track of classes.
 * This class also utilizes animation, itemtouchhelper, alert dialogs, etc. to simulate actions.
 * When view is destroyed, data will be saved.
 * @author Tianyi Yu
 * @version 1.0
 */
public class myclasses extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter ;
    private List<Classes> mNewsList;

    public myclasses() {
        // Required empty public constructo
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
            mNewsList = (List<Classes>) savedInstanceState.getSerializable("MyClassesList");
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("MyClassesList", (Serializable) mNewsList);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getActivity().findViewById(R.id.recyclerview);
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
                Classes deletedCourse = mNewsList.get(viewHolder.getAdapterPosition());
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                TextView textView = new TextView(getContext());
                textView.setText("Are you sure you want to delete course: " + deletedCourse.getClassName());
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
                alertDialog.setView(linearLayout);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                        mMyAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mNewsList.remove(viewHolder.getAdapterPosition());
                        mMyAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                });
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.tech_gold));

            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton more = getActivity().findViewById(R.id.addButton);
        more.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
        more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.NavigationView).create();
                TextView textView = new TextView(getContext());
                textView.setText("Add your class");
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
                textView.setTypeface(face);
                textView.setTextSize(32);
                textView.setPadding(64, 64, 0, 32);
                textView.setTextColor(getResources().getColor(R.color.tech_gold));
                alertDialog.setCustomTitle(textView);
                alertDialog.setMessage("Please enter your class name, instructor, time and location below.");
                LinearLayout linearLayout = new LinearLayout(getContext());
                EditText name = createEditText("Class Name");
                EditText instructor = createEditText("Instructor");
                //old ways to do the time input
//              EditText time = createEditText("Meeting Times");

                LayoutInflater inflater = LayoutInflater.from(getContext());
                View timePickerView = inflater.inflate(R.layout.time_picker, null);
                TimePicker timePicker = timePickerView.findViewById(R.id.timePicker);
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(12);
                timePicker.setCurrentMinute(00);
                setTypefaceToTimePicker(timePicker, face);


                //creating the checkBox
                CheckBox mCheckBox = createCheckBox("M");
                CheckBox tCheckBox = createCheckBox("T");
                CheckBox wCheckBox = createCheckBox("W");
                CheckBox rCheckBox = createCheckBox("R");
                CheckBox fCheckBox = createCheckBox("F");
                mCheckBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                tCheckBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                wCheckBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                rCheckBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                fCheckBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                mCheckBox.setTypeface(face);
                tCheckBox.setTypeface(face);
                wCheckBox.setTypeface(face);
                rCheckBox.setTypeface(face);
                fCheckBox.setTypeface(face);
                LinearLayout checkBoxLayout = new LinearLayout(getContext());
                checkBoxLayout.setOrientation(LinearLayout.HORIZONTAL);
                checkBoxLayout.addView(mCheckBox);
                checkBoxLayout.addView(tCheckBox);
                checkBoxLayout.addView(wCheckBox);
                checkBoxLayout.addView(rCheckBox);
                checkBoxLayout.addView(fCheckBox);

                EditText location = createEditText("Location");
                linearLayout.addView(name);
                linearLayout.addView(instructor);
                linearLayout.addView(checkBoxLayout);
                linearLayout.addView(timePicker);
//                linearLayout.addView(time);
                linearLayout.addView(location);
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
                        String day = getDays(mCheckBox) + getDays(tCheckBox) + getDays(wCheckBox) + getDays(rCheckBox) + getDays(fCheckBox);
                        mNewsList.add(new Classes(instructor.getText().toString(), location.getText().toString(), day, String.format("%02d",timePicker.getHour()) + ":" + String.format("%02d",timePicker.getMinute()), name.getText().toString()));
                        mMyAdapter.notifyItemInserted(mMyAdapter.getItemCount());
                    }
                });
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.tech_gold));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.tech_gold));

            }
        });
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
        return inflater.inflate(R.layout.fragment_myclasses, container, false);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {
        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //View view = View.inflate(getActivity(), R.layout.item_mylist, null);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mylist,parent, false);
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
        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            Classes news = mNewsList.get(position);
            holder.mTitleTv.setText("Class Name: "+news.getClassName());
            holder.mTitleContent.setText("Instructor: "+news.getInstructor());
            boolean[] day = news.getDay();
            for (int i = 0; i < 5; i++) {
                if(day[i]) {
                    holder.days[i].setBackgroundResource(R.color.tech_gold);
                    continue;
                }
                holder.days[i].setBackgroundResource(R.color.white);
            }
            holder.m2.setText("Meeting times: " + news.getTime());
            holder.m3.setText("Location: " + news.getLocation());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.NavigationView).create(); //Read Update
                    TextView textView = new TextView(getContext());
                    textView.setText("Edit your class");
                    textView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf"));
                    textView.setTextSize(32);
                    textView.setPadding(64, 64, 0, 32);
                    textView.setTextColor(getResources().getColor(R.color.tech_gold));
                    alertDialog.setCustomTitle(textView);
                    alertDialog.setMessage("Please edit your class name, instructor, time and location below. Fields that haven't been overwritten will remain the way it was.");
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    EditText name = createEditText(news.getClassName());
                    EditText instructor = createEditText(news.getInstructor());
//                    EditText time = createEditText(news.getTime());

                    //creating the checkBox
                    CheckBox mCheckBox = createCheckBox("M");
                    CheckBox tCheckBox = createCheckBox("T");
                    CheckBox wCheckBox = createCheckBox("W");
                    CheckBox rCheckBox = createCheckBox("R");
                    CheckBox fCheckBox = createCheckBox("F");
                    mCheckBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                    tCheckBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                    wCheckBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                    rCheckBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                    fCheckBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.tech_gold)));
                    LinearLayout checkBoxLayout = new LinearLayout(getContext());
                    checkBoxLayout.setOrientation(LinearLayout.HORIZONTAL);
                    checkBoxLayout.addView(mCheckBox);
                    checkBoxLayout.addView(tCheckBox);
                    checkBoxLayout.addView(wCheckBox);
                    checkBoxLayout.addView(rCheckBox);
                    checkBoxLayout.addView(fCheckBox);
                    Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
                    mCheckBox.setTypeface(face);
                    tCheckBox.setTypeface(face);
                    wCheckBox.setTypeface(face);
                    rCheckBox.setTypeface(face);
                    fCheckBox.setTypeface(face);
                    if (news.getDay()[0]) mCheckBox.setChecked(true);
                    if (news.getDay()[1]) tCheckBox.setChecked(true);
                    if (news.getDay()[2]) wCheckBox.setChecked(true);
                    if (news.getDay()[3]) rCheckBox.setChecked(true);
                    if (news.getDay()[4]) fCheckBox.setChecked(true);

                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View timePickerView = inflater.inflate(R.layout.time_picker, null);
                    TimePicker timePicker = timePickerView.findViewById(R.id.timePicker);
                    timePicker.setIs24HourView(true);
                    timePicker.setCurrentHour(Integer.parseInt(news.getTime().substring(0,2)));
                    timePicker.setCurrentMinute(Integer.parseInt(news.getTime().substring(3,5)));
                    setTypefaceToTimePicker(timePicker, face);
                    EditText location = createEditText(news.getLocation());
                    linearLayout.addView(name);
                    linearLayout.addView(instructor);
                    linearLayout.addView(checkBoxLayout);
                    linearLayout.addView(timePicker);
                    linearLayout.addView(location);
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
                            // do nothing
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (!name.getText().toString().isEmpty()) ((Classes) mNewsList.get(position)).setClassName(name.getText().toString());
                            if (!instructor.getText().toString().isEmpty())((Classes) mNewsList.get(position)).setInstructor(instructor.getText().toString());
                            ((Classes) mNewsList.get(position)).setTime(String.format(Locale.getDefault(), "%02d:%02d", timePicker.getHour(), timePicker.getMinute()));
                            boolean[] daysArray = {
                                    mCheckBox.isChecked(),
                                    tCheckBox.isChecked(),
                                    wCheckBox.isChecked(),
                                    rCheckBox.isChecked(),
                                    fCheckBox.isChecked()
                            };

                            ((Classes) mNewsList.get(position)).setDay(daysArray);


                            if (!location.getText().toString().isEmpty())((Classes) mNewsList.get(position)).setLocation(location.getText().toString());
                            mMyAdapter.notifyItemChanged(position);
                        }
                    });
                    alertDialog.show();  //<-- See This!
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
        TextView[] days = new TextView[5];
        TextView m2;
        TextView m3;
        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.textView);
            mTitleContent = itemView.findViewById(R.id.textView2);
            days[0] = itemView.findViewById(R.id.textViewMonday);
            days[1] = itemView.findViewById(R.id.textViewTuesday);
            days[2] = itemView.findViewById(R.id.textViewWednesday);
            days[3] = itemView.findViewById(R.id.textViewThursday);
            days[4] = itemView.findViewById(R.id.textViewFriday);
            m2 = itemView.findViewById(R.id.textView3);
            m3 = itemView.findViewById(R.id.textView4);
        }
    }

    private void setTypefaceToTimePicker(TimePicker timePicker, Typeface typeface) {
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
package com.example.styleimageview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.chengdazhi.styleimageview.StyleImageView;
import it.chengdazhi.styleimageview.Styler;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private List<Integer> options;
    private List<String> optionTexts;

    private StyleImageView image;
    private View lastChosenOptionView;
    private CheckBox enableAnimationCheckBox;
    private EditText animationDurationEditText;
    private SeekBar brightnessBar;
    private SeekBar contrastBar;
    private SeekBar saturationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (StyleImageView) findViewById(R.id.image);
        listView = (ListView) findViewById(R.id.list);
        initOptions();
        listView.setAdapter(new ListAdapter());

        enableAnimationCheckBox = (CheckBox) findViewById(R.id.animation_checkbox);
        animationDurationEditText = (EditText) findViewById(R.id.duration_edittext);
        enableAnimationCheckBox.setChecked(image.isAnimationEnabled());
        animationDurationEditText.setText(String.valueOf(image.getAnimationDuration()));
        if (image.isAnimationEnabled()) {
            animationDurationEditText.setEnabled(true);
            animationDurationEditText.setTextColor(Color.BLACK);
        } else {
            animationDurationEditText.setEnabled(false);
            animationDurationEditText.setTextColor(Color.GRAY);
        }
        enableAnimationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    animationDurationEditText.setText("0");
                    animationDurationEditText.setEnabled(false);
                    animationDurationEditText.setTextColor(Color.GRAY);
                } else {
                    animationDurationEditText.setEnabled(true);
                    animationDurationEditText.setTextColor(Color.BLACK);
                }
                if (b) {
                    image.enableAnimation(Long.parseLong(animationDurationEditText.getText().toString()));
                } else {
                    image.disableAnimation();
                }
            }
        });
        animationDurationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    image.enableAnimation(Long.parseLong(charSequence.toString()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        brightnessBar = (SeekBar) findViewById(R.id.seekbar_brightness);
        contrastBar = (SeekBar) findViewById(R.id.seekbar_contrast);
        brightnessBar.setProgress(image.getBrightness() + 255);
        contrastBar.setProgress((int) (image.getContrast() * 100));
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                image.setBrightness(i - 255).updateStyle();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        contrastBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                image.setContrast(i / 100F).updateStyle();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void initOptions() {
        options = new ArrayList<>();
        optionTexts = new ArrayList<>();
        options.add(Styler.Mode.GREY_SCALE);
        optionTexts.add("Grey Scale");
        options.add(Styler.Mode.INVERT);
        optionTexts.add("Invert");
        options.add(Styler.Mode.RGB_TO_BGR);
        optionTexts.add("RGB to BGR");
        options.add(Styler.Mode.SEPIA);
        optionTexts.add("Sepia");
        options.add(Styler.Mode.BLACK_AND_WHITE);
        optionTexts.add("Black & White");
        options.add(Styler.Mode.BRIGHT);
        optionTexts.add("Bright");
        options.add(Styler.Mode.VINTAGE_PINHOLE);
        optionTexts.add("Vintage Pinhole");
        options.add(Styler.Mode.KODACHROME);
        optionTexts.add("Kodachrome");
        options.add(Styler.Mode.TECHNICOLOR);
        optionTexts.add("Technicolor");
        options.add(Styler.Mode.SATURATION);
        optionTexts.add("Saturation");
    }

    class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return options.size() + 1;
        }

        @Override
        public Object getItem(int i) {
            if (i >= options.size()) {
                return 100;
            }
            return options.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final View result = getLayoutInflater().inflate(R.layout.option_item, null);
            if (i < options.size() && image.getMode() == options.get(i) ||
                    i >= options.size() && image.getMode() == Styler.Mode.NONE) {
                result.setBackgroundColor(Color.LTGRAY);
                lastChosenOptionView = result;
            }
            TextView title = (TextView) result.findViewById(R.id.text);
            if (i >= options.size()) {
                title.setText("Clear");
            } else {
                title.setText(optionTexts.get(i));
                if (options.get(i) == Styler.Mode.SATURATION) {
                    saturationBar = (SeekBar) result.findViewById(R.id.seekbar_saturation);
                    saturationBar.setVisibility(View.VISIBLE);
                    saturationBar.setProgress((int) (image.getSaturation() * 100));
                    saturationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            image.setSaturation(i / 100F).updateStyle();
                            if (lastChosenOptionView != result) {
                                if (lastChosenOptionView != null) {
                                    lastChosenOptionView.setBackgroundColor(Color.WHITE);
                                }
                                result.setBackgroundColor(Color.LTGRAY);
                                lastChosenOptionView = result;
                            }
                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {}
                    });
                }
            }
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lastChosenOptionView != null) {
                        lastChosenOptionView.setBackgroundColor(Color.WHITE);
                    }
                    view.setBackgroundColor(Color.LTGRAY);
                    if (i >= options.size()) {
                        image.clearStyle();
                        if (saturationBar != null) {
                            saturationBar.setProgress(100);
                        }
                    } else {
                        image.setMode(options.get(i)).updateStyle();
                        if (saturationBar != null && options.get(i) != Styler.Mode.SATURATION) {
                            saturationBar.setProgress(100);
                        }
                    }
                    lastChosenOptionView = view;
                }
            });
            return result;
        }
    }
}
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.chengdazhi.styleimageview.Styler;

public class MainActivity extends AppCompatActivity {
    private ImageView image;
    private ListView list;
    private List<Integer> options;
    private List<String> optionTexts;
    private View lastActiveView;
    private CheckBox shouldAnimateCheckBox;
    private EditText animationDurationEditText;
    private SeekBar brightnessBar;
    private SeekBar contrastBar;
    private SeekBar saturationBar;
    private Styler styler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.image);
        list = (ListView) findViewById(R.id.list);
        initOptions();
        list.setAdapter(new ListAdapter());

        styler = new Styler.Builder(image, Styler.Mode.NONE).enableAnimation(500).build();

        shouldAnimateCheckBox = (CheckBox) findViewById(R.id.animation_checkbox);
        animationDurationEditText = (EditText) findViewById(R.id.duration_edittext);
        shouldAnimateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                    styler.enableAnimation(Long.parseLong(animationDurationEditText.getText().toString()));
                } else {
                    styler.disableAnimation();
                }
            }
        });
        animationDurationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    styler.enableAnimation(Long.parseLong(charSequence.toString()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        brightnessBar = (SeekBar) findViewById(R.id.seekbar_brightness);
        contrastBar = (SeekBar) findViewById(R.id.seekbar_contrast);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                styler.setBrightness(brightnessBar.getProgress() - 255).updateStyle();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        contrastBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                styler.setContrast(contrastBar.getProgress() / 100F).updateStyle();
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
            if (i < options.size() && styler.getMode() == options.get(i) ||
                    i >= options.size() && styler.getMode() == Styler.Mode.NONE) {
                result.setBackgroundColor(Color.LTGRAY);
                lastActiveView = result;
            }
            TextView title = (TextView) result.findViewById(R.id.text);
            if (i >= options.size()) {
                title.setText("Clear");
            } else {
                title.setText(optionTexts.get(i));
                if (options.get(i) == Styler.Mode.SATURATION) {
                    saturationBar = (SeekBar) result.findViewById(R.id.seekbar_saturation);
                    saturationBar.setVisibility(View.VISIBLE);
                    saturationBar.setProgress((int) (styler.getSaturation() * 100));
                    saturationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            styler.setSaturation(i / 100F).updateStyle();
                            if (lastActiveView != result) {
                                result.setBackgroundColor(Color.LTGRAY);
                                if (lastActiveView != null) {
                                    lastActiveView.setBackgroundColor(Color.WHITE);
                                }
                            }
                            lastActiveView = result;
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
                    if (lastActiveView != null) {
                        lastActiveView.setBackgroundColor(Color.WHITE);
                    }
                    view.setBackgroundColor(Color.LTGRAY);
                    if (i >= options.size()) {
                        styler.clearStyle();
                    } else {
                        styler.setMode(options.get(i)).updateStyle();
                        if (saturationBar != null) {
                            saturationBar.setProgress(100);
                        }
                    }
                    lastActiveView = view;
                }
            });
            return result;
        }
    }
}


package com.example.customspinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;


public class BaseSpinner extends AppCompatSpinner {

    private static final String TAG = BaseSpinner.class.getSimpleName();

    public OnValueChangedListener onValueChangedListener;

    private TextPaint textPaint;

    //Properties about Header
    private boolean headerVisible;
    private boolean enableHeader;

    //AttributeSet
    private CharSequence placeholder;
    private int baseColor;
    private CharSequence headerText;
    private int headerColor;
    private Typeface typeface;

    //Inner Adapters
    private CustomSpinnerAdapter customSpinnerAdapter;

    private ArrayList<GeneralModel> spinnerListAll;
    private ArrayList<GeneralModel> spinnerListSelected = new ArrayList<>();


    /*
     * **********************
     * CONSTRUCTORS
     * **********************
     */
    public BaseSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public BaseSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /*
     * *******************************
     * INITIALISATION METHODS
     * *******************************
     */
    private void init(Context context, AttributeSet attrs) {
        initAttributes(context, attrs);
        initPaintObjects();
        initOnItemSelectedListener();
    }

    private void initPaintObjects() {
        int labelTextSize = getResources().getDimensionPixelSize(R.dimen.label_text_size);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(labelTextSize);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray defaultArray = context.obtainStyledAttributes(new int[]{R.attr.colorControlNormal, R.attr.colorAccent});
        int defaultBaseColor = defaultArray.getColor(0, 0);
        defaultArray.recycle();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BaseSpinner);
        baseColor = array.getColor(R.styleable.BaseSpinner_baseColor, defaultBaseColor);
        placeholder = array.getString(R.styleable.BaseSpinner_placeholder);
        headerText = array.getString(R.styleable.BaseSpinner_headerText);
        headerColor = array.getColor(R.styleable.BaseSpinner_headerColor, baseColor);
        enableHeader = array.getBoolean(R.styleable.BaseSpinner_enableHeader, true);

        String typefacePath = array.getString(R.styleable.BaseSpinner_typeface);
        if (typefacePath != null) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), typefacePath);
        }
        array.recycle();
        headerVisible = true;
    }


    /*
     * ********************
     * Spinner Background
     * ********************
     */
    public void setSpinnerBackground(int drawableResource) {
        setBackgroundResource(drawableResource);//Can set upto 3 types of bg spinner_bg_with_underline spinner_bg_with_border spinner_bg_transparent
    }

    public OnValueChangedListener getOnValueChangedListener() {
        return onValueChangedListener;
    }

    public void setOnValueChangedListener(OnValueChangedListener listener) {
        onValueChangedListener = listener;
    }

    /*
     * ************************************
     * ON DRAW METHOD FOR CUSTOM HEADER
     * ************************************
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Header Drawing
        if ((headerText != null) && enableHeader) {
            textPaint.setColor(headerColor);
            String textToDraw = headerText.toString();
            canvas.drawText(textToDraw, textPaint.getTextSize() - 1, 35, textPaint);
        }
    }

    @Override
    public void setSelection(final int position, boolean animate) {
        this.post(() -> BaseSpinner.super.setSelection(position, animate));
    }

    @Override
    public int getSelectedItemPosition() {
        return super.getSelectedItemPosition();
    }

    private void initOnItemSelectedListener() {
        setOnItemSelectedListener(null);
    }

    public ArrayList<GeneralModel> getData() {
        int index = Constants.UNDEFINED;
        for (int i = 0; i < spinnerListAll.size(); i++) {
            GeneralModel obj = spinnerListAll.get(i);
            if (obj.getName().equals(placeholder.toString())) {
                index = spinnerListAll.indexOf(obj);
                break;
            }
        }
        if (index != Constants.UNDEFINED) {
            spinnerListAll.remove(index);
        }
        return spinnerListAll;
    }

    public void setData(ArrayList<GeneralModel> mainlist) {
        if (placeholder == null) {
            throw new AssertionError("setPlaceHolder() must be called before setData()");
        }
        spinnerListAll = new ArrayList(mainlist);
        if (spinnerListAll.stream().filter(object -> object.getName().equals(placeholder.toString())).findFirst().orElse(null) == null) {
            spinnerListAll.add(0, new GeneralModel(placeholder.toString(), placeholder.toString()));
        }
        customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), spinnerListAll);
        setAdapter(customSpinnerAdapter);
    }
    public void showheader() {
        headerVisible = true;
    }

    public void hideheader() {
        headerVisible = false;
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerListSelected.clear();
                spinnerListSelected.add(new GeneralModel(spinnerListAll.get(position).id, spinnerListAll.get(position).name));
                if (spinnerListSelected.stream().filter(object -> object.getName().equals(placeholder.toString())).findFirst().orElse(null) != null) {
//                    spinnerListSelected.clear();
                    setSelectedData(null);
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                    onValueChangedListener.onValueChanged();
                    return;
                }
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                onValueChangedListener.onValueChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (listener != null) {
                    listener.onNothingSelected(parent);
                }
            }
        };
        super.setOnItemSelectedListener(onItemSelectedListener);
    }

    /*
     * ***********************
     * GETTERS AND SETTERS
     * ***********************
     */
    public int getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
        invalidate();
    }

    public ArrayList<GeneralModel> getSelectedData() {
        int index = Constants.UNDEFINED;
        for (int i = 0; i < spinnerListSelected.size(); i++) {
            GeneralModel obj = spinnerListSelected.get(i);
            if (obj.getName().equals(placeholder.toString())) {
                index = spinnerListSelected.indexOf(obj);
                break;
            }
        }
        if (index != Constants.UNDEFINED) {
            spinnerListSelected.remove(index);
        }
        return spinnerListSelected;
    }

    public void setSelectedData(ArrayList<GeneralModel> list) {
        if (list == null) {
            setSelection(Constants.UNDEFINED, true);
            return;
        }
        if (spinnerListAll == null) {
            setSelection(0, true);
            return;
        }
        this.spinnerListSelected = new ArrayList(list);
        if (list.size() > 0 && spinnerListAll.size() > 0) {
            int position = Constants.UNDEFINED;
            for (int i = 0; i < spinnerListAll.size(); i++) {
                if (list.get(0).id.equals(spinnerListAll.get(i).id)) {
                    position = i;
                    break;
                }
            }
            if (position != Constants.UNDEFINED) {
                setSelection(position, true);
            } else {
                selectPlaceholder();
            }
        }
    }

    public void selectPlaceholder() {
        setSelection(Constants.UNDEFINED, true);
    }

    public CustomSpinnerAdapter getCustomSpinnerAdapter() {
        return customSpinnerAdapter;
    }

    public void setCustomSpinnerAdapter(CustomSpinnerAdapter customSpinnerAdapter) {
        this.customSpinnerAdapter = customSpinnerAdapter;
    }

    public CharSequence getHeaderText() {
        return this.headerText;
    }

    public void setHeaderText(CharSequence headerText) {
        this.headerText = headerText;
        invalidate();
    }

    public void setHeaderText(int resid) {
        String headerText = getResources().getString(resid);
        setHeaderText(headerText);
    }

    public int getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(int headerColor) {
        this.headerColor = headerColor;
        invalidate();
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        invalidate();
    }

    public boolean isEnableHeader() {
        return enableHeader;
    }

    public void setEnableHeader(boolean enableHeader) {
        this.enableHeader = enableHeader;
        invalidate();
    }

    public CharSequence getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(CharSequence placeholder) {
        this.placeholder = placeholder;
        if (spinnerListAll != null) {
            throw new AssertionError("setData() must be called after setPlaceHolder()");
        }
        invalidate();
    }

    public void setPlaceholder(int resid) {
        CharSequence placeholder = getResources().getString(resid);
        if (spinnerListAll != null) {
            throw new AssertionError("setData() must be called after setPlaceHolder()");
        }
        setPlaceholder(placeholder);
    }

    /*
     * ************************
     * CALL BACK METHOD
     * ************************
     */
    public interface OnValueChangedListener {
        public void onValueChanged();
    }

    /*
     * **************
     * INNER CLASS
     * **************
     */
    public class CustomSpinnerAdapter extends BaseAdapter {
        private final Context context;
        private final ArrayList<GeneralModel> values;

        public CustomSpinnerAdapter(Context context, ArrayList<GeneralModel> values) {
            this.context = context;
            this.values = values;
        }

        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            return values.get(position);
        }


        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text = new TextView(context);
            text.setTextColor(Color.BLACK);
            text.setTextSize(18);
            text.setText(values.get(position).getName()
                    .toString());
            text.setPadding(15, 5, 15, 5);
            return text;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView label = new TextView(context);
            label.setTextColor(Color.GRAY);
            label.setTextSize(18);
            label.setText(values.get(position).getName());
            label.setPadding(15, 5, 15, 5);
            return label;
        }
    }
}

# BaseSpinner_Android
A simple and easy to use, highly customizable Spinner for Android Developers, written in Java.

### Include the spinner object in XML
```xml
<com.example.customspinner.BaseSpinner 
    android:id="@+id/spinner_country"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="center_vertical"
    android:layout_margin="5dp"
    app:headerColor="#000000"
    app:baseColor="@color/black"
    app:headerText="Select your site"
    app:placeholder="Select"
    app:enableHeader="true"> 
</com.example.customspinner.BaseSpinner>
```

### Declare Properties
```android
    ArrayList<GeneralModel> listCountry;
    BaseSpinner spinnerCountry;
```

### Load the spinner object in Java class
```android
    listCountry = new ArrayList<GeneralModel>();
    listCountry.add(new GeneralModel("1231132","United States of America"));
    listCountry.add(new GeneralModel("4451132","United Kingdom"));

    spinnerCountry =  findViewById(R.id.spinner_country);
    spinnerCountry.setPlaceholder("Select");
    spinnerCountry.setData(listCountry);
    spinnerCountry.setSpinnerBackground(R.drawable.spinner_bg_transparent);
    spinnerCountry.setOnValueChangedListener(new BaseSpinner.OnValueChangedListener() {
        @Override
        public void onValueChanged() {
            if(spinnerCountry.getSelectedData().size() > 0) {
                if (spinnerCountry.getSelectedData().get(0).getName().equalsIgnoreCase("United States of America")) {
                    //Do something
                } else if (spinnerCountry.getSelectedData().get(0).getName().equalsIgnoreCase("United Kingdom")) {
                    //Do something
                } 
            }
        }
    });
```
*listCountry is the meta data to show on spinnerCountry*
*spinnerCountry.setSpinnerBackground(R.drawable.spinner_bg_transparent); We have 3 kinds of background options*

### Validations very simple
```android
    btnSubmit.setOnClickListener(view -> {
        if (spinnerCountry.getSelectedData().size() <= 0) {
            showSingleToast("Please select a country");
        } else {
            exit();
        }
    });
```

### Unselect
```android
    btnSubmit.selectPlaceholder();
```
*This will also invoke onValueChanged method*

### Image
#### focused
![Focused](https://github.com/xeieshan/BaseSpinner_Android/raw/main/Spinner%20focused.png)
#### not focused
![Not Focused](https://github.com/xeieshan/BaseSpinner_Android/raw/main/Spinner%20not%20focused.png)
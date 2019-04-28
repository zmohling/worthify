package team_10.client.utility.factory;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Calendar;

import team_10.client.MainActivity;
import team_10.client.R;
import team_10.client.data.UserInputField;

public abstract class InputFieldFactory {
    private static LayoutInflater inflater;
    private static Context context;

    public static View getInputFieldView(final Object account, final Field field) {
        context = MainActivity.myContext;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        UserInputField userInputField = field.getAnnotation(UserInputField.class);

        View v = null;

        Class type = userInputField.inputType();

        try {
            if (String.class.isAssignableFrom(type)) {

                v = getStringInputFieldView(account, field, userInputField);

            } else if (Number.class.isAssignableFrom(type)) { // Other Numbers

                v = getNumberInputFieldView(account, field, userInputField);

            } else if (LocalDate.class.isAssignableFrom(type)) {

                v = getDateInputFieldView(account, field, userInputField);

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return v;
    }

    private static View getStringInputFieldView(final Object account, final Field field,
                                                final UserInputField userInputField) throws IllegalAccessException {
        View v = inflater.inflate(R.layout.item_string_input_view, null);

        // Title text
        final TextView textView = (TextView) v.findViewById(R.id.item_string_input_view_TITLE);
        textView.setText(userInputField.name() + ":");
        // Hint text
        final EditText editText = (EditText) v.findViewById(R.id.item_string_input_view_INPUT);
        editText.setHint((field.get(account) == null) ? "Click to Edit" : field.get(account).toString());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {

                    field.set(account, s.toString());

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    private static View getNumberInputFieldView(final Object account, final Field field,
                                                final UserInputField userInputField) throws IllegalAccessException {
        View v = inflater.inflate(R.layout.item_string_input_view, null);

//        if (inputField.type.isInstance(BigDecimal.class)) {
//
//        }

        // Title text
        final TextView textView = (TextView) v.findViewById(R.id.item_string_input_view_TITLE);
        textView.setText(userInputField.name() + ":");
        // Hint text
        final EditText editText = (EditText) v.findViewById(R.id.item_string_input_view_INPUT);
        editText.setHint((field.get(account) == null) ? "Click to Edit" : field.get(account).toString());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {

                    field.set(account, Double.parseDouble(s.toString()));

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    private static View getDateInputFieldView(final Object account, final Field field,
                                              final UserInputField userInputField) throws IllegalAccessException {
        View v = inflater.inflate(R.layout.item_date_input_view, null);

        // Title text
        final TextView textView = (TextView) v.findViewById(R.id.item_date_input_view_TITLE);
        textView.setText(userInputField.name() + ":");
        // Hint text
        final EditText editText = (EditText) v.findViewById(R.id.item_date_input_view_INPUT);
        editText.setHint((field.get(account) == null) ? "Click to Edit" : field.get(account).toString());

        // Date Picker
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                try {

                    field.set(account, LocalDate.of(year, monthOfYear, dayOfMonth));
                    editText.setHint(field.get(account).toString());

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        };

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return v;
    }
}

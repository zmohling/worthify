package team_10.client.utility;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import team_10.client.R;
import team_10.client.data.models.Account;
import team_10.client.object.account.UserInputField;

public abstract class InputFieldFactory {
    private static LayoutInflater inflater;
    private static Context context;

    private static Map<Field, UserInputField> fields;
    private static Account a_temp;

    public static View getInputFieldView(final Account account, Context _context) {
        context = _context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout v = new LinearLayout(context);
        v.setOrientation(LinearLayout.VERTICAL);

        a_temp = account;
        fields = new TreeMap<>(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.getAnnotation(UserInputField.class).priority() -
                        o2.getAnnotation(UserInputField.class).priority();
            }
        });

        for (Field field : account.getClass().getFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(UserInputField.class)) {
                fields.put(field, field.getAnnotation(UserInputField.class));
            }
        }

        for (Field field : fields.keySet()) {
                Class type = fields.get(field).inputType();
                if (String.class.isAssignableFrom(type)) {
                    v.addView(getStringInputFieldView(field));
                } else if (Number.class.isAssignableFrom(type)) { // Other Numbers
                    v.addView(getNumberInputFieldView(field));
                } else if (LocalDate.class.isAssignableFrom(type)) {
                    v.addView(getDateInputFieldView(field));
                }
        }


        return v;
    }

    private static View getStringInputFieldView(Field inputField) {
        View v = inflater.inflate(R.layout.item_string_input_view, null);

//        i_temp = inputField;

        // Title text
        final TextView textView = (TextView) v.findViewById(R.id.item_string_input_view_TITLE);
        textView.setText(fields.get(inputField).name() + ":");
        // Hint text
        final EditText editText = (EditText) v.findViewById(R.id.item_string_input_view_INPUT);
        //editText.setHint((inputField.ref == null) ? "Click to Edit" : inputField.ref.toString());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    inputField.set(a_temp, s.toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return v;
    }

    private static View getNumberInputFieldView(Field inputField) {
        View v = inflater.inflate(R.layout.item_string_input_view, null);

//        i_temp = inputField;

//        if (inputField.type.isInstance(BigDecimal.class)) {
//
//        }

        // Title text
        final TextView textView = (TextView) v.findViewById(R.id.item_string_input_view_TITLE);
        textView.setText(fields.get(inputField).name() + ":");
        // Hint text
        final EditText editText = (EditText) v.findViewById(R.id.item_string_input_view_INPUT);
//        editText.setHint((inputField.ref == null) ? "Click to Edit" : inputField.ref.toString());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                i_temp.ref = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return v;
    }

    private static View getDateInputFieldView(Field inputField) {
        View v = inflater.inflate(R.layout.item_date_input_view, null);

//        i_temp = inputField;

        // Title text
        final TextView textView = (TextView) v.findViewById(R.id.item_date_input_view_TITLE);
        textView.setText(fields.get(inputField).name() + ":");
        // Hint text
        final EditText editText = (EditText) v.findViewById(R.id.item_date_input_view_INPUT);
//        editText.setHint((inputField.ref == null) ? "Click to Edit" : inputField.ref.toString());

        // Date Picker
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
//                i_temp.ref = LocalDate.of(year, monthOfYear, dayOfMonth);
//                editText.setHint(i_temp.ref.toString());
            }

        };

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return v;
    }
}

package com.kkgame.sdk.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class PhoneNumTextWatcher implements TextWatcher {

    private EditText numberEditText;

    int beforeLen = 0;

    int afterLen = 0;

    public PhoneNumTextWatcher(EditText numberEditText) {
        this.numberEditText = numberEditText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        beforeLen = s.length();
    }

    @Override
    public void afterTextChanged(Editable s) {
        String txt = numberEditText.getText().toString();
        afterLen = txt.length();

        if (afterLen > beforeLen) {
            if (txt.length() == 4 || txt.length() == 9) {
                numberEditText.setText(new StringBuffer(txt).insert(
                        txt.length() - 1, " ").toString());
                numberEditText.setSelection(numberEditText.getText().length());
            }
        } else {
            if (txt.startsWith(" ")) {
                numberEditText.setText(new StringBuffer(txt).delete(
                        afterLen - 1, afterLen).toString());
                numberEditText.setSelection(numberEditText.getText().length());

            }
        }

    }

}

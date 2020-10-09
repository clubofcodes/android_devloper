package validations;

import android.text.TextUtils;

public class Validation {
    public boolean empty(String blank){

        if(blank.length()==0){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isValidPhone(CharSequence phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }
}

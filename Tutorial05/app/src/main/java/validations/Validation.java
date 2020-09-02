package validations;

public class Validation {
    public boolean empty(String blank){

        if(blank.length()==0){
            return true;
        }
        else {
            return false;
        }
    }
}

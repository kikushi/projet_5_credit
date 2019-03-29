package proj.myapplication;

public class PINConfig {
    Boolean isByte;
    Boolean isInput;
    int pinNumbers[] = new int[8];
    int pinNumber;
    Character ID;
    String text;

    public PINConfig(boolean _isByte, Boolean _isInput, int _pinNumber, char _ID, String _text){
        isByte = _isByte;
        isInput = _isInput;
        pinNumber = _pinNumber;
        text =_text;
    }
    public Boolean getIsByte(){
        if(isByte){
            return true;
        }
        return false;
    }
    public Boolean getIsInput(){
        if(isInput){
            return true;
        }
        return false;

    }
    public int getPinNumbers( int i){
        return pinNumbers[i];
    }
    public int getPinNumber(){
        return pinNumber;
    }
    public char getId(){
        return ID;
    }
    public String getText(){
        return text;
    }
}

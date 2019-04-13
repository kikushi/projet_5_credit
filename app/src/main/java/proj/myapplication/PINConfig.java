package proj.myapplication;

public class PINConfig {
    private boolean isByte;
    private boolean isInput;
    private char ID;
    private String text;
    private String subText;
    private int pinNumber;
    private int[] pinNumbers;

    public PINConfig(boolean _isByte, boolean _isInput, char _ID, String _text){
        isByte = _isByte;
        isInput = _isInput;
        ID = _ID;
        text =_text;
        if (isInput) {
            subText = "Input";
        }
        else {
            subText = "Output";
        }
    }

    public boolean getIsByte(){
        return isByte;
    }
    public boolean getIsInput(){
        return isInput;
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
    public String getSubText(){
        return subText;
    }

    public void setPinNumber(int _pinNumber) {
        pinNumber = _pinNumber;
    }
    public void setPinNumbers(int[] _pinNumbers) {
        pinNumbers = _pinNumbers;
    }
}

package web.english.application.utils;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static final String fullNameRequire="Họ tên tối đa 255 kí tự, không được chứa kí tự số hoặc kí tự đặc biệt";
    public static final String usernameRequire="Username tối thiểu 6, tối đa 255 kí tự và không chứa kí tự khoảng trắng";
    public static final String phoneNumberRequire="Số điện thoại gồm 10 chữ số, bắt đầu bằng số 0";
    public static final String emailRequire="Email tối đa 255 kí tự, chỉ được sử dụng chữ cái (a-z), số (0-9) và kí tự (.)";
    public static final String yearRequire="Năm sinh phải trong khoảng: "+(LocalDate.now().getYear()-100)+" <= năm sinh <= "+(LocalDate.now().getYear()-18);
    public static final String maxLengthRequire="Tối đa 255 kí tự";

    public static final int exam = 27;
    public static final int maxLength = 255;

    /**
     *{@value #phoneNumberRequire}
     * @author VQKHANH
     * @param phoneNumber
     * @return true if phoneNumber was matched require, else return false
     */
    public boolean checkPhoneNumberFormat(String phoneNumber){
        String reg="^0[1-9]{1}[0-9]{8}$";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * {@value #usernameRequire}
     * @author VQKHANH
     * @param username
     * @return true if username was matched require, else return false
     */
    public boolean checkUsernameFormat(String username){
        String reg="^[\\w]{6,255}$";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(username);
        return matcher.matches();
    }

    /**
     * {@value #fullNameRequire}
     * @author VQKHANH
     * @param fullName
     * @return true if fullName was matched require, else return false (allow Vietnamese)
     */
    public boolean checkFullNameFormat(String fullName){
//        String reg="^([a-zA-Z]+\\s?)+$";
        String reg="^([a-záàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệóòỏõọôốồổỗộơớờởỡợíìỉĩịúùủũụưứừửữựýỳỷỹỵđ]+\\s?)$";
        Pattern pattern=Pattern.compile(reg,Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher=pattern.matcher(fullName.toLowerCase());
        return matcher.matches();
    }

    /**
     * {@value #emailRequire}
     * @author VQKHANH
     * @param email
     * @return true if email was matched require, else return false
     */
    public boolean checkEmailFormat(String email){
        String reg="^[\\w\\.]+@([\\w]+\\.)+[\\w]{2,4}$";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }

    /**
     *
     * @author VQKHANH
     * @param dob
     * @return true if dob was matched require, else return false
     */
    public boolean checkDob(LocalDate dob){
        if(dob==null) return false;
        if(dob.getYear()<1900||dob.getYear()>(LocalDate.now().getYear())-18) return false;
        return true;
    }


    public int checkPoint(List<String> listCheck, List<String> listAnswer){
        int point = 0;
        for (int i = 0; i < listCheck.size(); i++) {
            if(listCheck.get(i).equals(listAnswer.get(i))) point++;
        }
        return point;
    }

    public boolean checkMaxLength(String text){
        if (text.length()>maxLength)return  false;
        else return true;
    }

    public String extractMessageFromException(String message){
        String msg = message.substring(message.indexOf("\"message\":")+11,message.lastIndexOf(",\"timeStamp\"")-1);

        return msg;
    }
}

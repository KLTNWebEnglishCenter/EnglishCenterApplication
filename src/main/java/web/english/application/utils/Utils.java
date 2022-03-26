package web.english.application.utils;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static final String fullNameRequire="Họ tên không được chứa ký tự số hoặc ký tự đặc biệt";
    public static final String usernameRequire="Username tối thiểu 6 ký tự và không chứa ký tự khoảng trắng";
    public static final String phoneNumberRequire="Số điện thoại gồm 10 chữ số, bắt đầu bằng số 0";
    public static final String emailRequire="Email chỉ được sử dụng chữ cái (a-z), số (0-9) và ký tự (.)";
    public static  final String yearRequire="Năm sinh phải trong khoảng: "+(LocalDate.now().getYear()-100)+" <= năm sinh <= "+(LocalDate.now().getYear()-18);

    public boolean checkPhoneNumberFormat(String phoneNumber){
        String reg="^0[1-9]{1}[0-9]{8}$";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public boolean checkUsernameFormat(String username){
        String reg="^[\\w]{6,}$";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(username);
        return matcher.matches();
    }

    public boolean checkFullNameFormat(String fullName){
        String reg="^([a-zA-Z]+\\s?)+$";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(fullName);
        return matcher.matches();
    }

    public boolean checkEmailFormat(String email){
        String reg="^[\\w\\.]+@([\\w]+\\.)+[\\w]{2,4}$";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }

    public boolean checkDob(LocalDate dob){
        if(dob.getYear()<1900||dob.getYear()>(LocalDate.now().getYear())-18) return false;
        return true;
    }
}

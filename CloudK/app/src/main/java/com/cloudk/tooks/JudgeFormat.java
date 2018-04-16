package com.cloudk.tooks;

import android.widget.EditText;
import android.widget.TextView;

import com.cloudk.algo.Str;


/**
 * Created by dong on 2018/2/1.
 */

public class JudgeFormat {
    private static TextView error_info;
    public JudgeFormat(TextView error_info)
    {
        this.error_info = error_info;
    }
    public JudgeFormat(){}
    //该方法用于判断用户名或密码输入是否正确
    public static boolean isUserandPassRight(EditText userName, EditText password)
    {
        if(!"".equals(userName.getText().toString())
                && !Str.isRightFormat(userName.getText().toString())
                && !"".equals(password.getText().toString())
                && !Str.isRightFormat(password.getText().toString())){
            error_info.setText("用户名和密码输入格式有误");
            return false;
        }else if(!"".equals(userName.getText().toString())
                && !Str.isRightFormat(userName.getText().toString())
                ){
            error_info.setText("用户名输入格式有误");
            return false;
        }else if(!"".equals(password.getText().toString())
                && !Str.isRightFormat(password.getText().toString())
                ){
            error_info.setText("密码输入格式有误");
            return false;
        }
        return true;
    }
    public static boolean isUserRight(EditText userName)
    {
        if(!"".equals(userName.getText().toString())
                && !Str.isRightFormat(userName.getText().toString())
                ){
            error_info.setText("用户名输入格式有误");
            return false;
        }
        return true;
    }
}

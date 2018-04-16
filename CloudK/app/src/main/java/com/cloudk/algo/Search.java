package com.cloudk.algo;

/**
 * Created by dong on 2018/1/31.
 */

public class Search {

    public static boolean isEquality(String editText,String data)
    {
        if(editText.length()==1 && data.length()>=editText.length())
        {
            for(int i=0;i<data.length();i++)
            {
                if(editText.charAt(0) == data.charAt(i))
                    return true;
            }
            return false;
        }

        if(editText.length()>data.length()) {
            return false;
        }else if(editText.length()==data.length()) {
            return editText.equals(data);
        }else {
            for(int i = 0;i < data.length(); i++) {
                if((data.length()-i) >= editText.length())
                {
                    if(editText.equals(data.substring(i,i+editText.length()))) {
                        return true;
                    }
                    else {
                        continue;
                    }
                }else{
                    return false;
                }
            }
        }
        return true;
    }
}

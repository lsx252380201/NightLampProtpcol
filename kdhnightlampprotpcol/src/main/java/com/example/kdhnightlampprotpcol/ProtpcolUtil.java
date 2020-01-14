package com.example.kdhnightlampprotpcol;

import android.text.TextUtils;

public class ProtpcolUtil {
    private static String write = "03";
    private static String read = "02";

    private static String CodeBreatheLightColor = "05";
    private static String CodeNightLampColor = "06";
    private static String CodeNightLampTime = "04";
    private static String CodeTempTime = "02";

    private static String CodeKeyNightLampOpen = "0301";
    private static String CodeKeyNightLampClose = "0300";
    private static String CodeKeyTempOpen = "0101";
    private static String CodeKeyTempClose = "0100";

    private static String CodeKeyNightLampRead = "0700";
    private static String CodeKeyTempRead = "0100";

    public static int Type_NightLamp = 0;
    public static int Type_Temp = 1;
    public static int Type_BreatheLamp = 2;

    private static String getData(String respond_flag,String mac_address,String set_data) {
        String result = "";

        result = respond_flag + mac_address + "0" + set_data.length() / 2 + set_data;
        return result;
    }

    private static String decimal2Hex(int decimal){
        String hex = Integer.toHexString(decimal);
        while (hex.length() % 2 != 0){
            hex = "0" + hex;
        }
        return hex;
    }

    private static int formatting(String a) {
        int i = 0;
        for (int u = 0; u < 10; u++) {
            if (a.equals(String.valueOf(u))) {
                i = u;
            }
        }
        if (a.equals("a")) {
            i = 10;
        }
        if (a.equals("b")) {
            i = 11;
        }
        if (a.equals("c")) {
            i = 12;
        }
        if (a.equals("d")) {
            i = 13;
        }
        if (a.equals("e")) {
            i = 14;
        }
        if (a.equals("f")) {
            i = 15;
        }
        return i;
    }

    private static String toD(String a, int b) {
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r = (int) (r + formatting(a.substring(i, i + 1))
                    * Math.pow(b, a.length() - i - 1));
        }
        return String.valueOf(r);
    }

    public static String SetColor(String mac,int type,int ColorR,int ColorG,int ColorB){
        String result = "";
        if(type == Type_NightLamp){
            result = getData(write,mac,CodeNightLampColor + decimal2Hex(ColorR) + decimal2Hex(ColorG) + decimal2Hex(ColorB));
        }else if(type == Type_BreatheLamp){
            result = getData(write,mac,CodeBreatheLightColor + decimal2Hex(ColorR) + decimal2Hex(ColorG) + decimal2Hex(ColorB));
        }

        return result;
    }

    public static String SetTime(String mac,int type,int time){
        String result = "";
        if(type == Type_NightLamp){
            result = getData(write,mac,CodeNightLampTime +
                    decimal2Hex(time * 30 / 60) + decimal2Hex(time * 30 % 60));
        }else if(type == Type_Temp){
            result = getData(write,mac,CodeTempTime +
                    decimal2Hex(time * 30 / 60) + decimal2Hex(time * 30 % 60));
        }

        return result;
    }

    public static String SetKey(String mac,int type,boolean key){
        String result = "";
        if(type == Type_NightLamp){
            if (key){
                result = getData(write,mac,CodeKeyNightLampOpen);
            }else {
                result = getData(write,mac,CodeKeyNightLampClose);
            }
        }else if (type == Type_Temp){
            if (key){
                result = getData(write,mac,CodeKeyTempOpen);
            }else {
                result = getData(write,mac,CodeKeyTempClose);
            }
        }

        return result;
    }

    private static int isRightRespond(String data,int location){
        int result = 0;

        if(data.length() < location + 4 || location <= 0){
            return result;
        }

        if(data.length() >= location + 4){
            switch (data.substring(location + 2, location+ 4)){
                case "01":
                    if(data.length() >= location + 6){
                        result = 1;
                    }
                    break;

                case "02":
                    if(data.length() >= location + 8){
                        result = 2;
                    }
                    break;

                case "03":
                    if(data.length() >= location + 6){
                        result = 3;
                    }
                    break;

                case "04":
                    if(data.length() >= location + 8){
                        result = 4;
                    }
                    break;

                case "05":
                    if(data.length() >= location + 10){
                        result = 5;
                    }
                    break;

                case "06":
                    if(data.length() >= location + 10){
                        result = 6;
                    }
                    break;
            }
        }

        return result;
    }

    private static int GetLocation(String data1,String code){
        int result = 0;

        if(data1.contains(code)){
            result = data1.indexOf(code) + code.length();
        }

        return result;
    }

    private static int location;
    public static String ParseData(String pData,String mac){
        String result = "";

        if(pData == null || TextUtils.equals("",pData) || !pData.contains("020104")){
            return result;
        }

        if(TextUtils.equals("",mac) && pData.length() >= 32){
            return pData.substring(24,32);
        }

        if(!TextUtils.equals("",mac) && pData.contains(mac)){
            location = GetLocation(pData,mac);
            switch (isRightRespond(pData,location)){
                case 1:
                case 3:
                    result = pData.substring(location + 2, location + 6);
                    break;

                case 2:
                case 4:
                    result = pData.substring(location + 2, location + 8);
                    break;

                case 5:
                case 6:
                    result = pData.substring(location + 2, location + 10);
                    break;
            }
        }

        return result;
    }
}

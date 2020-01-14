package com.example.kdhnightlampprotpcol;

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
}

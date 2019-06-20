package com.yayawan.impl;


import cn.nubia.nbgame.sdk.entities.ErrorCode;
import cn.nubia.nbgame.sdk.entities.ErrorMessage;

public class ErrorMsgUtil {
    /**
     * 根据服务器返回码显示相应的错误提示
     * @param errorCode
     */
    public static String translate(int errorCode) {
        String errorMsg = "";

        switch (errorCode) {
            case ErrorCode.START_SILENT_UPGRADE:
                errorMsg = ErrorMessage.START_SILENT_UPGRADE;
                break;
            case ErrorCode.START_NORMAL_UPGRADE:
                errorMsg = ErrorMessage.START_NORMAL_UPGRADE;
                break;
            case ErrorCode.START_FORCE_UPGRADE:
                errorMsg = ErrorMessage.START_FORCE_UPGRADE;
                break;
            case ErrorCode.SILENT_UPGRADE_FAILED:
                errorMsg = ErrorMessage.SILENT_UPGRADE_FAILED;
                break;
            case ErrorCode.SILENT_UPGRADE_SUCCESS:
                errorMsg = ErrorMessage.SILENT_UPGRADE_SUCCESS;
                break;
            case ErrorCode.NB_GAME_NOT_INSTALL:
                errorMsg = ErrorMessage.NB_GAME_NOT_INSTALL;
                break;
            case ErrorCode.APP_INFO_ARGUMENT_EXCEPTION:
                errorMsg = ErrorMessage.APP_INFO_ARGUMENT_EXCEPTION;
                break;
            case ErrorCode.USER_CANCLE:
                errorMsg = ErrorMessage.USER_CANCLE;
                break;
            default:
                errorMsg = String.valueOf(errorCode);
                break;
        }

        return errorMsg;
    }
}

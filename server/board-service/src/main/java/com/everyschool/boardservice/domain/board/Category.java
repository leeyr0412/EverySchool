package com.everyschool.boardservice.domain.board;

import lombok.Getter;

@Getter
public enum Category {

    FREE(6001, "자유게시판"),
    NOTICE(6002, "공지사항"),
    COMMUNICATION(6003, "가정통신문");

    private final int code;
    private final String text;

    Category(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static String getText(int code) {
        if (FREE.getCode() == code) {
            return FREE.getText();
        }

        if (NOTICE.getCode() == code) {
            return FREE.getText();
        }

        if (COMMUNICATION.getCode() == code) {
            return FREE.getText();
        }

        throw new IllegalArgumentException();
    }
}

package com.company.common.tools;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class Language {
    public static Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    public static boolean isSimplifiedChinese() {
        return Locale.SIMPLIFIED_CHINESE.equals(getCurrentLocale());
    }

    public static boolean isSimplifiedChinese(String lang) {
        return Locale.SIMPLIFIED_CHINESE.toString().equals(lang);
    }
}

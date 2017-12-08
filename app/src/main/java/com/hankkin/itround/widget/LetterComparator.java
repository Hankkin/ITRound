package com.hankkin.itround.widget;

import com.hankkin.itround.bean.UserBean;

import java.util.Comparator;


public class LetterComparator implements Comparator<UserBean> {

    @Override
    public int compare(UserBean l, UserBean r) {
        if (l == null || r == null) {
            return 0;
        }
        String lhsSortLetters = l.getName().substring(0, 1).toUpperCase();
        String rhsSortLetters = r.getName().substring(0, 1).toUpperCase();
        if (lhsSortLetters == null || rhsSortLetters == null) {
            return 0;
        }
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}
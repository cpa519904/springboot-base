package com.company.common.tools;

import com.company.pojo.model.User;

public class UserUtil {
    public static User getCurrentUser() {
        return (User) ThreadLocalUtil.get();
    }
}

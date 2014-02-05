package com.webi.ent.registration;

public class DuplicateUserRegistrationException extends Exception {

    public DuplicateUserRegistrationException() {
    }

    public DuplicateUserRegistrationException(String arg0) {
        super(arg0);
    }

    public DuplicateUserRegistrationException(Throwable arg0) {
        super(arg0);
    }

    public DuplicateUserRegistrationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DuplicateUserRegistrationException(String arg0, Throwable arg1,
                                              boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}

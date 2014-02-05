package com.webi.ent.registration;

import com.webi.ent.common.vo.RegistrationVO;

public class RegistrationHelper {
    /**
     * Registers the user if does not exist already
     * @param registrationVO
     * @throws DuplicateUserRegistrationException
     */
    public static void registerNewUser(RegistrationVO registrationVO) throws DuplicateUserRegistrationException {
        // Use Hibernate to persist the data in DB
    }
}

package com.webi.ent.common.vo

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Created by yuyutsu on 2/4/14.
 */
class RegistrationVO {
    @NotNull
    @Size(min=8,max=300)
    String emailId

    @NotNull
    @Size(min=4,max=50)
    String userName

    @NotNull
    @Size(min=5)
    String password
}

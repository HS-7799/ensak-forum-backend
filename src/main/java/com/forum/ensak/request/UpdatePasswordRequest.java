package com.forum.ensak.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdatePasswordRequest {

    @NotBlank(message = "Old password is required")
    @Size(min = 6, max = 40,message = "Old password must be less than 40 and greather than 6")
    private String oldpassword;

    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 40,message = "New password must be less than 40 and greather than 6")
    private String newpassword;

    @NotBlank(message = "New password confirmation is required")
    @Size(min = 6, max = 40,message = "New password confirmation must be less than 40 and greather than 6")
    private String passwordconfirmation;

    public UpdatePasswordRequest() {
    }

    public UpdatePasswordRequest(@NotBlank @Size(min = 6, max = 40) String oldpassword, @NotBlank @Size(min = 6, max = 40) String newpassword, @NotBlank @Size(min = 6, max = 40) String passwordconfirmation) {
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
        this.passwordconfirmation = passwordconfirmation;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getPasswordconfirmation() {
        return passwordconfirmation;
    }

    public void setPasswordconfirmation(String passwordconfirmation) {
        this.passwordconfirmation = passwordconfirmation;
    }
}

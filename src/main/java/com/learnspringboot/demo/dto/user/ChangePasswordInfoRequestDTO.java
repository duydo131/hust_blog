package com.learnspringboot.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordInfoRequestDTO {

    @NotNull
    @JsonAlias("old-password")
    private String oldPassword;

    @NotNull
    @JsonAlias("new-password-1")
    private String newPassword1;

    @NotNull
    @JsonAlias("new-password-2")
    private String newPassword2;

    public boolean checkRepeatPassword(){return newPassword1.equals(newPassword2);}
}

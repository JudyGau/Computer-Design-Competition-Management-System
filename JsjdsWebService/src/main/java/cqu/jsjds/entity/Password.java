package cqu.jsjds.entity;

import lombok.Data;

@Data
public class Password {
    String oldPassword;
    String newPassword;
    String newRePassword;
}

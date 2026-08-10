package cqu.jsjds.entity;

import lombok.Getter;

@Getter // 提供获取属性值的getter方法
public enum ResultCodeEnum {

    SUCCESS(200, "操作成功"),
    USER_NON_EXISTS(211, "查无此用户"),
    LOGIN_ERROR(201, "账号或密码错误"),
    VALIDATECODE_ERROR(202, "验证码错误"),
    LOGIN_AUTH(208, "用户未登录"),
    USER_NAME_IS_EXISTS(209, "用户已存在"),
    SYSTEM_ERROR(9999, "您的网络有问题请稍后重试"),
    NODE_ERROR(217, "该节点下有子节点，不可以删除"),
    DATA_ERROR(204, "数据异常"),
    ACCOUNT_STOP(216, "账号已停用"),
    ORDER_EXISTS(218, "订单已存在或订单号过期,请刷新页面重试"),
    GROUP_NAME_IS_EXISTS(219, "队伍名称已被使用"),
    DATA_NOT_EXISTS(220, "查无数据"),
    GROUP_NUMBER_LIMIT(221, "队伍人数已满"),
    WORK_NAME_IS_EXISTS(222, "作品名称已存在"),

    OLD_PASSWORD_ERROR(223, "原密码错误"),
    RE_PASSWORD_NOT_EQUAL_PASSWORD(224, "两次输入的密码不一致"),
    FILE_UPLOAD_FAILED(225, "文件上传失败"),
    NO_FILE_UPLOADED(226, "没有文件上传"),
    IS_APPLYING_GROUP(227, "您正在申请其他队伍"),
    HAVE_JOINED_GROUP(228, "您已经加入其他队伍"),

    AWARD_PERCENT_ERROR(229, "要求一等奖比例<二等奖比例<三等奖比例"),

    JOIN_NONE_GROUP(230, "您尚未加入任何队伍"),

    WORK_NOT_EXISTS(231, "作品不存在"),

    GROUP_NAME_EMPTY(232, "队伍名称不能为空"),

    NO_PERMISSION(233, "无权限"),

    NO_REPEAT_OPERATE(234, "请勿重复操作");

    private Integer code;      // 业务状态码
    private String message;    // 响应消息

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
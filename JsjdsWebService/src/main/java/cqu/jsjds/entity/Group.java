package cqu.jsjds.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Group {
    @Schema(description = "队伍名称")
    String groupName;
    @Schema(description = "队伍人数上限")
    int number;
    @Schema(description = "队伍所属学校")
    String school;
    @Schema(description = "指导教师编号")
    String teacherId;
    @Schema(description = "队伍当前人数")
    String currentNumber;
}

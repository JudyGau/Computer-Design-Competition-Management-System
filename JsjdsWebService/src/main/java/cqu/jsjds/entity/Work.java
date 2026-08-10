package cqu.jsjds.entity;

import lombok.Data;

@Data
public class Work {
    String workName;
    String groupName;
    String category;
    String subclass;
    int isSubmitted;
    int isSchoolCompetition;
    int isProvinceCompetition;
    int IsApproved;
    int score;
    int award;
    int schoolCompetitionScore;
    String comment;
}

package com.example.projecthope.entity;

import com.example.projecthope.annotation.ExlBind;
import com.example.projecthope.util.Jackson;
import com.example.projecthope.util.module.EmptyStringAsNullDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_hope", schema = "qmai-wg")
public class ProjectHope {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;
    @Basic
    @Column(name = "name", nullable = false)
    @ExlBind(value = "姓名",required = true)
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String name;
    @Basic
    @Column(name = "identity", unique = true, nullable = false)
    @ExlBind(value = "身份证号",required = true)
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String identity;
    @Basic
    @Column(name = "sex")
    @ExlBind("性别")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String sex;
    @Basic
    @Column(name = "school")
    @ExlBind("现就读学校")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String school;
    @Basic
    @Column(name = "grade")
    @ExlBind("所在班级")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String grade;
    @Basic
    @Column(name = "teacher")
    @ExlBind("班主任姓名")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String teacher;
    @Basic
    @Column(name = "addr")
    @ExlBind("家庭住址")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String addr;
    @Basic
    @Column(name = "guardian_relationship")
    @ExlBind("监护人")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String guardianRelationship;
    @Basic
    @Column(name = "guardian_name")
    @ExlBind("监护人姓名")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String guardianName;
    @Basic
    @Column(name = "phone")
    @ExlBind("联系方式")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String phone;
    @Basic
    @Column(name = "family_info")
    @ExlBind("家庭情况")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String familyInfo;
    @Basic
    @Column(name = "receiving_way")
    @ExlBind("受助方式")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String receivingWay;
    @Basic
    @Column(name = "project_name")
    @ExlBind("项目名称/爱心人士姓名")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String projectName;
    @Basic
    @Column(name = "subsidize_time")
    @ExlBind("资助时间/开始结对时间")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String subsidizeTime;
    @Basic
    @Column(name = "receiving_amount")
    @ExlBind("受助金额（元）/结对计划")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String receivingAmount;
    @Basic
    @Column(name = "other")
    @ExlBind("其他需要说明的情况")
    @JsonDeserialize(using = EmptyStringAsNullDeserializer.class)
    private String other;
    @Basic
    @Column(name = "bach_id")
    private Long bachId;
    @Basic
    @Column(name = "is_delete", nullable = false)
    private Integer isDelete;
    @Basic
    @Column(name = "created_at", insertable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Timestamp createdAt;
    @Basic
    @Column(name = "updated_at", insertable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Timestamp updatedAt;
    @Basic
    @Column(name = "deleted_at")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Timestamp deletedAt;


    public void setVariable(Field field, String value) {
        try {
            field.set(this, value);
        } catch (Exception e) {
            log.error("ProjectHope[setVariable] occurred error field:{} value:{}", field.getName(), value);
        }
    }

    public String getVariable(Field field) {
        try {
            return (String) field.get(this);
        } catch (Exception e) {
            log.error("ProjectHope[getVariable] occurred error field:{}", field.getName());
        }
        return null;
    }

    @Override
    public String toString() {
        return Jackson.toJsonString(this);
    }
}

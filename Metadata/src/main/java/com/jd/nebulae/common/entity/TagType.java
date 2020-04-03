package com.jd.nebulae.common.entity;

import com.jd.nebulae.common.annotation.Column;
import com.jd.nebulae.common.annotation.Table;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Table("tag_type")
public class TagType {

  @Column(isPrimaryKey = true)
  private Long id;

  @Size(max = 50,message = "code长度不能超过50")
  private String code;
  @Size(max = 30,message = "描述长度不能超过50")
  private String name;

  @Size(max = 30,message = "描述长度必须为1到100")
  private String description;

  private Integer level;

  private Integer sort;

  @Column("p_code")
  private String pCode;

  @Column("p_name")
  private String pName;

  @Column("create_by")
  private String createBy;

  @Column("update_by")
  private String updateBy;

  @Column("create_time")
  private Long createTime;

  @Column("update_time")
  private Long updateTime;

  @Column("category")
  private String category;

  private String username;

}

package com.jd.nebulae.common.entity;
import com.jd.nebulae.common.annotation.Column;
import com.jd.nebulae.common.annotation.Table;
import lombok.Data;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@Table("biz_line")
@Data
public class BizLine {
    @Column(isPrimaryKey = true)
    private Long id;

    private String name;

    private String comment;

    private String code;

    private Integer level;

    @Column("level_code")
    private String levelCode;

    @Column("ordinal")
    private Integer order;

    @Column("top_id")
    private Integer topId;

    private String topName;

    private Long pid;

    @Column("create_by")
    private String createBy;

    @Column("update_by")
    private String updateBy;

    @Column("create_time")
    private Long createTime;

    @Column("update_time")
    private Long updateTime;

    private String username;
}

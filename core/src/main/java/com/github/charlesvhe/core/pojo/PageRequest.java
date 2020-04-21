package com.github.charlesvhe.core.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

@ApiModel
@Data
public class PageRequest<F> {
    @ApiModelProperty(value = "页码 从1开始", example = "1")
    private long page = 1;
    @ApiModelProperty("深度分页时的游标")
    private String cursor;
    @ApiModelProperty(value = "每页条数", example = "10")
    private long size = 10;
    @ApiModelProperty(value = "总条数 不传或传0则从数据库查询返回 正数则直接复用不从数据库查询 负数则忽略", example = "0")
    private long total = 0;

    @ApiModelProperty("查询条件")
    @Valid
    private F filter;
    @ApiModelProperty("排序 逗号分隔 -代表desc 例如 sort=fieldA,-fieldB")
    private String sort;
    @ApiModelProperty("返回字段列表 逗号分隔 不传则返回所有 用于裁剪返回内容")
    private String field;

    public boolean needCount() {
        return total == 0;
    }

    public PageRequest next() {
        if (total < 0 || (page * size) < total) {
            page += 1;
        }
        return this;
    }
}

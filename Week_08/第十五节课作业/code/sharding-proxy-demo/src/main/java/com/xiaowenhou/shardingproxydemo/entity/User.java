package com.xiaowenhou.shardingproxydemo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId
    private Long id;

    private String username;

    private String password;

    private String nickName;

    private String name;

    private String birthday;

    private Integer sex;

    private String headPic;

    private String phone;

    private String email;

    private Integer status;

    private Integer isMobileCheck;

    private Integer isEmailCheck;

    private Long createdTime;

    private Long updatedTime;
}

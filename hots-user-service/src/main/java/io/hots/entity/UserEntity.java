package io.hots.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 8:42 下午
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class UserEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    private String name;

    /**
     * 密码
     */
    private String pwd;


    /**
     * 盐，用于个人敏感信息处理
     */
    private String secret;

    /**
     * 创建时间
     */
    private Date createTime;
}

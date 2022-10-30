package org.rainy.commerce.entity;

import com.google.common.base.Preconditions;
import lombok.*;
import org.rainy.commerce.param.RegisterParam;
import org.rainy.commerce.util.PasswordUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户实体类
 * </p>
 *
 * @author zhangyu
 */
@Entity
@Table(schema = "commerce", name = "t_user")
@EntityListeners(AuditingEntityListener.class)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    public static User convert(RegisterParam registerParam) {
        Preconditions.checkNotNull(registerParam);
        return new RegisterParamConvert().convert(registerParam);
    }


    private static class RegisterParamConvert implements Converter<RegisterParam, User> {

        @Override
        public User convert(@NonNull RegisterParam registerParam) {
            User user = new User();
            BeanUtils.copyProperties(registerParam, user);
            user.setPassword(PasswordUtils.encrypt(registerParam.getPassword()));
            return user;
        }

    }


}

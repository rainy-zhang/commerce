package org.rainy.commerce.repository;

import org.rainy.commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <p>
 * 用户数据访问层
 * </p>
 *
 * @author zhangyu
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    void deleteByUsername(String username);

}

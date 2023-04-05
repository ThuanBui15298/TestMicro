package com.example.userservice.repository;

import com.example.userservice.config.CustomUserDetails;
import com.example.userservice.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByCodeAndDeleted(String code, Integer deleted);

    Optional<Users> findByEmailAndDeleted(String email, Integer deleted);

    Optional<Users> findByPhoneAndDeleted(String phone, Integer deleted);

    List<Users> findAllByIdInAndDeleted(List<Long> id, Integer deleted);

    @Query(value = "select u from Users u where u.deleted = 1 and u.code =: userName or u.username = :userName")
    Users findByUserNameOrCode(@Param("userName") String userName);

    @Query(value = " select * from users u " +
            " WHERE u.deleted = 1 ", nativeQuery = true)
    Page<Map<String, Object>> findAllByUsers(Pageable pageable);


    CustomUserDetails findByCode(String code);


    @Query(value = "                select u.name , u.id, u.code, u.phone, u.email, u.date_of_birth, u.status , u.note                                                   \n" +
            "                         from users u      WHERE u.deleted = 1 and (u.status in (:status) or -1 in (:status))                                              \n" +
            "                          AND lower(concat(coalesce(u.name ,''), coalesce(u.code ,'')))     " +
            "                           like lower(concat('%', :search, '%'))    ", countQuery = "                             select u.name , u.id, u.code, u.phone, u.email, u.date_of_birth, u.status , u.note                                                   \n" +
            "                             from users u      WHERE u.deleted = 1 and (u.status in (:status) or -1 in (:status))                                              \n" +
            "                               AND lower(concat(coalesce(u.name ,''), coalesce(u.code ,'')))      " +
            "                            like lower(concat('%', :search, '%'))  ", nativeQuery = true)
    Page<Map<String, Object>> findAllBySearch(Pageable pageable,
                                              @Param("search") String search,
                                              @Param("status") Integer status);

    Users findByEmail(String email);

    Optional<Users> findAllByEmail(String email);

    @Query("select u from Users u where u.status = 1 and u.code = :userCode")
    Users findByUserCodeAndStatus(@Param("userCode") String userCode);


//    @Transactional
//    @Modifying
//    @Query(value = "insert into roles_users(users_id, roles_id) values(:user_id , :roles_id)", nativeQuery = true)
//    void insert(Long user_id, Long roles_id);

}

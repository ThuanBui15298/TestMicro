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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByCodeAndDeleted(String code, Integer deleted);

    Optional<Users> findByEmailAndDeleted(String email, Integer deleted);

    Optional<Users> findByPhoneAndDeleted(String phone, Integer deleted);

    List<Users> findAllByIdInAndDeleted(List<Long> id, Integer deleted);

    Users findByCodeAndStatusAndDeleted(String userCode, Integer status, Integer deleted);

    Optional<Users> findByIdAndDeletedAndStatus(Long id, Integer status, Integer deleted);

    Users findAllById(Long id);

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

    @Modifying
    @Transactional
    @Query(value = "insert into roles_users(users_id, roles_id) values(:user_id ,:roles_id)", nativeQuery = true)
    void insert(Long user_id, Long roles_id);


    @org.springframework.transaction.annotation.Transactional
    @Modifying
    @Query(value = "  DELETE FROM roles_users ru WHERE ru.users_id = :id ", nativeQuery = true)
    void deleteByUserId(@Param("id") Long id);

}

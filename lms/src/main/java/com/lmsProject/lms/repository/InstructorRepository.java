package com.lmsProject.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsProject.lms.entity.Instructor;
import java.util.List;
import java.util.Optional;

import com.lmsProject.lms.enums.InstructorStatus;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    List<Instructor> findByActive(boolean active);

    Optional<Instructor> findByEmail(String email);

    Optional<Instructor> findByPhone(String phoneNumber);

    @Query("SELECT Count(*) FROM Instructor i WHERE i.email =:email")
    int getCount(String email);

    @Query("SELECT Count(*) FROM Instructor i where i.phone =:phone")
    int getCountOfPhoneNumber(String phone);


    @Query("SELECT i FROM Instructor i WHERE i.active = :active AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(i.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Instructor> searchByActiveAndKeyword(@Param("active") boolean active, @Param("keyword") String keyword);

    @Query("SELECT i from Instructor i WHERE LOWER(i.name) = LOWER(:name)")
    Optional<Instructor> findByName(@Param("name") String name);

}

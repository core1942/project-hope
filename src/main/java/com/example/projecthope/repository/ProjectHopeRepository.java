package com.example.projecthope.repository;

import com.example.projecthope.entity.ProjectHope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProjectHopeRepository extends JpaRepository<ProjectHope, Integer>/* , JpaSpecificationExecutor<ProjectHope> */ {
    @Modifying
    @Transactional
    @Query("update ProjectHope u set u.isDelete = ?2,u.deletedAt=current_timestamp where u.id in ?1")
    int updateIsDeleteById(List<Integer> ids, Integer isDelete);

}

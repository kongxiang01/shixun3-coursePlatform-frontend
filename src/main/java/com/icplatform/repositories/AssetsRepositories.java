package com.icplatform.repositories;

import com.icplatform.entity.Assets;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetsRepositories extends JpaRepository<Assets, Integer> {

    Optional<Assets> findByFname(String fname);

    //根据文件名查询文件路径
    default Optional<String> findTpathByFname(String fname) {
        return findByFname(fname).map(Assets::getTpath);
    }
}

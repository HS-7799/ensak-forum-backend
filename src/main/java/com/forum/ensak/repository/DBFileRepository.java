package com.forum.ensak.repository;

import com.forum.ensak.models.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBFileRepository extends JpaRepository<DBFile,String> {

    DBFile getById(String id);
    DBFile getByFileDownloadUri(String fileDownloadUri);

}

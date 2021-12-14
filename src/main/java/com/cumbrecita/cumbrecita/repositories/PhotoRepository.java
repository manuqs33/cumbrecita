
package com.cumbrecita.cumbrecita.repositories;

import com.cumbrecita.cumbrecita.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, String> {
    
}

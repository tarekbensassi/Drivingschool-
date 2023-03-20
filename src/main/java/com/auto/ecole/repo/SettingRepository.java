package com.auto.ecole.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.auto.ecole.entity.Eleve;
import com.auto.ecole.entity.Elevedetails;
import com.auto.ecole.entity.Setting;




public interface SettingRepository extends CrudRepository<Setting, Long> {
	Setting findTopByOrderByIdDesc();
	
}

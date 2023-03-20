package com.auto.ecole.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.auto.ecole.entity.Eleve;
import com.auto.ecole.entity.User;



public interface EleveRepository extends CrudRepository<Eleve, Long> {
	   public Eleve findEleveByCin(String cin);
	
}

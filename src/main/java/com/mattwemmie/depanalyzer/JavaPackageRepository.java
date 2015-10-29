package com.mattwemmie.depanalyzer;

import org.springframework.data.repository.CrudRepository;

public interface JavaPackageRepository extends CrudRepository<JavaPackage, String> {

    JavaPackage findByName(String name);

}

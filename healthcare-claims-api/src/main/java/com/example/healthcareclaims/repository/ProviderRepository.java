package com.example.healthcareclaims.repository;

import com.example.healthcareclaims.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface ProviderRepository extends JpaRepository<Provider, String> {
} 
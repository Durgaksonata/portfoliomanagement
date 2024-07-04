package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.PipelineState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseLine_PipelineStateRepository extends JpaRepository<PipelineState, Integer> {
}

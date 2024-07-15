package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.PipelineStateRepository;
import com.sonata.portfoliomanagement.model.DataEntry;
import com.sonata.portfoliomanagement.model.PipelineState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("Pipelinestate")
public class PipelineStateController {

    @Autowired
    private PipelineStateRepository pipelineStateRepository;

    @Autowired
    private DataEntryRepository dataEntryRepository;


    @PostMapping("/save")
    public PipelineState savePipelineState(@RequestBody PipelineState pipelineState) {
        return pipelineStateRepository.save(pipelineState);
    }


    @PostMapping("/updatePipelineState")
    public String updatePipelineState() {
        List<Object[]> distinctAccountYearQuarter = dataEntryRepository.findDistinctAccountYearQuarter();

        for (Object[] combination : distinctAccountYearQuarter) {
            String account = (String) combination[0];
            int financialYear = (int) combination[1];
            String quarter = (String) combination[2];

            List<DataEntry> dataEntries = dataEntryRepository.findByAccountAndYearAndQuarter(account, financialYear, quarter);

            float sumOfPipelineOpportunity = 0;
            float sumOfPipelineShaping = 0;
            float sumOfPipelinePitch = 0;
            String deliveryDirector = "";
            String deliveryManager = "";

            for (DataEntry entry : dataEntries) {
                deliveryDirector = entry.getDeliveryDirector();
                deliveryManager = entry.getDeliveryManager();
                switch (entry.getProjectsOrPursuitStage().toLowerCase()) {
                    case "opportunity":
                        sumOfPipelineOpportunity += entry.getUpside();
                        break;
                    case "shaping":
                        sumOfPipelineShaping += entry.getUpside();
                        break;
                    case "pitch":
                        sumOfPipelinePitch += entry.getUpside();
                        break;
                }
            }

            float sumOfPipelineTotal = sumOfPipelineOpportunity + sumOfPipelineShaping + sumOfPipelinePitch;

            PipelineState pipelineState = pipelineStateRepository.findByAccountAndFinancialYearAndQuarter(account, financialYear, quarter);
            if (pipelineState == null) {
                pipelineState = new PipelineState();
                pipelineState.setAccount(account);
                pipelineState.setFinancialYear(financialYear);
                pipelineState.setQuarter(quarter);

            }

            pipelineState.setDeliveryDirector(deliveryDirector);
            pipelineState.setDeliveryManager(deliveryManager);
            pipelineState.setSumOfPipeline_opportunity(sumOfPipelineOpportunity);
            pipelineState.setSumOfPipeline_shaping(sumOfPipelineShaping);
            pipelineState.setSumOfPipeline_pitch(sumOfPipelinePitch);
            pipelineState.setSumOfPipeline_total(sumOfPipelineTotal);

            pipelineStateRepository.save(pipelineState);
        }

        return "Data populated to PipelineState table successfully.";
    }

}

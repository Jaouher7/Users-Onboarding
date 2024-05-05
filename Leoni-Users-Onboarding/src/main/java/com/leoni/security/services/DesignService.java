package com.leoni.security.services;

import com.leoni.models.Design;
import com.leoni.repository.DesignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DesignService {
    @Autowired
    private DesignRepository designRepository;

    public List<Design> getAllDesigns() {
        return designRepository.findAll();
    }

    public Optional<Design> getDesignById(Long id) {
        return designRepository.findById(id);
    }

    public Design createDesign(Design design) {
        return designRepository.save(design);
    }

    public Design updateDesign(Long id, Design newDesignData) {
        Optional<Design> optionalDesign = designRepository.findById(id);
        if (optionalDesign.isPresent()) {
            Design existingDesign = optionalDesign.get();

            existingDesign.setPlant(newDesignData.getPlant());
            existingDesign.setAccount(newDesignData.getAccount());
            existingDesign.setProjectName(newDesignData.getProjectName());
            existingDesign.setDesignDescription(newDesignData.getDesignDescription());
            existingDesign.setUsedSystemBeforeMigration(newDesignData.getUsedSystemBeforeMigration());
            existingDesign.setDesignNbr(newDesignData.getDesignNbr());
            existingDesign.setCountOfLeads(newDesignData.getCountOfLeads());
            existingDesign.setTargetDesignTimeAssigned(newDesignData.getTargetDesignTimeAssigned());
            existingDesign.setPlannedStartDate(newDesignData.getPlannedStartDate());
            existingDesign.setDesignImportResponsible(newDesignData.getDesignImportResponsible());
            existingDesign.setComponentLoadedToLpDb(newDesignData.getComponentLoadedToLpDb());
            existingDesign.setProductDesignCompleted(newDesignData.getProductDesignCompleted());
            existingDesign.setDesignIndex(newDesignData.getDesignIndex());
            existingDesign.setStartDate(newDesignData.getStartDate());
            existingDesign.setDrcCompletedStatus(newDesignData.getDrcCompletedStatus());
            existingDesign.setLeadLengthUpdate(newDesignData.getLeadLengthUpdate());
            existingDesign.seteCNsWorkpackageRecieved(newDesignData.geteCNsWorkpackageRecieved());
            existingDesign.setProcessingTvmCompletedStatus(newDesignData.getProcessingTvmCompletedStatus());
            existingDesign.setsBomUploadStatus(newDesignData.getsBomUploadStatus());
            existingDesign.setCheckedBy(newDesignData.getCheckedBy());
            existingDesign.setDesignMigrationStatus(newDesignData.getDesignMigrationStatus());
            existingDesign.setComments(newDesignData.getComments());


            return designRepository.save(existingDesign);
        } else {
            return null;
        }
    }

    public void deleteDesign(Long id) {
        designRepository.deleteById(id);
    }
}

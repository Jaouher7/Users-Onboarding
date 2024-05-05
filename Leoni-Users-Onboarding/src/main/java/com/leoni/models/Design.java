package com.leoni.models;

import javax.persistence.*;
import java.util.Date;

@Table
@Entity
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Plant;
    private String Account;
    private String projectName;
    private String designDescription;
    private String usedSystemBeforeMigration;
    private String designNbr;
    private String countOfLeads;
    private String targetDesignTimeAssigned;
    private Date plannedStartDate;
    private String designImportResponsible;
    private String componentLoadedToLpDb;
    private Date productDesignCompleted;
    private Integer designIndex;
    private Date startDate;
    private String drcCompletedStatus;
    private String leadLengthUpdate;
    private String eCNsWorkpackageRecieved;
    private String processingTvmCompletedStatus;
    private String sBomUploadStatus;
    private String checkedBy;
    private String designMigrationStatus;
    private String comments;


    public Design() {

    }

    public Design(Long id, String plant, String account, String projectName, String designDescription, String usedSystemBeforeMigration, String designNbr, String countOfLeads, String targetDesignTimeAssigned, Date plannedStartDate, String designImportResponsible, String componentLoadedToLpDb, Date productDesignCompleted, Integer designIndex, Date startDate, String drcCompletedStatus, String leadLengthUpdate, String eCNsWorkpackageRecieved, String processingTvmCompletedStatus, String sBomUploadStatus, String checkedBy, String designMigrationStatus, String comments) {
        this.id = id;
        Plant = plant;
        Account = account;
        this.projectName = projectName;
        this.designDescription = designDescription;
        this.usedSystemBeforeMigration = usedSystemBeforeMigration;
        this.designNbr = designNbr;
        this.countOfLeads = countOfLeads;
        this.targetDesignTimeAssigned = targetDesignTimeAssigned;
        this.plannedStartDate = plannedStartDate;
        this.designImportResponsible = designImportResponsible;
        this.componentLoadedToLpDb = componentLoadedToLpDb;
        this.productDesignCompleted = productDesignCompleted;
        this.designIndex = designIndex;
        this.startDate = startDate;
        this.drcCompletedStatus = drcCompletedStatus;
        this.leadLengthUpdate = leadLengthUpdate;
        this.eCNsWorkpackageRecieved = eCNsWorkpackageRecieved;
        this.processingTvmCompletedStatus = processingTvmCompletedStatus;
        this.sBomUploadStatus = sBomUploadStatus;
        this.checkedBy = checkedBy;
        this.designMigrationStatus = designMigrationStatus;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDesignDescription() {
        return designDescription;
    }

    public void setDesignDescription(String designDescription) {
        this.designDescription = designDescription;
    }

    public String getUsedSystemBeforeMigration() {
        return usedSystemBeforeMigration;
    }

    public void setUsedSystemBeforeMigration(String usedSystemBeforeMigration) {
        this.usedSystemBeforeMigration = usedSystemBeforeMigration;
    }

    public String getDesignNbr() {
        return designNbr;
    }

    public void setDesignNbr(String designNbr) {
        this.designNbr = designNbr;
    }

    public String getCountOfLeads() {
        return countOfLeads;
    }

    public void setCountOfLeads(String countOfLeads) {
        this.countOfLeads = countOfLeads;
    }

    public String getTargetDesignTimeAssigned() {
        return targetDesignTimeAssigned;
    }

    public void setTargetDesignTimeAssigned(String targetDesignTimeAssigned) {
        this.targetDesignTimeAssigned = targetDesignTimeAssigned;
    }

    public Date getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(Date plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public String getDesignImportResponsible() {
        return designImportResponsible;
    }

    public void setDesignImportResponsible(String designImportResponsible) {
        this.designImportResponsible = designImportResponsible;
    }

    public String getComponentLoadedToLpDb() {
        return componentLoadedToLpDb;
    }

    public void setComponentLoadedToLpDb(String componentLoadedToLpDb) {
        this.componentLoadedToLpDb = componentLoadedToLpDb;
    }

    public Date getProductDesignCompleted() {
        return productDesignCompleted;
    }

    public void setProductDesignCompleted(Date productDesignCompleted) {
        this.productDesignCompleted = productDesignCompleted;
    }

    public Integer getDesignIndex() {
        return designIndex;
    }

    public void setDesignIndex(Integer designIndex) {
        this.designIndex = designIndex;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDrcCompletedStatus() {
        return drcCompletedStatus;
    }

    public void setDrcCompletedStatus(String drcCompletedStatus) {
        this.drcCompletedStatus = drcCompletedStatus;
    }

    public String getLeadLengthUpdate() {
        return leadLengthUpdate;
    }

    public void setLeadLengthUpdate(String leadLengthUpdate) {
        this.leadLengthUpdate = leadLengthUpdate;
    }

    public String geteCNsWorkpackageRecieved() {
        return eCNsWorkpackageRecieved;
    }

    public void seteCNsWorkpackageRecieved(String eCNsWorkpackageRecieved) {
        this.eCNsWorkpackageRecieved = eCNsWorkpackageRecieved;
    }

    public String getProcessingTvmCompletedStatus() {
        return processingTvmCompletedStatus;
    }

    public void setProcessingTvmCompletedStatus(String processingTvmCompletedStatus) {
        this.processingTvmCompletedStatus = processingTvmCompletedStatus;
    }

    public String getsBomUploadStatus() {
        return sBomUploadStatus;
    }

    public void setsBomUploadStatus(String sBomUploadStatus) {
        this.sBomUploadStatus = sBomUploadStatus;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public String getDesignMigrationStatus() {
        return designMigrationStatus;
    }

    public void setDesignMigrationStatus(String designMigrationStatus) {
        this.designMigrationStatus = designMigrationStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

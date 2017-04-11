package com.luxoft.horizon.dir.entities.domain;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.awt.geom.Ellipse2D;
import java.util.Date;
import java.util.DoubleSummaryStatistics;

/**
 * Created by bogatp on 26.05.16.
 */
@Entity
@Table(name = "FORECAST_DIRECTORATES")
public class KPI {

    @Expose
    @Id
    @Column(name="ROW_NUM")
    @GeneratedValue
    private long id;

    @Expose
    @Column(name = "timeline")
    private Date timeline;

    @Expose
    @Column(name = "reportdate")

    private Date reportdate;

    @Expose
    @Column(name = "[revenue plan]",columnDefinition = "Decimal")
    private Double RevenuePlan;

    @Expose
    @Column(name = "[revenue fact]",columnDefinition = "Decimal")
    private Double RevenueFact;

    @Expose
    @Column(name = "[Revenue Previous FactForecast]",columnDefinition = "Decimal")
    private Double revenuePreviousFactForecast;

    @Expose
    @Column(name = "[CM Plan]",columnDefinition = "Decimal")
    private Double cmPLan;

    @Expose
    @Column(name = "[CM Fact]",columnDefinition = "Decimal")
    private Double cmFact;

    @Expose
    @Column(name = "[CM Previous FactForecast]",columnDefinition = "Decimal")
    private Double cmPreviousFactForecast;

    @Expose
    @Column(name = "[Net Income Plan]",columnDefinition = "Decimal")
    private Double netIncomePlan;

    @Expose
    @Column(name = "[Net Income Fact]",columnDefinition = "Decimal")
    private Double netIncomeFact;

    @Expose
    @Column(name = "[Net Income Previous FactForecast]",columnDefinition = "Decimal")
    private Double netIncomePreviousFactForecast;

    @Expose
    @Column(name = "[EBITDA Plan]",columnDefinition = "Decimal")
    private Double EBITDAPlan;

    @Expose
    @Column(name = "[EBITDA Fact]",columnDefinition = "Decimal")
    private Double EBITDAFact;

    @Expose
    @Column(name = "[EBITDA Previous FactForecast]",columnDefinition = "Decimal")
    private Double EBITDAPreviousFactForecast;

    @Expose
    @Column(name = "currency")
    private String currency;

    @Expose
    @Column(name = "reporttype")
    private String reportType;

    @Expose
    @Column(name = "budget")
    private String budget;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Date getTimeline() {
        return timeline;
    }

    public void setTimeline(Date timeline) {
        this.timeline = timeline;
    }

    public Date getReportdate() {
        return reportdate;
    }

    public void setReportdate(Date reportdate) {
        this.reportdate = reportdate;
    }

    public Double getRevenuePlan() {
        return RevenuePlan;
    }

    public void setRevenuePlan(Double revenuePlan) {
        RevenuePlan = revenuePlan;
    }

    public Double getRevenueFact() {
        return RevenueFact;
    }

    public void setRevenueFact(Double revenueFact) {
        RevenueFact = revenueFact;
    }

    public Double getRevenuePreviousFactForecast() {
        return revenuePreviousFactForecast;
    }

    public void setRevenuePreviousFactForecast(Double revenuePreviousFactForecast) {
        this.revenuePreviousFactForecast = revenuePreviousFactForecast;
    }

    public Double getCmPLan() {
        return cmPLan;
    }

    public void setCmPLan(Double cmPLan) {
        this.cmPLan = cmPLan;
    }

    public Double getCmFact() {
        return cmFact;
    }

    public void setCmFact(Double cmFact) {
        this.cmFact = cmFact;
    }

    public Double getCmPreviousFactForecast() {
        return cmPreviousFactForecast;
    }

    public void setCmPreviousFactForecast(Double cmPreviousFactForecast) {
        this.cmPreviousFactForecast = cmPreviousFactForecast;
    }

    public Double getNetIncomePlan() {
        return netIncomePlan;
    }

    public void setNetIncomePlan(Double netIncomePlan) {
        this.netIncomePlan = netIncomePlan;
    }

    public Double getNetIncomeFact() {
        return netIncomeFact;
    }

    public void setNetIncomeFact(Double netIncomeFact) {
        this.netIncomeFact = netIncomeFact;
    }

    public Double getNetIncomePreviousFactForecast() {
        return netIncomePreviousFactForecast;
    }

    public void setNetIncomePreviousFactForecast(Double netIncomePreviousFactForecast) {
        this.netIncomePreviousFactForecast = netIncomePreviousFactForecast;
    }

    public Double getEBITDAPlan() {
        return EBITDAPlan;
    }

    public void setEBITDAPlan(Double EBITDAPlan) {
        this.EBITDAPlan = EBITDAPlan;
    }

    public Double getEBITDAFact() {
        return EBITDAFact;
    }

    public void setEBITDAFact(Double EBITDAFact) {
        this.EBITDAFact = EBITDAFact;
    }

    public Double getEBITDAPreviousFactForecast() {
        return EBITDAPreviousFactForecast;
    }

    public void setEBITDAPreviousFactForecast(Double EBITDAPreviousFactForecast) {
        this.EBITDAPreviousFactForecast = EBITDAPreviousFactForecast;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

}

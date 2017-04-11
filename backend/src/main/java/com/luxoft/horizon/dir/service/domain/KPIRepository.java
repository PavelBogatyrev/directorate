package com.luxoft.horizon.dir.service.domain;

import com.luxoft.horizon.dir.entities.domain.KPI;
import com.luxoft.horizon.dir.utils.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by bogatp on 26.05.16.
 */

public interface KPIRepository extends CrudRepository<KPI, Long> {

    //kpi-month
    @Query(value = KpiMonthQueries.KPI_MONTH, nativeQuery = true)
    List<Object[]> findMonthSumValueForKPI(int month, int year);
    //kpi-year
    @Query(value = Queries.KPI_YEAR, nativeQuery = true)
    List<Object[]> findYearSumValueForKPI();

    //lobs

    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_REVENUE_FIRSTVALUE, nativeQuery = true)
    Object findKpiByLobRevenueFirstValue();


    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_CM_FIRSTVALUE, nativeQuery = true)
    Object findKpiByLobCMFirstValue();

    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_REVENUE_GCOE_FIRSTVALUE, nativeQuery = true)
    Object findKpiByLobRevenueGCoEFirstValue();

    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_CM_GCOE_FIRSTVALUE, nativeQuery = true)
    Object findKpiByLobCMGCoEFirstValue();

    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_CM_SECONDVALUE, nativeQuery = true)
    Object findKpiByLobCMSecondvalue();

    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_REVENUE_SECONDVALUE, nativeQuery = true)
    Object findKpiByLobRevenueSecondvalue();

    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_REVENUE_GCOE_SECONDVALUE, nativeQuery = true)
    Object findKpiByLobRevenueGCoESecondvalue();

    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_CM_GCOE_SECONDVALUE, nativeQuery = true)
    Object findKpiByLobCMGCoESecondvalue();
    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_REVENUE_DELTAS, nativeQuery = true)
    Object[] findKpiByLobRevenueDeltas();
    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_CM_DELTAS, nativeQuery = true)
    Object[] findKpiByLobCMDeltas();
    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_REVENUE_GCOE_DELTAS, nativeQuery = true)
    Object[] findKpiByLobRevenueGCoEDeltas();
    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_CM_GCOE_DELTAS, nativeQuery = true)
    Object[] findKpiByLobCMGCoEDeltas();


    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_REVENUE_LASTVALUE, nativeQuery = true)
    Object findKpiByLobRevenueLastValue();


    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_CM_LASTVALUE, nativeQuery = true)
    Object findKpiByLobCMLastValue();

    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_REVENUE_GCOE_LASTVALUE, nativeQuery = true)
    Object findKpiByLobRevenueGCoELastValue();

    @Query(value = RevenueLobsForecastForexQueries.KPI_BY_LOBS_CM_GCOE_LASTVALUE, nativeQuery = true)
    Object findKpiByLobCMGCoELastValue();

    @Query(value = Queries.REVENUE_CLIENTS, nativeQuery = true)
    Object[] findClientsRevenue();


    @Query(value = Queries.KPI_BY_LOBS_REVENUE_CM_FORECAST_CURRENT_YEAR_SMALL, nativeQuery = true)
    Object[][] findKpiByLobsRevenueCMForecastCurrentYearSmall();

    @Query(value = Queries.KPI_BY_LOBS_REVENUE_CM_FORECAST_CURRENT_YEAR_BIG, nativeQuery = true)
    Object[][] findKpiByLobsRevenueCMForecastCurrentYearBig();

    @Query(value = Queries.KPI_BY_LOBS_REVENUE_CM_FACT_PREVIOUS_YEAR_SMALL, nativeQuery = true)
    Object[][] findKpiByLobsRevenueCMFactPreviousYearSmall();

    @Query(value = Queries.KPI_BY_LOBS_REVENUE_CM_FACT_PREVIOUS_YEAR_BIG, nativeQuery = true)
    Object[][] findKpiByLobsRevenueCMFactPreviousYearBig();

    @Query(value = Queries.KPI_REVENUE_CM_CURRENT_YEAR, nativeQuery = true)
    Object[][] findKpiRevenueCMCurrentYear();


    @Query(value = Queries.REVENUE_CM_LOBS_FACTFORECAST_LEFT, nativeQuery = true)
    Object[] findLobsRevenueCMFactLeft();

    @Query(value = Queries.REVENUE_CM_LOBS_FACTFORECAST_RIGHT, nativeQuery = true)
    Object[] findLobsRevenueCMFactRight();

    @Query(value = Queries.REVENUE_CM_LOBS_PLAN_LEFT, nativeQuery = true)
    Object[] findLobsRevenueCMPlanLeft();

    @Query(value = Queries.REVENUE_CM_LOBS_PLAN_RIGHT, nativeQuery = true)
    Object[] findLobsRevenueCMPlanRight();

    @Query(value = LobsQueries.HEADCOUNT,nativeQuery = true)
    Object[] findHeadCountByLobs(String headcount);

    @Query(value = LobsQueries.YEAR,nativeQuery = true)
    Object[] findLobsYear(String headcount, String currency);


    @Query(value = LobsQueries.MONTH,nativeQuery = true)
    Object[] findLobsMonth(int month, int year, String lobs, String currency);

    @Query(value = LobsQueries.HORIZON_YEAR,nativeQuery = true)
    Object[] findHorizonLobYear(String currency);

    @Query(value = LobsQueries.HORIZON_MONTH,nativeQuery = true)
    Object[] findHorizonLobMonth(int month, int year, String currency);

    @Query(value = LobsQueries.GLOBAL_YEAR,nativeQuery = true)
    Object[] findGlobalLobYear(String currency);
    @Query(value = LobsQueries.GLOBAL_MONTH,nativeQuery = true)
    Object[] findGlobalLobMonth(int month, int year, String currency);


    @Query(value =Queries.REVENUE_CM_COE_PREVIOUS_YEAR_LEFT,nativeQuery = true)
    Object[] findLobsRevenueCMCoePreviousYearLeft();
    @Query(value = Queries.REVENUE_CM_COE_CURRENT_YEAR_LEFT,nativeQuery = true)
    Object[] findLobsRevenueCMCoeCurrentYearLeft();
    @Query(value = Queries.REVENUE_CM_COE_PREVIOUS_YEAR_RIGHT,nativeQuery = true)
    Object[] findLobsRevenueCMCoePreviousYearRight();
    @Query(value = Queries.REVENUE_CM_COE_CURRENT_YEAR_RIGHT,nativeQuery = true)
    Object[] findLobsRevenueCMCoeCurrentYearRight();



    //lobs
    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_REVENUE_FIRSTVALUE_VSPLAN, nativeQuery = true)
    Object findKpiByLobRevenueFirstValueVsplan();


    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_CM_FIRSTVALUE_VSPLAN, nativeQuery = true)
    Object findKpiByLobCMFirstValueVsplan();

    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_REVENUE_GCOE_FIRSTVALUE_VSPLAN, nativeQuery = true)
    Object findKpiByLobRevenueGCoEFirstValueVsplan();

    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_CM_GCOE_FIRSTVALUE_VSPLAN, nativeQuery = true)
    Object findKpiByLobCMGCoEFirstValueVsplan();

    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_REVENUE_DELTAS_VSPLAN, nativeQuery = true)
    Object[] findKpiByLobRevenueDeltasVsplan();
    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_CM_DELTAS_VSPLAN, nativeQuery = true)
    Object[] findKpiByLobCMDeltasVsplan();
    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_REVENUE_GCOE_DELTAS_VSPLAN, nativeQuery = true)
    Object[] findKpiByLobRevenueGCoEDeltasVsplan();
    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_CM_GCOE_DELTAS_VSPLAN, nativeQuery = true)
    Object[] findKpiByLobCMGCoEDeltasVsplan();


    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_REVENUE_LASTVALUE_VSPLAN, nativeQuery = true)
    Object findKpiByLobRevenueLastValueVsplan();


    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_CM_LASTVALUE_VSPLAN, nativeQuery = true)
    Object findKpiByLobCMLastValueVsplan();

    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_REVENUE_GCOE_LASTVALUE_VSPLAN, nativeQuery = true)
    Object findKpiByLobRevenueGCoELastValueVsplan();

    @Query(value = RevenueLobsVsplanForexQueries.KPI_BY_LOBS_CM_GCOE_LASTVALUE_VSPLAN, nativeQuery = true)
    Object findKpiByLobCMGCoELastValueVsplan();

    @Query(value = FourPracticesQueries.FOUR_PRACTICES_DATA, nativeQuery = true)
    Object[] findFourPracticesData();

    @Query(value = ReportPeriodQueries.REPORT_PERIODS,nativeQuery = true)
    List<Date> getReportPeriods();

    @Query(value = PracticesQueries.PRACTICE_DATA,nativeQuery = true)
    Object[] findPracticesData(String practice, String currency);

    @Query(value = PracticesQueries.HEADCOUNT,nativeQuery = true)
    Object[] findHeadCountByPractice(String practice);
}

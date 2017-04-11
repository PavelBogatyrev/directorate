package com.luxoft.horizon.dir.service.domain;

/**
 * @author rlapin
 */
public interface IViewLobsService {
    Object lobsYear(String lobName);
    Object lobsMonth(String lobName, int month, int year);
    Object lobsHC(String lobName);
}

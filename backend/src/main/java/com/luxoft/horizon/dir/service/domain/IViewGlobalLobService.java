package com.luxoft.horizon.dir.service.domain;

/**
 * @author rlapin
 */
public interface IViewGlobalLobService {
    Object globalLobYear();
    Object globalLobMonth(int month, int year);
    Object globalLobHC();
}

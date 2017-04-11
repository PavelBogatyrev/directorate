package com.luxoft.horizon.dir.service.domain;

/**
 * @author rlapin
 */
public interface IViewHorizonLobService {
    Object horizonLobYear();
    Object horizonLobMonth(int month, int year);
    Object horizonLobHC();
}

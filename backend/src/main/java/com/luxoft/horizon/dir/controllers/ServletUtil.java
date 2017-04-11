package com.luxoft.horizon.dir.controllers;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author rlapin
 */
class ServletUtil {
    public static final String SEPARATOR = "|";

    public static void sendResponse(HttpServletResponse resp, Status status, String msg) throws IOException {
        int httpStatus;
        switch (status) {
            case OK:
            case WARN:
                httpStatus = HttpServletResponse.SC_OK;
                break;
            default:
            case ERROR:
                httpStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                break;
        }
        resp.setStatus(httpStatus);
        resp.getWriter().print(status.name());
        resp.getWriter().print(SEPARATOR);
        if (msg != null) {
            resp.getWriter().print(msg);
        }
        resp.getWriter().close();
    }

    public enum Status {OK, WARN, ERROR}
}

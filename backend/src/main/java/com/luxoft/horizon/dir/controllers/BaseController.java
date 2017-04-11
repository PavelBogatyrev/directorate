package com.luxoft.horizon.dir.controllers;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Created by pavelbogatyrev on 17/04/16.
 */
public class BaseController {

    private static Logger log = Logger.getLogger(BaseController.class.getName());

    protected Gson gson;


    public BaseController() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .excludeFieldsWithoutExposeAnnotation();

        gsonBuilder.registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(final Double src, final Type typeOfSrc, final JsonSerializationContext context) {

                // TODO: Fix the local usage
                NumberFormat f = NumberFormat.getInstance(Locale.US);
                f.setGroupingUsed(false);
                String strVal = f.format(src);
                BigDecimal value = new BigDecimal(0.0);
                try {
                    value = new BigDecimal(strVal);
                } catch (Exception e) {
                    log.severe("Error converting data to BigDecimal: " + src);
                }
                return new JsonPrimitive(value);
            }
        });
        gson = gsonBuilder.create();
    }
}

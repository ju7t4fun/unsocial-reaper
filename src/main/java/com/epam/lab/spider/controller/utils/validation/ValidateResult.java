package com.epam.lab.spider.controller.utils.validation;

import java.util.Map;

/**
 * Created by shell on 6/13/2015.
 */
public interface ValidateResult {
    public boolean isValid();
    public Map<String,String> getInvalidMessage();
}

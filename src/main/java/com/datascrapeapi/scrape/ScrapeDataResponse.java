package com.datascrapeapi.scrape;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScrapeDataResponse {
    private Boolean ok;
    private  String error;
    private String ticker;

    public Boolean isOk() {
        return ok;
    }
}

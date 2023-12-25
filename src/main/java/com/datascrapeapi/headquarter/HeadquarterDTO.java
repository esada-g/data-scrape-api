package com.datascrapeapi.headquarter;

import lombok.Data;

@Data
public class HeadquarterDTO {
    private String city;
    private String state;

    public Headquarter toEntity() {
        Headquarter entity = new Headquarter();
        entity.setCity(this.city);
        entity.setState(this.state);
        return entity;

    }
}

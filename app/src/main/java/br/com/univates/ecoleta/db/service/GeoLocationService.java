package br.com.univates.ecoleta.db.service;

import java.util.List;

import br.com.univates.ecoleta.db.entity.dto.GeoLocationDto;
import br.com.univates.ecoleta.db.entity.dto.GeoLocationResponseDto;
import br.com.univates.ecoleta.db.rest.NominatimExecutor;

public class GeoLocationService {

    public List<GeoLocationResponseDto> findGeoCodeAddres(GeoLocationDto geoLocationDto){
        return NominatimExecutor.getInstance().searchLocation(geoLocationDto);
    }

}

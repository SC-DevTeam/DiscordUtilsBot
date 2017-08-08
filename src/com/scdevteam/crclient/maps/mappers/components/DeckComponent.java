package com.scdevteam.crclient.maps.mappers.components;

import com.scdevteam.crclient.maps.mappers.Mapper;

public class DeckComponent extends Mapper {
    @Override
    public MapPoint[] getMapPoints() {
        return new MapPoint[] {
                new MapPoint("deck", new CardComponent())
        };
    }
}

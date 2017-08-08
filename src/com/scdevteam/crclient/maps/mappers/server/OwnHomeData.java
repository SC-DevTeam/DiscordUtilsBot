package com.scdevteam.crclient.maps.mappers.server;

import com.scdevteam.crclient.maps.mappers.Mapper;
import com.scdevteam.crclient.maps.mappers.components.DeckComponent;

public class OwnHomeData extends Mapper {
    @Override
    public MapPoint[] getMapPoints() {
        return new MapPoint[] {
                new MapPoint("Account ID", 5),
                new MapPoint("ECT Seed", 2),
                new MapPoint("", 2),
                new MapPoint("", 2),
                new MapPoint("", 2),
                new MapPoint("", 2),
                new MapPoint("", 2),
                new MapPoint("Decks", new DeckComponent()),
        };
    }
}

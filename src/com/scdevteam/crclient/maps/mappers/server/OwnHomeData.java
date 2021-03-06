package com.scdevteam.crclient.maps.mappers.server;

import com.scdevteam.crclient.maps.mappers.Mapper;
import com.scdevteam.crclient.maps.mappers.components.DeckComponent;

public class OwnHomeData extends Mapper {
    @Override
    public MapPoint[] getMapPoints() {
        return new MapPoint[] {
                new MapPoint("Account ID", MapValueType.TAG),
                new MapPoint("ECT Seed", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("Decks", new DeckComponent()),
                new MapPoint("", MapValueType.BOOLEAN),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.INT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.RRSINT32),
                new MapPoint("", MapValueType.INT32),
        };
    }
}

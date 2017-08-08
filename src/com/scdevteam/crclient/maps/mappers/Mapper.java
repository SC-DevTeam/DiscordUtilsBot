package com.scdevteam.crclient.maps.mappers;

public abstract class Mapper {
    public class MapPoint {
        private final String mName;
        private int mMapValue;
        private Mapper mComponentMapPoint;

        public MapPoint(String name, int mapValue) {
            mName = name;
            mMapValue = mapValue;
        }

        public MapPoint(String name, Mapper componentMapPoint) {
            mName = name;
            mMapValue = -1;
            mComponentMapPoint = componentMapPoint;
        }

        public String getName(int index) {
            if (mName.isEmpty()) {
                return "Unknown" + index;
            }
            return mName;
        }

        public int getMapValue() {
            return mMapValue;
        }

        public Mapper getComponent() {
            return mComponentMapPoint;
        }
    }

    public abstract MapPoint[] getMapPoints();
}

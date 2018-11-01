package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.Boxes;

public interface BoxesDAO {

    public Boxes findBoxesByBoxId(String boxId);

    public Boxes findBoxesByBoxIdAndCustomerId(String boxId, int customerId);

    public List<Boxes> findBoxesByCustomerId(int customerId);

    public Boxes findBoxesByIcsId(int icsId);

    public List<Boxes> findNearByBoxes(String fromLat, String fromLng,
            double nearByRange, int pageNumber);

    public int findNearByBoxesCount(String fromLat, String fromLng,
            double nearByRange);

    public List<Boxes> findNearByBoxesByCustomerId(int customerId,
            String fromLat, String fromLng, double nearByRange, int pageNumber);

    public int findNearByBoxesCountByCustomerId(int customerId, String fromLat,
            String fromLng, double nearByRange);

    public boolean addBoxes(Boxes boxes);

    public boolean updateBoxRegisterBoxesInfo(Boxes boxes);

    public boolean updateICSId(int icsId);

    public boolean updateLocationLatitudeAndLongitudeByBoxId(String boxId,
            String latitude, String longitude);

    public Boxes findBoxesBySerializeBoxBodyId(int serializeBoxBodyId);

    public boolean updateSerializeBoxBodyId(int serializeBoxBodyId);

}

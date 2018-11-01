package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.Boxes;

public interface BoxesBoxManagerDAO {

    public List<Boxes> findBoxesByBoxManagerIdAndPageNumber(int boxManagerId,
            int pageNumber);

    public int findBoxesCountByBoxManagerId(int boxManagerId);

}

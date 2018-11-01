package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.BoxStructure;

public interface BoxStructureDAO {

    public BoxStructure findBoxStructureByStructureId(int structureId);

    public BoxStructure findBoxStructureByStructureName(String structureName);

}

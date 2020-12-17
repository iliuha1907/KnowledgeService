package com.senla.training.knowledgeservice.dao.section;

import com.senla.training.knowledgeservice.dao.AbstractDao;
import com.senla.training.knowledgeservice.model.section.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionDaoImpl extends AbstractDao<Section, Integer>
        implements SectionDao {

    @Override
    protected Class<Section> getEntityClass() {
        return Section.class;
    }
}

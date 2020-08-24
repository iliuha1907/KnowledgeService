package com.senla.training.hoteladmin.dao.hotelservicedao;

import com.senla.training.hoteladmin.dao.AbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.util.database.HotelServiceFields;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NeedInjectionClass
public class HotelServiceDaoImpl extends AbstractDao<HotelService> implements HotelServiceDao {
    @ConfigProperty(propertyName = "db.HotelService.tableName", type = String.class)
    private static String tableName;
    private static final String fields = "(" + HotelServiceFields.price + ", " + HotelServiceFields.type + ")";
    private static final String jokers = "(?, ?)";

    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected String getFields() {
        return fields;
    }

    @Override
    protected String getInsertJokers() {
        return jokers;
    }

    @Override
    protected List<HotelService> parseSelectResultSet(ResultSet rs) {
        List<HotelService> hotelServices = new ArrayList<>();
        try {
            while (rs.next()) {
                Integer id = rs.getInt(HotelServiceFields.id.toString());
                BigDecimal price = rs.getBigDecimal(HotelServiceFields.price.toString());
                HotelServiceType type = HotelServiceType.valueOf(rs.getString(HotelServiceFields.type.toString()));
                hotelServices.add(new HotelService(id, price, type));
            }
        } catch (SQLException ex) {
            throw new BusinessException(ex.getMessage());
        }
        return hotelServices;
    }

    @Override
    protected List<Object> getInsertData(HotelService object) {
        return new ArrayList<>(Arrays.asList(object.getPrice(), object.getType().toString()));
    }
}


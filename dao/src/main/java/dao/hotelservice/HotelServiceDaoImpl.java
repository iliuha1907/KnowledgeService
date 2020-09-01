package dao.hotelservice;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import dao.AbstractDao;
import exception.BusinessException;
import model.hotelservice.HotelService;
import model.hotelservice.HotelServiceType;
import model.hotelservice.HotelServiceField;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NeedInjectionClass
public class HotelServiceDaoImpl extends AbstractDao<HotelService> implements HotelServiceDao {

    private static final String FIELDS = "(" + HotelServiceField.PRICE.toString().toLowerCase()
            + ", " + HotelServiceField.TYPE.toString().toLowerCase() + ")";
    private static final String JOKERS = "(?, ?)";
    @ConfigProperty(propertyName = "db.HotelService.tableName", type = String.class)
    private static String tableName;

    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected String getFields() {
        return FIELDS;
    }

    @Override
    protected String getInsertJokers() {
        return JOKERS;
    }

    @Override
    protected List<HotelService> parseSelectResultSet(final ResultSet rs) {
        List<HotelService> hotelServices = new ArrayList<>();
        try {
            while (rs.next()) {
                Integer id = rs.getInt(HotelServiceField.ID.toString().toLowerCase());
                BigDecimal price = rs.getBigDecimal(HotelServiceField.PRICE.toString().toLowerCase());
                HotelServiceType type = HotelServiceType.valueOf(rs.getString(HotelServiceField.
                        TYPE.toString().toLowerCase()));
                hotelServices.add(new HotelService(id, price, type));
            }
        } catch (SQLException ex) {
            logger.error("Error at parsing select result: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
        return hotelServices;
    }

    @Override
    protected List<Object> getInsertData(final HotelService object) {
        return Arrays.asList(object.getPrice(), object.getType().toString());
    }
}


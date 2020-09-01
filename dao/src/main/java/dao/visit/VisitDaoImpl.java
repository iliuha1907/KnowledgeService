package dao.visit;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import dao.AbstractDao;
import dao.hotelservice.HotelServiceDaoImpl;
import exception.BusinessException;
import model.client.Client;
import model.hotelservice.HotelService;
import model.hotelservice.HotelServiceType;
import model.visit.Visit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.client.ClientField;
import model.hotelservice.HotelServiceField;
import model.visit.VisitField;
import util.sort.VisitSortCriterion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class VisitDaoImpl extends AbstractDao<Visit> implements VisitDao {

    private static final String FIELDS = "(" + VisitField.CLIENT_ID.toString().toLowerCase() + ", "
            + VisitField.HOTEL_SERVICE_ID.toString().toLowerCase() + ", "
            + VisitField.DATE.toString().toLowerCase() + ", "
            + VisitField.IS_ACTIVE.toString().toLowerCase() + ")";
    private static final String JOKERS = "(?, ?, ?, ?)";
    private static final Logger LOGGER = LogManager.getLogger(HotelServiceDaoImpl.class);
    @ConfigProperty(propertyName = "db.Visit.tableName", type = String.class)
    private static String tableName;
    @ConfigProperty(propertyName = "db.HotelService.tableName", type = String.class)
    private static String hotelServiceTable;
    @ConfigProperty(propertyName = "db.Client.tableName", type = String.class)
    private static String clientTable;

    @Override
    public List<Visit> getSortedClientVisits(final Integer clientId, final VisitSortCriterion criterion,
                                             final Connection connection) {
        String sql = getSelectAllActiveQuery() + " and " + VisitField.CLIENT_ID.toString().toLowerCase()
                + " = ? order by " + criterion;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            return parseSelectResultSet(statement.executeQuery());
        } catch (Exception ex) {
            LOGGER.error("Error at getting sorted client visits: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Visit getById(final Integer id, final Connection connection) {
        LOGGER.error("Error at getting visits by id: Could not get by id");
        throw new BusinessException("Could not get by id");
    }

    @Override
    public void updateById(final Integer id, final Object value, final String columnName,
                           final Connection connection) {
        LOGGER.error("Error at updating visits by id: Could not update by id");
        throw new BusinessException("Could not update by id");
    }

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
    protected String getSelectAllQuery() {
        return "select * from " + tableName + ", " + clientTable + ", " + hotelServiceTable
                + " where hotel_service_id = " + hotelServiceTable + ".id" + " and "
                + clientTable + ".id = client_id";
    }

    @Override
    protected List<Visit> parseSelectResultSet(final ResultSet rs) {
        List<Visit> visits = new ArrayList<>();
        try {
            while (rs.next()) {
                Integer serviceId = rs.getInt(VisitField.HOTEL_SERVICE_ID.toString().toLowerCase());
                BigDecimal servicePrice = rs.getBigDecimal(HotelServiceField.PRICE.toString().toLowerCase());
                HotelServiceType serviceType = HotelServiceType.valueOf(rs.getString(
                        HotelServiceField.TYPE.toString().toLowerCase()));
                String clientFirstName = rs.getString(ClientField.FIRST_NAME.toString().toLowerCase());
                String clientLastName = rs.getString(ClientField.LAST_NAME.toString().toLowerCase());
                Integer clientId = rs.getInt(VisitField.CLIENT_ID.toString().toLowerCase());
                Date date = rs.getDate(VisitField.DATE.toString().toLowerCase());
                boolean isActive = rs.getBoolean(VisitField.IS_ACTIVE.toString().toLowerCase());
                visits.add(new Visit(new Client(clientId, clientFirstName, clientLastName),
                        new HotelService(serviceId, servicePrice, serviceType), date, isActive));
            }
        } catch (SQLException ex) {
            LOGGER.error("Error at parsing select result: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
        return visits;
    }

    @Override
    protected List<Object> getInsertData(final Visit object) {
        return Arrays.asList(object.getClient().getId(), object.getHotelService().getId(),
                object.getDate(), object.isActive());
    }

    private String getSelectAllActiveQuery() {
        return getSelectAllQuery() + " and "
                + VisitField.IS_ACTIVE.toString().toLowerCase() + " = 1";
    }
}


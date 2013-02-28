package net.iteach.service.db;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public final class SQLUtils {

    private SQLUtils() {
    }

    private static final BigDecimal MINUTES_IN_HOUR = BigDecimal.valueOf(60);

    public static BigDecimal getHours(LocalTime from, LocalTime to) {
        Period duration = new Period(from, to);
        BigDecimal minutes = new BigDecimal(duration.toStandardMinutes().getMinutes());
        return minutes.divide(MINUTES_IN_HOUR, 2, RoundingMode.HALF_UP);
    }

    public static String dateToDB(LocalDate date) {
        return date.toString();
    }

    public static LocalDate dateFromDB(String str) {
        return LocalDate.parse(str);
    }

    public static String timeToDB(LocalTime time) {
        return time.toString("HH:mm");
    }

    public static LocalTime timeFromDB(String str) {
        return LocalTime.parse(str);
    }

    public static DateTime now() {
        return DateTime.now(DateTimeZone.UTC);
    }

    public static Timestamp toTimestamp(DateTime dateTime) {
        return new Timestamp(dateTime.getMillis());
    }

    public static DateTime getDateTime(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return getDateTime(timestamp);
    }

    public static DateTime getDateTime(Timestamp timestamp) {
        return timestamp != null ? new DateTime(timestamp.getTime(), DateTimeZone.UTC) : null;
    }

    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null) {
            return null;
        } else {
            return Enum.valueOf(enumClass, value);
        }
    }

    public static Money moneyFromDB(ResultSet rs, String column) throws SQLException {
        BigDecimal amount = rs.getBigDecimal(column);
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        return Money.of(CurrencyUnit.EUR, amount);
    }

}

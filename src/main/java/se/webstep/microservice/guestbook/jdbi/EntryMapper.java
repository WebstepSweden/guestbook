package se.webstep.microservice.guestbook.jdbi;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import se.webstep.microservice.guestbook.core.Entry;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntryMapper implements ResultSetMapper<Entry> {
    @Override
    public Entry map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        // TODO
        return null;
    }
}
package ch.ffhs.hdo.persistence.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.ffhs.hdo.persistence.dto.RegelDto;
import ch.ffhs.hdo.persistence.dto.RegelsetDto;
import ch.ffhs.hdo.persistence.jdbc.JdbcHelper;

public class RegelsetDao extends JdbcHelper {

	private final String SELECTRULESETS = "SELECT RULESET.* FROM RULESET ORDER BY priority ASC";

	private final String FIND_BIGGER_PRIO = "(Select id from Ruleset where priority = (SELECT min(priority) from RULESET where priority > (SELECT priority  FROM RULESET where id = ?)))";
	private final String SWAP_DOWN = "UPDATE RULESET SET priority = ( SELECT SUM(priority) FROM RULESET WHERE id IN (?, "
			+ FIND_BIGGER_PRIO + ") ) - priority WHERE id IN (?, " + FIND_BIGGER_PRIO + ")";

	private final String FIND_SMALLER_PRIO = "(Select id from Ruleset where priority = (SELECT max(priority) from RULESET where priority < (SELECT priority  FROM RULESET where id = ?)))";
	private final String SWAP_UP = "UPDATE RULESET SET priority = ( SELECT SUM(priority) FROM RULESET WHERE id IN (?, "
			+ FIND_SMALLER_PRIO + ") ) - priority WHERE id IN (?, " + FIND_SMALLER_PRIO + ")";

	private final String INSERT = "INSERT INTO RULESET (targetDirectory, rulesetName, newFilename, filenameCounter, priority, active, creationDate, changedate) VALUES (?,?,?,?,?,?, CURTIME () ,CURTIME () )";

	private final String INSERT_RULESET = "INSERT INTO RULESET (targetDirectory, rulesetName, newFilename, filenameCounter, priority, active, creationDate, changedate) VALUES (?,?,?,?,?,?, CURTIME () ,CURTIME () )";
	private final String INSERT_RULE = "INSERT INTO RULE (rulesetId, contextType, contextAttribute, compareType, compareValue, creationDate, changedate) VALUES (?,?,?,?,?, CURTIME () ,CURTIME () )";
	
	private final String UPDATE_RULESET = "UPDATE RULESET SET (targetDirectory, rulesetName, newFilename, filenameCounter, priority, active, creationDate, changedate) VALUES (?,?,?,?,?,?, CURTIME () ,CURTIME () ) where id = ?";
	

	private final String DELETE_RULE = "DELETE FROM RULE where rulesetId = ?";
	private final String DELETE_RULESET = "DELETE FROM RULESET where id = ?";

	public List<RegelsetDto> findAllRegelsets() throws SQLException {

		PreparedStatement selectAllRegelsets = conn.prepareStatement(SELECTRULESETS);

		ResultSet executeQuery = selectAllRegelsets.executeQuery();

		List<RegelsetDto> regelsetlist = new ArrayList<RegelsetDto>();

		while (executeQuery.next()) {

			RegelsetDto dto = new RegelsetDto();
			dto.setId(executeQuery.getInt("id"));
			dto.setActive(executeQuery.getBoolean("active"));
			dto.setNewFilename(executeQuery.getString("newFilename"));
			dto.setTargetDirectory(executeQuery.getString("targetDirectory"));
			dto.setChangedate(executeQuery.getDate("changedate"));
			dto.setCreationDate(executeQuery.getDate("creationDate"));
			dto.setFilenameCounter(executeQuery.getLong("filenameCounter"));
			dto.setPrority(executeQuery.getInt("priority"));
			dto.setRulesetName(executeQuery.getString("rulesetName"));
			dto.setRegeln(new ArrayList<RegelDto>());

			regelsetlist.add(dto);

		}

		return regelsetlist;
	}

	public void save(RegelsetDto regelsetDto, boolean newEntry) throws SQLException {
		PreparedStatement insertRegelset = null;

		if (newEntry) {
			update(regelsetDto);
		} else {
			insertRegelset = conn.prepareStatement(INSERT_RULESET);
			insertRegelset.setString(1, regelsetDto.getTargetDirectory());
			insertRegelset.setString(2, regelsetDto.getRulesetName());
			insertRegelset.setString(3, regelsetDto.getNewFilename());
			insertRegelset.setLong(4, regelsetDto.getFilenameCounter());
			insertRegelset.setInt(5, regelsetDto.getPrority());
			insertRegelset.setBoolean(6, regelsetDto.isActive());

			insertRegelset.executeUpdate();
		}

		terminate();
	}

	private void update(RegelsetDto regelsetDto) throws SQLException {
		PreparedStatement updateRegelset = null;

		updateRegelset = conn.prepareStatement(INSERT_RULESET);
		updateRegelset.setString(1, regelsetDto.getTargetDirectory());
		updateRegelset.setString(2, regelsetDto.getRulesetName());
		updateRegelset.setString(3, regelsetDto.getNewFilename());
		updateRegelset.setLong(4, regelsetDto.getFilenameCounter());
		updateRegelset.setInt(5, regelsetDto.getPrority());
		updateRegelset.setBoolean(6, regelsetDto.isActive());
		updateRegelset.setInt(7,  regelsetDto.getId());
		
		updateRegelset.executeUpdate();
		
		terminate();
	}

	public void changePrioDown(int id) throws SQLException {

		final PreparedStatement ruleset = conn.prepareStatement(SWAP_DOWN);
		ruleset.setInt(1, id);
		ruleset.setInt(2, id);
		ruleset.setInt(3, id);
		ruleset.setInt(4, id);
		final int executeUpdate = ruleset.executeUpdate();

	}

	public void changePrioUp(int id) throws SQLException {

		final PreparedStatement ruleset = conn.prepareStatement(SWAP_UP);
		ruleset.setInt(1, id);
		ruleset.setInt(2, id);
		ruleset.setInt(3, id);
		ruleset.setInt(4, id);
		ruleset.executeUpdate();

	}

	public void deleteRegelset(int regelsetId) throws SQLException {

		try {
			conn.setAutoCommit(false);

			final PreparedStatement deleteRule = conn.prepareStatement(DELETE_RULE);
			deleteRule.setInt(1, regelsetId);
			deleteRule.executeUpdate();

			final PreparedStatement deleteRuleset = conn.prepareStatement(DELETE_RULESET);
			deleteRuleset.setInt(1, regelsetId);
			deleteRuleset.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);

		} catch (SQLException e) {
			conn.rollback();
			throw new SQLException(e);

		}

	}

}
/*
 * Copyright 2015, 2016, 2025 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.beefeater;

import java.util.List;

import se.uu.ub.cora.beefeater.authentication.User;
import se.uu.ub.cora.beefeater.authorization.Rule;

public interface Authorizator {

	boolean providedRulesSatisfiesRequiredRules(List<Rule> providedRules, List<Rule> requiredRules);

	List<Rule> providedRulesMatchRequiredRules(List<Rule> providedRules, List<Rule> requiredRules);

	/**
	 * userIsAuthorizedForActionOnRecordType is used to check if a user is allowed to perform any
	 * action on the record based on user permissionsUnits, if recordType uses permissionUnits and
	 * permissionUnit associated with the record.
	 * 
	 * @param user
	 *            The {@link User} to check if is authorized.
	 * @param recordTypeUsesPermissionUnit
	 *            A boolean indicating if the recordType uses permissionUnit
	 * @param recordPermissionUnit
	 *            The permissionUnit associated with the record
	 * @return a boolean, true if the user is allowed to regarding its permissionUnits.
	 */
	boolean checkUserIsAuthorizedForPemissionUnit(User user, boolean recordTypeUsesPermissionUnit,
			String recordPermissionUnit);

}

/**
 * Copyright (C) 2014 4th Line GmbH, Switzerland and others
 *
 * The contents of this file are subject to the terms of either the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jupnp.tool.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * @author Jochen Hiller - Initial contribution
 */
public class SearchCommandSortByValidator implements IParameterValidator {
	public void validate(String name, String value) throws ParameterException {
		if (name.equals("--sort")) {
			if ((value.equalsIgnoreCase("ip"))
					|| (value.equalsIgnoreCase("model"))
					|| (value.equalsIgnoreCase("serialNumber"))
					|| (value.equalsIgnoreCase("manufacturer"))
					|| (value.equalsIgnoreCase("udn"))) {
			} else {
				throw new ParameterException("Parameter " + name
						+ " must be {ip|model|serialNumber} (found " + value
						+ ")");
			}
		}
	}
}

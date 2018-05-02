/* IRL World Minecraft Mod
    Copyright (C) 2017  Smyler

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
	along with this program. If not, see <http://www.gnu.org/licenses/>.

	The author can be contacted at smyler@mail.com
*/
package com.github.smylermc.irlw.maps.exceptions;

/**
 * @author SmylerMC
 *
 */
public class InvalidMapboxSessionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9055342748959147290L;

	public InvalidMapboxSessionException() {
		super("It seems like your mapbox session is invalid, is your access token correctly set?");
	}
	
	
	public InvalidMapboxSessionException(String messsage) {
		super(messsage);
	}
	
}

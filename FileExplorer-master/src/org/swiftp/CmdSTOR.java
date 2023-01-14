/*
Copyright 2009 David Revell

This file is part of SwiFTP.

SwiFTP is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SwiFTP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.swiftp;


public class CmdSTOR extends CmdAbstractStore implements Runnable {
	protected String input;
	
	public CmdSTOR(SessionThread sessionThread, String input) {
		super(sessionThread, CmdSTOR.class.toString());
		this.input = input;
	}
	
	public void run() {
		doStorOrAppe(getParameter(input), false);
	}
}

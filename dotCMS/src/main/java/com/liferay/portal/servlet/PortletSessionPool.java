/**
 * Copyright (c) 2000-2005 Liferay, LLC. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portal.servlet;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.liferay.util.CollectionFactory;

/**
 * <a href="PortletSessionPool.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public class PortletSessionPool {

	public static Map get(String sessionId) {
		return _getInstance()._get(sessionId);
	}

	public static void put(String sessionId, HttpSession session) {
		_getInstance()._put(sessionId, session);
	}

	public static Map remove(String sessionId) {
		return _getInstance()._remove(sessionId);
	}

	private static PortletSessionPool _getInstance() {
		if (_instance == null) {
			synchronized (PortletSessionPool.class) {
				if (_instance == null) {
					_instance = new PortletSessionPool();
				}
			}
		}

		return _instance;
	}

	private PortletSessionPool() {
		_sessionPool = CollectionFactory.getSyncHashMap();
	}

	private Map _get(String sessionId) {
		Map sessions = (Map)_sessionPool.get(sessionId);

		if (sessions == null) {
			sessions = new LinkedHashMap();

			_sessionPool.put(sessionId, sessions);
		}

		return sessions;
	}

	private void _put(String sessionId, HttpSession session) {
		Map sessions = _get(sessionId);

		if (!sessions.containsKey(session.getId())) {
			sessions.put(session.getId(), session);
		}
	}

	private Map _remove(String sessionId) {
		return (Map)_sessionPool.remove(sessionId);
	}

	private static PortletSessionPool _instance;

	private Map _sessionPool;

}
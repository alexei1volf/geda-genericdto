/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.osgi.test;

import java.math.BigDecimal;

/**
 * User: denispavlov
 * Date: 13-02-21
 * Time: 8:21 AM
 */
public interface SimpleEntity {
    String getString();

    void setString(String string);

    BigDecimal getDecimal();

    void setDecimal(BigDecimal decimal);

    int getInteger();

    void setInteger(int integer);
}

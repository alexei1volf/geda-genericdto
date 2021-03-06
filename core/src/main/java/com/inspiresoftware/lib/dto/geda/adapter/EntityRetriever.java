
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.adapter;

/**
 * Adapter for delegate parent entities.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public interface EntityRetriever extends Adapter {
	
	/**
	 * Method that allows to retrieve entity from persistence layer by providing the interface, class and primary key.
	 * 
	 * @param entityInterface interface that is used on DTO (this maybe just class if no interface is used)
	 * @param entityClass the concrete class that is used by entity object.
	 * @param primaryKey the primary key value to use.
	 * 
	 * @return entity that is retrieved from persistence layer.
	 */
	@SuppressWarnings("unchecked")
	Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey);

}

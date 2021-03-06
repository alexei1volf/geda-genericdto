/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */
package com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors.dto;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

import java.math.BigDecimal;

@Dto
public class CustomerOrderDeliveryDetailDTOImpl implements CustomerOrderDeliveryDetailDTO {

    private static final long serialVersionUID = 20120812L;

    @DtoField(value = "qty")
    private BigDecimal qty;

    /** {@inheritDoc} */
    public BigDecimal getQty() {
        return qty;
    }

    /** {@inheritDoc} */
    public void setQty(final BigDecimal qty) {
        this.qty = qty;
    }

}

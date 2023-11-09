package com.dufler.swt.utils.elements.table.template;

import com.dufler.swt.utils.elements.Ordinatore;

public class TemplateOrdinatore extends Ordinatore<TemplateEntity> {

	@Override
	protected int compare(TemplateEntity p1, TemplateEntity p2, int property) {
		int rc;
		
		switch (propertyIndex) {
	        case 0:
	                rc = p1.getFirstName().compareTo(p2.getFirstName());
	                break;
	        case 1:
	                rc = p1.getLastName().compareTo(p2.getLastName());
	                break;
	        case 2:
	                rc = p1.getGender().compareTo(p2.getGender());
	                break;
	        case 3:
	                if (p1.isMarried() == p2.isMarried()) {
	                        rc = 0;
	                } else
	                        rc = (p1.isMarried() ? 1 : -1);
	                break;
	        default: rc = 0;
        }
		
		return rc;
	}

}

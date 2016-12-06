package ch.ffhs.hdo.infrastructure.regelset;

import ch.ffhs.hdo.client.ui.regelset.RegelModel;
import ch.ffhs.hdo.client.ui.regelset.RegelModel.ComparisonTypeEnum;
import ch.ffhs.hdo.client.ui.regelset.RegelModel.ContextAttributeEnum;
import ch.ffhs.hdo.client.ui.regelset.RegelModel.ContextTypeEnum;
import ch.ffhs.hdo.persistence.dto.RegelDto;

public class RegelConverter {

	public static RegelModel convert(RegelDto dto, Integer id) {

		RegelModel model = new RegelModel();

		model.setId(id);					
		model.setContextType(ContextTypeEnum.valueOf(dto.getContextType()));
		model.setContextAttribute(ContextAttributeEnum.valueOf(dto.getContextAttribute()));
		model.setComparisonType(ComparisonTypeEnum.valueOf(dto.getCompareType()));
		model.setCompareValue(dto.getCompareValue());

		return model;

	}

	public static RegelDto convert(RegelModel model, Integer id) {

		final RegelDto regelDto = new RegelDto();
		
		regelDto.setCompareType(model.getComparisonType().toString());
		regelDto.setCompareValue(model.getCompareValue());
		regelDto.setCompareType(model.getComparisonType().toString());
		regelDto.setContextAttribute(model.getContextAttribute().toString());
		regelDto.setRulesetId(id);
		regelDto.setId(model.getId());
		
		return regelDto;

	}

}
